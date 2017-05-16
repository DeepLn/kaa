#! /bin/bash
mvn -P compile-gwt,mongo-dao,mariadb-dao,build-rpm clean install verify -DskipTests
