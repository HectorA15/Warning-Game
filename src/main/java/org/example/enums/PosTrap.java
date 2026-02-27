package org.example.enums;

import java.util.Random;

public enum PosTrap {

    LEFT_TOP, LEFT_BOTTOM, LEFT_CENTER,
    RIGHT_TOP, RIGHT_BOTTOM, RIGHT_CENTER,
    BOTTOM_LEFT, BOTTOM_RIGHT, BOTTOM_CENTER,
    TOP_LEFT, TOP_RIGHT, TOP_CENTER;


    private static final Random random = new Random();
    private static final PosTrap[] VALUES = values();
    private static final int SIZE = VALUES.length;

    public static PosTrap getRandomPos() {
        return VALUES[random.nextInt(SIZE)];
    }

}
