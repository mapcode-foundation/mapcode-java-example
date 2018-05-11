# Example Project for Mapcode Library for Java

[![Build Status](https://img.shields.io/travis/mapcode-foundation/mapcode-java-example.svg?maxAge=3600&branch=master)](https://travis-ci.org/mapcode-foundation/mapcode-java-example)
[![License](http://img.shields.io/badge/license-APACHE2-blue.svg)]()

**Copyright (C) 2014-2017 Stichting Mapcode Foundation (http://www.mapcode.com)**

This Java project contains example code of how to use the Mapcode
Library.

First, make sure you have the correct file encoding (UTF8) set for Java on your system.
Include this environment variable in your `.profile` or `.bashrc`:

    export JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF8"

To run the examples, simply execute:

    mvn clean install
    mvn exec:java -Dexec.mainClass="com.mapcode.example.Example"

Don't forget to make sure you have the Mapcode JAR in your Maven
repository, for example by building it locally.

For more information, see: http://www.mapcode.com.

## License

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Original C version created by Pieter Geelen. Work on Java version
of the Mapcode library by Rijn Buve and Matthew Lowden.
