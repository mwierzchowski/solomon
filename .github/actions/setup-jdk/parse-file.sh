#!/bin/bash
set -e

javaPattern="^java=(.*)-(.*)$"
while IFS='' read -r line || [ -n "$line" ]; do
  if [[ $line =~ $javaPattern ]]; then
    echo "version=${BASH_REMATCH[1]}" >> "$GITHUB_OUTPUT"
    if [[ ${BASH_REMATCH[2]} == "amzn" ]]; then
      echo "distribution=corretto" >> "$GITHUB_OUTPUT"
    elif [[ ${BASH_REMATCH[2]} == "librca" ]]; then
      echo "distribution=liberica" >> "$GITHUB_OUTPUT"
    elif [[ ${BASH_REMATCH[2]} == "ms" ]]; then
      echo "distribution=microsoft" >> "$GITHUB_OUTPUT"
    elif [[ ${BASH_REMATCH[2]} == "oracle" ]]; then
      echo "distribution=oracle" >> "$GITHUB_OUTPUT"
    elif [[ ${BASH_REMATCH[2]} == "sem" ]]; then
      echo "distribution=semeru" >> "$GITHUB_OUTPUT"
    elif [[ ${BASH_REMATCH[2]} == "tem" ]]; then
      echo "distribution=temurin" >> "$GITHUB_OUTPUT"
    elif [[ ${BASH_REMATCH[2]} == "zulu" ]]; then
      echo "distribution=zulu" >> "$GITHUB_OUTPUT"
    else
      echo "Distribution ${BASH_REMATCH[2]} is not supported"
      exit 1
    fi
  fi
done < $1