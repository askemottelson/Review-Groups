#!/bin/bash

if [ $1 = "clean" ]; then
    rm -rf review-data
    rm -rf dist
    rm -rf build
else
    java -jar dist/review-combined.jar $1 $2 $3 $4 $5
fi



