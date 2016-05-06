package org.al.training;

import org.al.etc.Constants;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class Utils {
    public static boolean shouldDisplayBoard() {
        AtomicInteger oneToTen = new AtomicInteger(ThreadLocalRandom.current().nextInt(1, Constants.TRAIN_MOVES + 1));

        return oneToTen.get() == 1;
    }
}
