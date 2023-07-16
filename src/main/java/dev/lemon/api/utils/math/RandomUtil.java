package dev.lemon.api.utils.math;

import java.util.Random;

public class RandomUtil {
    public static int getRandomIntInRange(int min, int max){
        if (min == max)
            return max;
        return new Random().ints(min, max).findFirst().getAsInt();
    }

    public static double getRandomDoubleInRange(double min, double max){
        if (min == max)
            return max;
        return new Random().doubles(min, max).findFirst().getAsDouble();
    }
}
