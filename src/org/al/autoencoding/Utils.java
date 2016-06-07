package org.al.autoencoding;

public class Utils {
    private static double sigmoid(double x) { // TODO: Pre-save several hundred values, and return estimate (see http://stackoverflow.com/q/2887815)
        return 1 / (1 + Math.exp(-x));
    }
}
