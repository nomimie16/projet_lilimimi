#!/bin/bash
xdotool mousemove 1280 1024
cd projet/Sudoku
touch highscore
java -cp .:../..:/home/pi/git/MG2D Main
