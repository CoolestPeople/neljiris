package org.al.training;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class Utils {
    public static boolean shouldDisplayBoard() {
        AtomicInteger oneToTen = new AtomicInteger(ThreadLocalRandom.current().nextInt(1, 11));

        return oneToTen.get() == 1;
    }
}
