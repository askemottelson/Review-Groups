#!/bin/bash

if [ $1 = "clean" ]; then
    rm -rf dist
    rm -rf build
    rm -rf store
else
    java -jar store/Review.jar $1 $2 $3 $4 $5
fi



