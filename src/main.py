
import gevent.monkey
gevent.monkey.patch_all()

import flask
from flask import Flask, Response, request, session, redirect, url_for
from functools import wraps

import flask_wtf.file
from flask_wtf import FlaskForm, CSRFProtect
import wtforms
from wtforms.validators import DataRequired, ValidationError

from flask_socketio import SocketIO, disconnect, send, emit
import os, os.path as path

import interface, interface.data

from threading import Thread

from globals import BASE_PATH
import changelog

from logging import info

from werkzeug.middleware.proxy_fix import ProxyFix

import dotenv, log_setup, atexit
log_setup.setup()
dotenv.load_dotenv()


ADMIN_PASSWORD = os.getenv("ADMIN_PASSWORD")

app = Flask(__name__, root_path=BASE_PATH, static_folder=path.join(BASE_PATH, "static"))
app.wsgi_app = ProxyFix(
    app.wsgi_app, x_for=1, x_proto=1, x_host=1, x_prefix=1
)
# app.config["REDIS_URL"] = "redis://localhost"
app.config["JSONIFY_PRETTYPRINT_REGULAR"] = False
if __name__ == "__main__":
    app.config["TEMPLATES_AUTO_RELOAD"] = True

csrf = CSRFProtect(app)

sock = SocketIO(app, logger=True, async_mode="threading", cors_allowed_origins="*")

def render_template(filename, **kwargs):
    return flask.render_template(filename, **kwargs, minor_game_data=interface.data.game_data)

def authenticated():
    return "authenticated" in session

def redirect_for(*args, **kwargs):
    if args and args[0] == "index":
        redirect("/")
    return redirect(url_for(*args, **kwargs))

@app.route("/")
def index():
    return render_game("maina")

class SetGameDataForm(FlaskForm):
    file = wtforms.FileField("File:", validators=[flask_wtf.file.FileRequired(), flask_wtf.file.FileAllowed(["json"], "Not a valid json file!")])

def render_game(game_id):
    game_data = interface.data.game_datas
    if authenticated():
        return render_template("admin.html", game_data=game_data, game_id=game_id, game_data_form=SetGameDataForm())
    return render_template("index.html", game_data=game_data, game_id=game_id)

@app.route("/game/<game_id>")
def game(game_id):
    if game_id not in interface.data.game_data:
        return not_found()
    return render_game(game_id)

@app.route("/balancedgameinfo")
def info_redir():
    return redirect("https://docs.google.com/spreadsheets/d/1PGaQ40GtAoWpAUI7J8gqUTi9pyk8zkza6wVBtjtjOfg/edit")
@app.route("/mobwiki")
def mobwiki_redir():
    return redirect("https://docs.google.com/document/d/1RxGX2aEl6rdOKYhA1VFyinZkzsneSiQcoNvKSMtUSmE/edit")

@app.errorhandler(404)
def not_found(*args):
    return render_template("404.html"), 404

@app.errorhandler(500)
def internal_server_error(*args):
    return render_template("500.html"), 500

def auth_required(f):
    @wraps(f)
    def decorator(*args, **kwargs):
        if not authenticated():
            return redirect_for("auth")
        return f(*args, **kwargs)
    return decorator

def sock_auth_required(f):
    @wraps(f)
    def decorator(*args, **kwargs):
        if not authenticated():
            disconnect()
        else:
            return f(*args, **kwargs)
    return decorator

@app.route("/auth", methods=["GET", "POST"])
def auth():
    class AuthForm(FlaskForm):
        password = wtforms.PasswordField("Password:", validators=[DataRequired()])

        def validate_password(form, field):
            if field.data != ADMIN_PASSWORD:
                raise ValidationError("Incorrect password")

    form = AuthForm()
    if form.validate_on_submit():
        session["authenticated"] = 1
        return redirect_for("index")
    return render_template("auth.html", form=form)

@app.route("/logout")
def logout():
    if authenticated():
        del session["authenticated"]
    return redirect_for("index")

@app.route("/api/game_data", methods=["GET"])
def game_data():
    return interface.data.game_datas

@app.route("/api/game_data/<game_id>", methods=["GET"])
def game_data_with_id(game_id):
    if game_id not in interface.data.game_datas:
        return not_found()
    return interface.data.game_datas[game_id]

@app.route("/api/set_game_data/<game_id>", methods=["POST"])
@auth_required
def set_game_data(game_id):
    if game_id not in interface.data.game_data:
        return not_found()
    
    file = request.files["file"]
    import json
    try:
        data = json.load(file)
    except:
        return "invalid json file >:( (amazing error page right)", 400
    
    info("Setting data of %s to uploaded file" % game_id)

    interface.data.set_data(game_id, data)
    
    return redirect_for("game", game_id=game_id)

@app.route("/api/changelog", methods=["GET"])
def get_changelog():
    return changelog.changelog

@app.route("/api/giveap", methods=["POST"])
def giveap():
    # lmao
    return Response(response="nope!", status_code=403)

def send_communication_data(text):
    if text is None:
        return
    
    if interface.process is None:
        emit("error", { "code": "process_not_started" })

    def func():
        stdout, stderr = interface.communicate(text)
        sock.emit("term_data", {
            "stdout": stdout,
            "stderr": stderr,
            "closed": interface.process is None,
        })
    
    Thread(target=func).start()

@sock.on("term_start")
@sock_auth_required
def start_process():
    if interface.process:
        emit("error", { "code": "process_already_started" })
        return

    info("Starting process")

    def func():
        interface.start_process()
        send_communication_data("")

    Thread(target=func).start()

@sock.on("term_last")
@sock_auth_required
def send_last():
    if interface.process is None:
        return {
            "closed": True,
        }
    
    last = []
    cur_fd = None
    cur_list = []
    for fd, text in interface.text_queue:
        if fd == cur_fd:
            cur_list.append(text)
        else:
            if cur_list:
                last.append((cur_fd, "".join(cur_list)))
            cur_fd = fd
            cur_list.clear()
            cur_list.append(text)
    if cur_list:
        last.append((cur_fd, "".join(cur_list)))

    return {
        "closed": False,
        "last": last,
    }

@sock.on("force_disconnect")
@sock_auth_required
def force_dcon():
    emit("dont_force_dcon_youre_fine")
    sock.emit("force_disconnect")

@sock.on("term_send")
@sock_auth_required
def send_to_program(json):
    send_communication_data(json.get("input"))

def update_game_data(game_id, data):
    with app.app_context():
        sock.emit("data", {
            "game_id": game_id,
            "data": data,
        })

def init():
    interface.init()
    changelog.init()
    interface.data.set_callback(update_game_data)

def deinit():
    interface.deinit()
    changelog.deinit()

init()
atexit.register(deinit)

app.secret_key = bytes.fromhex(os.getenv("SECRET_KEY"))

if __name__ == "__main__":
    sock.run(app, host="0.0.0.0", port=8082)
