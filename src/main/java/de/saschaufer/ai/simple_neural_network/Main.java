package de.saschaufer.ai.simple_neural_network;

import de.saschaufer.ai.simple_neural_network.args.Args;
import de.saschaufer.ai.simple_neural_network.mnist.MNIST;
import de.saschaufer.ai.simple_neural_network.net.Net;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(final String[] args) {

        log.atInfo().setMessage("Run neural net.").addKeyValue("args", List.of(args)).log();

        final Args arguments = new Args(args);

        if (!arguments.isOk()) {
            return;
        }

        if (arguments.getTrainDataSet() != null && arguments.getLearningRate() != null && arguments.getSaveFile() != null) {

            final Net net;

            if (arguments.getLayer() != null) {
                log.info("Create a new net, train and save it.");

                net = new Net(arguments.getLayer());
            } else if (arguments.getLoadFile() != null) {
                log.info("Load a net, train and save it.");

                try {
                    net = new Net(arguments.getLoadFile());
                } catch (IOException e) {
                    log.error("Error loading net.", e);
                    return;
                }
            } else {
                return;
            }

            final MNIST mnist = new MNIST();
            mnist.train(net, arguments.getTrainDataSet(), arguments.getLearningRate());

            try {
                net.save(arguments.getSaveFile());
            } catch (final IOException e) {
                log.error("Error saving net.", e);
            }

            log.info("Net trained.");

        } else if (arguments.getTestDataSet() != null) {

            final Net net;

            if (arguments.getLayer() != null) {
                log.info("Create a new net and test it.");

                net = new Net(arguments.getLayer());

            } else if (arguments.getLoadFile() != null) {
                log.info("Load a net and test it.");

                try {
                    net = new Net(arguments.getLoadFile());
                } catch (IOException e) {
                    log.error("Error loading net.", e);
                    return;
                }

            } else {
                return;
            }

            final MNIST mnist = new MNIST();
            mnist.test(net, arguments.getTestDataSet());

            log.info("Net tested.");
        }
    }
}
