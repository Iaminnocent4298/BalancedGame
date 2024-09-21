#!/bin/bash
ps ax | grep gunicorn | grep -v grep | awk '{ print $1 }' | xargs -r kill -9
