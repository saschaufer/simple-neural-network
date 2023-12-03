package de.saschaufer.ai.simple_neural_network.net;

import de.saschaufer.ai.simple_neural_network.math.Math;
import de.saschaufer.ai.simple_neural_network.math.Matrix;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class Net {

    private final Matrix[] layers;
    private final Matrix[] weights;

    public Net(final Path path) throws IOException {
        weights = load(path);
        layers = new Matrix[weights.length + 1];

        for (int i = 0; i < layers.length; i++) {
            if (i == 0) {
                layers[i] = new Matrix(1, weights[i].getElements().length);
            } else {
                layers[i] = new Matrix(1, weights[i - 1].getElements()[0].length);
            }
        }
    }

    public Net(final int[] layerSizes) {

        layers = new Matrix[layerSizes.length];
        weights = new Matrix[layerSizes.length - 1];

        for (int i = 0; i < layerSizes.length; i++) {

            layers[i] = new Matrix(1, layerSizes[i]);

            if (i != 0) {
                weights[i - 1] = new Matrix(layerSizes[i - 1], layerSizes[i]);
            }
        }
    }

    public double[] run(final double[] input) {

        // Set input layer.
        layers[0].getElements()[0] = input;

        for (int i = 1; i < layers.length; i++) {

            // Multiply previous layer with weights to get this layer.
            layers[i] = layers[i - 1].multiply(weights[i - 1]);
            
            // Run this layers values through activation function.
            for (int j = 0; j < layers[i].getElements()[0].length; j++) {
                layers[i].getElements()[0][j] = Math.sigmoid(layers[i].getElements()[0][j]);
            }
        }

        // Get the values of the last layer as output of the net.
        return layers[layers.length - 1].getElements()[0];
    }

    public void backpropagate(final double[] target, final double learningRate) {

        // Calc error of output layer.
        Matrix error = new Matrix(layers[layers.length - 1].getElements()[0].length, 1);

        // Error function: target - actual
        for (int row = 0; row < error.getElements().length; row++) {
            error.getElements()[row][0] = target[row] - layers[layers.length - 1].getElements()[0][row];
        }

        // Go backwards through the net.
        for (int i = layers.length - 1; i > 0; i--) {

            // Multiply error with learning rate.
            error = error.multiply(learningRate);

            // Multiply differentiated sigmoid function (layer output * (1 - layer output)) with error.
            final Matrix l = layers[i];
            for (int j = 0; j < layers[i].getElements()[0].length; j++) {
                final double out = layers[i].getElements()[0][j];
                final double e = error.getElements()[j][0];
                l.getElements()[0][j] = out * (1 - out) * e;
            }

            // Multiply with previous layer output.
            final Matrix dWeights = layers[i - 1].transpose().multiply(l);

            // Calc error of previous layer for the next iteration.
            error = weights[i - 1].multiply(error);

            // Adjust weights.
            weights[i - 1] = weights[i - 1].add(dWeights);
        }
    }

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < layers.length; i++) {

            if (i != 0) {
                sb.append(weights[i - 1]).append("\n\n");
            }

            sb.append(layers[i]).append("\n\n");
        }

        return sb.toString();
    }

    public void save(final Path path) throws IOException {

        final StringBuilder sb = new StringBuilder();

        for (final Matrix weight : weights) {
            sb.append(weight.getElements().length).append(";").append(weight.getElements()[0].length).append(";");

            for (final double[] row : weight.getElements()) {
                for (double col : row) {
                    sb.append(col).append(";");
                }
            }
            sb.append("\n");
        }

        Files.writeString(path, sb.toString(), StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
    }

    private Matrix[] load(final Path path) throws IOException {

        final List<Matrix> w = new ArrayList<>();

        for (final String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {

            final String[] l = line.split(";");
            final int rows = Integer.parseInt(l[0]);
            final int cols = Integer.parseInt(l[1]);

            final double[][] weight = new double[rows][cols];

            int row = 0;
            int col = 0;
            for (int i = 2; i < l.length; i++) {
                weight[row][col] = Double.parseDouble(l[i]);
                col++;
                if (col == cols) {
                    row++;
                    col = 0;
                }
            }

            w.add(new Matrix(weight));
        }

        return w.toArray(new Matrix[0]);
    }
}
