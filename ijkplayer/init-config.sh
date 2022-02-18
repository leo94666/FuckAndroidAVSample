#! /usr/bin/env bash


echo "init-config.sh================================================================start"


if [ ! -f 'config/module.sh' ]; then
    #cp config/module-default.sh config/module.sh
    cp config/module-lite.sh config/module.sh

fi

echo "init-config.sh================================================================end"
