package com.doronkakuli.gps_tracking_example.utils;

/**
 * Created by Doron on 08/04/2015.
 */
public class Utils {


    public static String convertMilisToTime(long millis) {
        long second = (millis / 1000) % 60;
        long minute = (millis / (1000 * 60)) % 60;
        long hour = (millis / (1000 * 60 * 60)) % 24;
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }
}
