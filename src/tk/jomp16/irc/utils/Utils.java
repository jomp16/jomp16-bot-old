/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.utils;

import com.google.common.base.CharMatcher;

import java.util.ArrayList;

public class Utils {
    // I took it from PircBotX =P
    public static ArrayList<String> tokenizeLine(String input) {
        ArrayList<String> stringParts = new ArrayList<>();
        if (input == null || input.length() == 0) {
            return stringParts;
        }

        // Heavily optimized version string split by space with all characters after :
        // as a single entry. Under benchmarks, its faster than StringTokenizer,
        // String.split, toCharArray, and charAt
        String trimmedInput = CharMatcher.WHITESPACE.trimFrom(input);
        int pos = 0, end;
        while ((end = trimmedInput.indexOf(' ', pos)) >= 0) {
            stringParts.add(trimmedInput.substring(pos, end));
            pos = end + 1;
            if (trimmedInput.charAt(pos) == ':') {
                stringParts.add(trimmedInput.substring(pos + 1));
                return stringParts;
            }
        }
        // No more spaces, add last part of line
        stringParts.add(trimmedInput.substring(pos));
        return stringParts;
    }
}
