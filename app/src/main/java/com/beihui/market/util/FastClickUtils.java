package com.beihui.market.util;


public class FastClickUtils {

    private static long lastClickTime;
    private static long lastPressTime;

    public static boolean isFastClick() {
        long last = lastClickTime;
        return (lastClickTime = System.currentTimeMillis()) - last < 1000;
    }

    public static boolean isFastBackPress() {
        long last = lastPressTime;
        return (lastPressTime = System.currentTimeMillis()) - last < 2500;
    }
}
