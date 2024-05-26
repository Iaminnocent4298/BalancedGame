
from pyinotify import WatchManager, ProcessEvent, ThreadedNotifier
import pyinotify
import globals
import os
from logging import warn, debug

class EventHandler(ProcessEvent):
    def process_IN_MODIFY(self, event):
        pathname = os.path.relpath(event.pathname, globals.BASE_PATH)
        if not pathname.startswith("log") and not pathname.startswith("dist"):
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
    wdd = wm.add_watch(globals.BASE_PATH, pyinotify.IN_MODIFY, rec=True)

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
    
    hooks[id] = (path, callback)

def unregister(id):
    del hooks[id]