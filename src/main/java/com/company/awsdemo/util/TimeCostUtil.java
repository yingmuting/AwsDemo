package com.company.awsdemo.util;

public class TimeCostUtil {

    public static long start() {
        return System.currentTimeMillis();
    }

    public static void printCost(long start) {
        System.out.println("it consumes " + (System.currentTimeMillis() - start) + "ms");
    }

}
