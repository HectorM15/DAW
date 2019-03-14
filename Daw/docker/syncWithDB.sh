#!/bin/sh

echo "Ready..."
while ! nc -z -v daw_grupo9 3306
do
  echo "Steady..."
  sleep 9
done
    echo "Go!"
java -jar target/daw-0.0.1-SNAPSHOT.jar
