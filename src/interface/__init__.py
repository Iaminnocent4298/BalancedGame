
import signal, subprocess, os, os.path as path
from subprocess import TimeoutExpired
from logging import debug, info, warn, error
from threading import RLock
import interface.data as data
import globals
import monitor
import time
import collections
import util

JAVA_NAME = "BalancedGame"

IS_RASPI = os.uname().nodename == "raspberrypi"

process = None
mutex = RLock()

observer = None

compile_queued = False

stdout_fd = None
stderr_fd = None

ptys = None

TEXT_QUEUE_LINES = 50

text_queue = collections.deque()

DIST_PATH = path.join(globals.BASE_PATH, "dist")

def start_process():
    global process, stdout_fd, stderr_fd, ptys
    with mutex:
        if process is not None:
            raise RuntimeError("multiple processes tried to start at the same time")
        ptys = (os.openpty(), os.openpty())
        process = subprocess.Popen(["java", JAVA_NAME], stdin=subprocess.PIPE, stdout=ptys[0][1], stderr=ptys[1][1], cwd=DIST_PATH, text=True, encoding="utf-8", bufsize=0)

        stdout_fd = ptys[0][0]
        stderr_fd = ptys[1][0]
        os.set_blocking(stdout_fd, False)
        os.set_blocking(stderr_fd, False)

        # sleep because java is slow and takes a long time to initialize
        time.sleep(1)

def stop_process():
    global process, stdout_fd, stderr_fd, ptys, compile_queued
    with mutex:
        if process is None:
            raise RuntimeError("tried to stop nonexistent process")

        process.send_signal(signal.SIGINT)
        try:
            process.wait(timeout=1)
        except TimeoutExpired:
            warn("Process took >1s to exit, killing")
            process.kill()

        process = None
        stdout_fd = None
        stderr_fd = None

        if ptys:
            for pty in ptys:
                for fd in pty:
                    os.close(fd)

        text_queue.clear()

        # if compile_queued:
        #     compile_queued = False
        #     compile()

def communicate(s, wait=4, timeout=0.05):
    global process

    with mutex:
        stdouts, stderrs = [], []

        if s:
            try:
                text_queue.append((0, s))
                process.stdin.write(s)
                process.stdin.flush()
            except BrokenPipeError:
                pass
            else:
                for i in range(wait * 20):
                    if process.poll() is not None:
                        break
                    try:
                        stdouts.append(str(os.read(stdout_fd, 4096), "utf-8"))
                        break
                    except BlockingIOError:
                        pass
                    try:
                        stderrs.append(str(os.read(stderr_fd, 4096), "utf-8"))
                        break
                    except BlockingIOError:
                        pass
                    time.sleep(0.05)

        time.sleep(timeout)

        try:
            while True:
                stdouts.append(str(os.read(stdout_fd, 4096), "utf-8"))
        except BlockingIOError:
            pass
        try:
            while True:
                stderrs.append(str(os.read(stderr_fd, 4096), "utf-8"))
        except BlockingIOError:
            pass

        stdout = "".join(stdouts)
        stderr = "".join(stderrs)

        def lineify(s, is_last):
            s = s.replace("\r", "")
            return s + "\n" if not is_last else s

        if stdout:
            out_split = stdout.split("\n")
            for i, line in enumerate(out_split):
                text_queue.append((1, lineify(line, i == len(out_split) - 1)))
        if stderr:
            err_split = stderr.split("\n")
            for i, line in enumerate(err_split):
                text_queue.append((2, lineify(line, i == len(err_split) - 1)))

        while len(text_queue) > TEXT_QUEUE_LINES:
            text_queue.popleft()

        if process.returncode is not None:
            if process.returncode != 0:
                warn(f"Process exited abnormally after communication with exit code {process.returncode}")
                warn(stderr)
            process = None

        return stdout, stderr

# TODO because there are so many files, recompiling is kinda obsolete, possibly remove this code?

# def compile():
#     with mutex:
#         info("Recompiling...")
#         try:
#             output = subprocess.run(["javac", JAVA_NAME + ".java"], timeout=15, capture_output=True, text=True, encoding="utf-8", check=True, cwd=DIST_PATH)
#             if output.stdout:
#                 debug(output.stdout)
#         except subprocess.TimeoutExpired as err:
#             error("Java compilation took >15s")
#             raise err
#         except subprocess.CalledProcessError as err:
#             error("Java compilation failed!")
#             error(err.stderr)
#             raise err

# @util.debounce(0.1)
# def queue_compilation():
#     global compile_queued
#     if process:
#         info("Queueing compilation")
#         compile_queued = True
#     else:
#         compile()

def init():
    global observer

    data.init()

    # if not IS_RASPI:
    #     monitor.register("program", JAVA_NAME + ".java", lambda _: queue_compilation())
    #     compile()

def deinit():
    global observer

    data.deinit()

    # if not IS_RASPI:
    #     monitor.unregister("program")
