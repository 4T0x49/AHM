package com.AHM.utils;

public class MathHelper {

    public static double random(double min, double max) {
        return min + java.lang.Math.random() * (max - min);
    }
    public static double log(double v, double base) {
        return Math.log1p(v) / Math.log(base);
    }

}
