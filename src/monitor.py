
from pyinotify import WatchManager, ProcessEvent, ThreadedNotifier
import pyinotify
import globals
import os
from logging import warn, debug

class EventHandler(ProcessEvent):
    def process_IN_MODIFY(self, event):
        pathname = os.path.abspath(event.pathname)
        if pathname.startswith("log"):
            return
        debug("%s modified" % pathname)
        for path, callback in hooks.values():
            if pathname == path:
                callback(event)
        

wm = pyinotify.WatchManager()
notifier = None
wdd = None
hooks = {}

def init():
    global notifier, wdd
    
    notifier = ThreadedNotifier(wm, EventHandler())
    notifier.start()
    wdd = wm.add_watch(os.path.join(globals.BASE_PATH, "dist"), pyinotify.IN_MODIFY, rec=True)

def deinit():
    global notifier, wdd
    wm.rm_watch(wdd.values())
    notifier.stop()
    hooks.clear()

def register(id, path, callback):
    if notifier is None:
        init()
    
    if id in hooks:
        warn(f"hook id {id} already registered, overwriting")
    
    debug(f"Registering hook {id} for {path}")

    hooks[id] = (path, callback)

def unregister(id):
    del hooks[id]