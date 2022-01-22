#!/bin/bash

lein pom &&  mvn -X  clean compile  clojure:compile package