package de.saschaufer.ai.simple_neural_network.args;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Args {

    private static final Logger log = LoggerFactory.getLogger(Args.class);

    private final Path trainDataSet;
    private final Path testDataSet;
    private final Path saveFile;
    private final Path loadFile;
    private final int[] layer;
    private final Double learningRate;
    private final boolean ok;

    private Args() {
        this(null);
    }

    public Args(final String[] args) {

        final Map<String, String> a = readArgs(args);

        ok = a != null;
        if (!ok) {
            trainDataSet = null;
            testDataSet = null;
            saveFile = null;
            loadFile = null;
            layer = null;
            learningRate = null;
            return;
        }

        trainDataSet = a.containsKey("train") ? Path.of(a.get("train")) : null;
        testDataSet = a.containsKey("test") ? Path.of(a.get("test")) : null;
        saveFile = a.containsKey("save") ? Path.of(a.get("save")) : null;
        loadFile = a.containsKey("load") ? Path.of(a.get("load")) : null;
        learningRate = a.containsKey("lr") ? Double.parseDouble(a.get("lr")) : null;

        if (a.containsKey("layer")) {
            final String[] layerSizes = a.get("layer").split(",");
            layer = new int[layerSizes.length];
            for (int i = 0; i < layerSizes.length; i++) {
                layer[i] = Integer.parseInt(layerSizes[i]);
            }
        } else {
            layer = null;
        }
    }

    private Map<String, String> readArgs(final String[] args) {

        if (args == null || args.length == 0) {
            printHelp("Args required.");
            return null;
        }

        final Map<String, String> a = new HashMap<>();

        for (int i = 0; i < args.length; i += 2) {
            switch (args[i]) {
                case "--help":
                    printHelp(null);
                    return null;
                case "--train":
                    a.put("train", args[i + 1]);
                    break;
                case "--test":
                    a.put("test", args[i + 1]);
                    break;
                case "--layer":
                    a.put("layer", args[i + 1]);
                    break;
                case "--save":
                    a.put("save", args[i + 1]);
                    break;
                case "--load":
                    a.put("load", args[i + 1]);
                    break;
                case "--lr":
                    a.put("lr", args[i + 1]);
                    break;
                default:
                    printHelp(String.format("Unknown argument '%s'.", args[i]));
                    return null;
            }
        }

        if (a.containsKey("layer") && a.containsKey("train") && a.containsKey("lr") && a.containsKey("save")) {
            return a;
        }

        if (a.containsKey("load") && a.containsKey("train") && a.containsKey("lr") && a.containsKey("save")) {
            return a;
        }

        if (a.containsKey("layer") && a.containsKey("test")) {
            return a;
        }

        if (a.containsKey("load") && a.containsKey("test")) {
            return a;
        }

        printHelp("Unknown pairs of arguments.");

        return null;
    }

    private void printHelp(final String error) {

        if (error != null) {
            log.atError().setMessage(error).log();
        }

        log.info("""
                                
                Arguments:
                --help   Print this help menu.
                --train  Train the net with the training data set.
                         Example: --train /path/mnist_train.csv
                --test   Test the net with the test data set.
                         Example: --test /path/mnist_test.csv
                --layer  To create a new net, specify the layer sizes.
                         For the MNIST data sets the first layer has to
                         have 784 and the last layer 10 elements.
                         Example: --layer 784,2,5,3,10
                --save   Save the net in a file. The file must not exist.
                         Example: --save /path/file
                --load   Load the net from a file.
                         Example: --load /path/file
                --lr     The learning rate.
                         Example: 0.01
                                
                The arguments must come on pairs:
                --layer --train --lr --save   Create a new net, train and save it.
                --load  --train --lr --save   Load a net, train and save it.
                --layer --test                Create a new net and test it.
                --load  --test                Load a net and test it.
                """);
    }

    public Path getTrainDataSet() {
        return trainDataSet;
    }

    public Path getTestDataSet() {
        return testDataSet;
    }

    public Path getSaveFile() {
        return saveFile;
    }

    public Path getLoadFile() {
        return loadFile;
    }

    public int[] getLayer() {
        return layer;
    }

    public Double getLearningRate() {
        return learningRate;
    }

    public boolean isOk() {
        return ok;
    }
}
