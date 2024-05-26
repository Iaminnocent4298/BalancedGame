
import globals
import json
from logging import info, warn
import monitor
import util
import functools

import os.path as path
import json

with open(path.join(path.dirname(__file__), "games.json")) as file:
    game_data = json.load(file)

DIST_PATH = path.join(globals.BASE_PATH, "dist")

# mmm grammar
game_datas = {}

game_data_callback = None

def set_callback(callback):
    global game_data_callback
    game_data_callback = callback

def update_game_data_undebounced(game_id, game_data_json, event):
    try:
        with open(path.join(DIST_PATH, game_data_json), "r") as file:
            content = file.read().strip()
            if not content:
                new_game_data = None
            else:
                new_game_data = json.loads(content)
    except FileNotFoundError:
        warn("The file for %s (%s) doesn't exist!" % (game_id, game_data_json))
    except json.decoder.JSONDecodeError:
        warn("%s is invalid json, ignoring" % game_data_json)
        return
    
    if game_id in game_datas and new_game_data == game_datas[game_id]:
        if event:
            info("%s marked as modified but not actually modified" % game_data_json)
        return
    
    if event:
        info("%s modified, reloading game data" % game_data_json)
    else:
        info("Reading %s for the first time" % game_data_json)

    game_datas[game_id] = new_game_data
    
    if game_data_callback:
        game_data_callback(game_id, new_game_data)

def init():
    for game_id, data in game_data.items():
        game_data_file = data["file"]
        update_game_data_undebounced(game_id, game_data_file, None)
        monitor.register("data_" + game_id, path.join("dist", game_data_file), functools.partial(util.debounce(0.1)(update_game_data_undebounced), game_id, game_data_file))

def deinit():
    for game_id in game_data:
        monitor.unregister("data_" + game_id)

def set_data(game_id, data):
    game_data_file = game_data[game_id]["file"]
    with open(path.join("dist", game_data_file), "w") as file:
        json.dump(data, file)
    game_datas[game_id] = data