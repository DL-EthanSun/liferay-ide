#!/bin/sh

cd ..
mvn clean verify -P functional-repo
mvn clean verify -P functional-tests-mac
rm -rf ../tests-resources/bundles

exit 0