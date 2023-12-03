package de.saschaufer.ai.simple_neural_network.mnist;

import de.saschaufer.ai.simple_neural_network.net.Net;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MNIST {

    private static final Logger log = LoggerFactory.getLogger(MNIST.class);

    public void train(final Net net, final Path path, final double learningRate) {

        try (final Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8)) {
            stream
                    // Parse a line of the data set.
                    .map(this::toNumber)
                    // Feed the trainings data to the net and backpropagate the error.
                    .forEach(number -> train(net, number, learningRate))
            ;
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void test(final Net net, final Path path) {

        final Map<Boolean, Integer> result;

        try (final Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8)) {
            result = stream
                    // Parse a line of the data set.
                    .map(this::toNumber)
                    // Feed the test data to the net
                    // and check whether the number was recognized correctly.
                    .map(number -> run(net, number))
                    .collect(Collectors.groupingBy(ok -> ok, Collectors.summingInt(value -> 1)))
            ;
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        final int ok = result.get(true);
        final int fail = result.get(false);

        final double successRate = (double) ok / (ok + fail) * 100d;

        log.atInfo().setMessage(String.format("Success rate: %d of %d (%.2f%%) correct.", ok, (ok + fail), successRate)).log();
    }

    private Number toNumber(final String line) {

        final String[] strings = line.split(",");

        int label = 0;
        final double[] pixels = new double[strings.length - 1];

        for (int i = 0; i < strings.length; i++) {
            final int n = Integer.parseInt(strings[i]);
            if (i == 0) {
                label = n;
            } else {
                pixels[i - 1] = n;
            }
        }

        return new Number(label, pixels);
    }

    private boolean run(final Net net, final Number number) {

        // Feed the MNIST data through the net.
        final double[] output = net.run(number.pixels());

        // Get the index of the highest value in the output.
        // This is the identified number.
        int n = -1;
        double highestOutput = -1d;
        for (int i = 0; i < output.length; i++) {
            if (highestOutput < output[i]) {
                highestOutput = output[i];
                n = i;
            }
        }

        // Is the identified number correct?
        final boolean correct = number.label() == n;

        log.atInfo().setMessage(String.format("Test. Target: %d Actual: %d Correct: %s.", number.label(), n, correct ? "y" : "n")).log();

        return correct;
    }

    private void train(final Net net, final Number number, final double learningRate) {

        // Feed the MNIST data through the net.
        final double[] output = net.run(number.pixels());

        // Build the target output and calc the error.
        final double[] target = new double[output.length];

        double error = 0d;

        for (int i = 0; i < target.length; i++) {

            // The label is equal to the index of the output layer.
            // On this index the output should be maximized and on
            // the other indices minimized.
            if (i == number.label()) {
                target[i] = 1d;
            } else {
                target[i] = 0d;
            }

            // Add up the errors to show how far the overall output is from the target.
            final double e = target[i] - output[i];
            error += e < 0 ? -e : e;
        }

        log.atInfo().setMessage(String.format("Train. Target: %d Error: %.2f.", number.label(), error))
                .addKeyValue("target", format(target, false))
                .addKeyValue("actual", format(output, true))
                .log();

        // Propagate the error back through the net.
        net.backpropagate(target, learningRate);
    }

    private String format(final double[] doubles, final boolean decimal) {

        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < doubles.length; i++) {
            if (decimal) {
                sb.append(String.format("%.2f", doubles[i]));
            } else {
                sb.append(String.format("%.0f", doubles[i]));
            }

            if (i < doubles.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");

        return sb.toString();
    }
}
