#!/bin/bash
ps ax | grep gunicorn | grep -v grep | cut -d' ' -f2 | xargs -r kill -9
