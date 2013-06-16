/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.utils;

public class Utils {
    public static boolean empty(String string) {
        return string == null || string.length() == 0;
    }

    /**
     * Split a string until a certain string occurs
     */
    public static String[] splitUntil(String source, String split, String until) {
        return splitUntilOccurrence(source, split, until, 1);
    }

    /**
     * Split a string until a certain string occurs a certain amount of times
     */
    public static String[] splitUntilOccurrence(String source, String split, String until, int occurrence) {
        int untilIndex = -until.length();
        for (int i = 0; i < occurrence; i++) {
            untilIndex = source.indexOf(until, untilIndex + until.length());
        }
        if (untilIndex <= 0 || untilIndex >= source.length()) {
            return source.split(split);
        }
        String toSplit = source.substring(0, untilIndex);
        String remainder = source.substring(untilIndex + until.length());
        String[] splitted = toSplit.split(split);

        String[] out = new String[splitted.length + 1];

        System.arraycopy(splitted, 0, out, 0, splitted.length);
        out[out.length - 1] = remainder;

        return out;
    }

    /**
     * Split a string until a certain string occurs a certain amount of times after another certain string occurs a certain amount of times
     */
    public static String[] splitUntilOccurenceAfterOccurence(String source, String split, String until, int occurrence, String after, int afterOccurrence) {
        if (occurrence <= 0) {
            return source.split(split);
        }
        int afterIndex = -after.length();
        for (int i = 0; i < afterOccurrence; i++) {
            afterIndex = source.indexOf(after, afterIndex + after.length());
        }
        if (afterIndex <= 0 || afterIndex >= source.length()) {
            return source.split(split);
        }
        int occurenceCountUntilAfter = 0;
        int untilIndex = source.indexOf(until);
        while (untilIndex != -1 && untilIndex < afterIndex + after.length()) {
            untilIndex = source.indexOf(until, untilIndex + until.length());
            occurenceCountUntilAfter++;
        }
        return splitUntilOccurrence(source, split, until, occurenceCountUntilAfter + occurrence);
    }

    public static <T> T indexOrDefault(T[] items, int index, T def) {
        if (index >= 0 && index < items.length) {
            return items[index];
        }
        return def;
    }
}
