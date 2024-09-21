#!/bin/bash
set -e

mkdir -p log
cd src && python3 -m gunicorn -w 1 -b 0.0.0.0:3025 'main:app' -k gevent --worker-connections 1000 --access-logfile ../log/access.log --error-logfile ../log/general.log
