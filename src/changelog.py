
import monitor, globals, os.path as path

CHANGELOG_PATH = path.join(globals.BASE_PATH, "java", "changelog.html")
changelog = None

def reload_changelog():
    global changelog
    with open(CHANGELOG_PATH) as file:
        changelog = file.read()

def init():
    monitor.register("changelog", CHANGELOG_PATH, reload_changelog)
    reload_changelog()

def deinit():
    monitor.unregister("changelog")