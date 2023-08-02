#!/bin/bash

set -e 

./gradlew --warning-mode all openApiGenerate
rm -R src/openapi/*
mv generated_openapi/src/main/* src/openapi/
cp -R generated_openapi/src/test/java/* src/test/java/
mv generated_openapi/pom.xml src/openapi/
rm -R generated_openapi/

