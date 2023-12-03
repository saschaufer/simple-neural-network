package de.saschaufer.ai.simple_neural_network.math;

import java.util.Random;

public class Matrix {

    private final double[][] elements;

    public Matrix(final int rows, final int columns) {

        if (rows == 0 || columns == 0) {
            throw new RuntimeException("The rows and columns must not be 0.");
        }

        elements = new double[rows][columns];

        // Fill the matrix with random numbers.
        for (int row = 0; row < elements.length; row++) {
            for (int col = 0; col < elements[0].length; col++) {
                // Random double between -0.5 and 0.5.
                elements[row][col] = -0.5 + new Random().nextDouble();
            }
        }
    }

    public Matrix(final double[][] elements) {

        if (elements.length == 0 || elements[0].length == 0) {
            throw new RuntimeException("The rows and columns must not be 0.");
        }

        this.elements = elements;
    }

    public Matrix add(final Matrix matrix) {

        if (elements.length != matrix.getElements().length || elements[0].length != matrix.getElements()[0].length) {
            throw new RuntimeException("The number of columns and rows of the two matrices must be equal.");
        }

        final double[][] result = new double[elements.length][elements[0].length];

        for (int row = 0; row < elements.length; row++) {
            for (int col = 0; col < elements[0].length; col++) {
                result[row][col] = elements[row][col] + matrix.getElements()[row][col];
            }
        }

        return new Matrix(result);
    }

    public Matrix multiply(final Matrix matrix) {

        if (elements[0].length != matrix.getElements().length) {
            throw new RuntimeException("The number of columns of the first matrix must be equal to the number of rows of the second matrix.");
        }

        final double[][] result = new double[elements.length][matrix.getElements()[0].length];

        for (int col2 = 0; col2 < matrix.getElements()[0].length; col2++) {
            for (int row1 = 0; row1 < elements.length; row1++) {

                double res = 0;
                for (int col1 = 0; col1 < elements[0].length; col1++) {
                    res += elements[row1][col1] * matrix.getElements()[col1][col2];
                    result[row1][col2] = res;
                }
            }
        }

        return new Matrix(result);
    }

    public Matrix multiply(final double scalar) {

        final double[][] result = new double[elements.length][elements[0].length];

        for (int row = 0; row < elements.length; row++) {
            for (int col = 0; col < elements[0].length; col++) {
                result[row][col] = scalar * elements[row][col];
            }
        }

        return new Matrix(result);
    }

    public Matrix transpose() {

        // Columns become rows and rows columns.
        final Matrix m = new Matrix(getElements()[0].length, getElements().length);

        for (int row = 0; row < getElements().length; row++) {
            for (int col = 0; col < getElements()[0].length; col++) {
                m.getElements()[col][row] = getElements()[row][col];
            }
        }

        return m;
    }

    public double[][] getElements() {
        return elements;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        for (final double[] element : elements) {
            for (int col = 0; col < elements[0].length; col++) {
                sb.append(String.format("%+1.20f", element[col])).append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
