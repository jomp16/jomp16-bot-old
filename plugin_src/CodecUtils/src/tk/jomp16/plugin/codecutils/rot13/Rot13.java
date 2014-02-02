// Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
// This work is free. You can redistribute it and/or modify it under the
// terms of the Do What The Fuck You Want To Public License, Version 2,
// as published by Sam Hocevar. See the COPYING file for more details.

package tk.jomp16.plugin.codecutils.rot13;

public class Rot13 {
    public static String rotate(String string) {
        // Original source: http://introcs.cs.princeton.edu/java/31datatype/Rot13.java.html
        // modified by jomp16 to fit my needs

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);

            if (c >= 'a' && c <= 'm') {
                c += 13;
            } else if (c >= 'A' && c <= 'M') {
                c += 13;
            } else if (c >= 'n' && c <= 'z') {
                c -= 13;
            } else if (c >= 'N' && c <= 'Z') {
                c -= 13;
            }

            builder.append(c);
        }

        return builder.toString();
    }
}
