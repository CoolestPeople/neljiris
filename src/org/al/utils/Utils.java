package org.al.utils;

public class Utils {
    public static String getBetween(String what, String start, String end) {
        return what.substring(what.indexOf(start) + start.length(), what.indexOf(end));
    }
}
