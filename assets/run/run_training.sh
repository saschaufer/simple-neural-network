#!/bin/bash

JAR=../../target/simple-neural-network-1.0.0-SNAPSHOT.jar
LEARNING_RATE=0.01
TRAINING_DATA="../mnist/mnist_train.csv"
TEST_DATA="../mnist/mnist_test.csv"
SAVE="net_save_training"
SAVE_1="net_save_1"
SAVE_2="net_save_2"

rm $SAVE
rm $SAVE_1
rm $SAVE_2

# Epoch 1
java -jar $JAR --layer "784,200,10" --train $TRAINING_DATA --lr $LEARNING_RATE --save $SAVE_1

# Epoch 2
java -jar $JAR --load $SAVE_1 --train $TRAINING_DATA --lr $LEARNING_RATE --save $SAVE_2
rm $SAVE_1

# Epoch 3
java -jar $JAR --load $SAVE_2 --train $TRAINING_DATA --lr $LEARNING_RATE --save $SAVE_1
rm $SAVE_2

# Epoch 4
java -jar $JAR --load $SAVE_1 --train $TRAINING_DATA --lr $LEARNING_RATE --save $SAVE_2
rm $SAVE_1

# Epoch 5
java -jar $JAR --load $SAVE_2 --train $TRAINING_DATA --lr $LEARNING_RATE --save $SAVE_1
rm $SAVE_2

# Epoch 6
java -jar $JAR --load $SAVE_1 --train $TRAINING_DATA --lr $LEARNING_RATE --save $SAVE_2
rm $SAVE_1

# Epoch 7
java -jar $JAR --load $SAVE_2 --train $TRAINING_DATA --lr $LEARNING_RATE --save $SAVE_1
rm $SAVE_2

# Epoch 8
java -jar $JAR --load $SAVE_1 --train $TRAINING_DATA --lr $LEARNING_RATE --save $SAVE_2
rm $SAVE_1

# Epoch 9
java -jar $JAR --load $SAVE_2 --train $TRAINING_DATA --lr $LEARNING_RATE --save $SAVE_1
rm $SAVE_2

# Epoch 10
java -jar $JAR --load $SAVE_1 --train $TRAINING_DATA --lr $LEARNING_RATE --save $SAVE
rm $SAVE_1

# Test
java -jar $JAR --load $SAVE --test $TEST_DATA
