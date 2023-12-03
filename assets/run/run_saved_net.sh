#!/bin/bash

JAR=../../target/simple-neural-network-1.0.0-SNAPSHOT.jar
TEST_DATA="../mnist/mnist_test.csv"
SAVE="net_save"

# Test
java -jar $JAR --load $SAVE --test $TEST_DATA
