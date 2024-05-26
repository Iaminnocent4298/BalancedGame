#!/bin/bash
ps ax | grep gunicorn | head -n -1 | awk '{ print $1 }' | xargs kill -9

