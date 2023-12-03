package de.saschaufer.ai.simple_neural_network.math;

import static java.lang.Math.pow;

public class Math {

    private static final double E = java.lang.Math.E;

    private Math() {
    }

    public static double sigmoid(final double x) {
        // 1 / (1 + e^-x)
        return (1 / (1 + pow(E, -x)));
    }
}
