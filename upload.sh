#!/bin/bash
if [ ! -f .env ]; then
    echo '.env does not exist. cannot get upload path'
    echo 'Try setting REMOTE_PATH="..." in .env and try again'
    exit 1
fi
source .env
if [ ! -n "$REMOTE_PATH" ]; then
    echo 'REMOTE_PATH is empty or null. cannot get upload path'
    echo 'Try setting REMOTE_PATH="..." in .env and try again'
    exit 1
fi
rsync -avr . --exclude-from='rsync-exclude.txt' "$REMOTE_PATH" $@
