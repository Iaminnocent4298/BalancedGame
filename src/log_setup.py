
from logging.config import dictConfig
import os.path
import globals

def setup():
    dictConfig(
        {
            "version": 1,
            "formatters": {
                "default": {
                    "format": "%(asctime)s %(levelname)s:\n%(message)s\n"
                },
            },
            "handlers": {
                "console": {
                    "class": "logging.StreamHandler",
                    "stream": "ext://sys.stdout",
                    "formatter": "default",
                },
                "file": {
                    "class": "logging.FileHandler",
                    "filename": os.path.join(globals.BASE_PATH, "log", "flask.log"),
                    "formatter": "default",
                },
            },
            "root": {"level": "DEBUG", "handlers": ["console", "file"]},
        }
    )
