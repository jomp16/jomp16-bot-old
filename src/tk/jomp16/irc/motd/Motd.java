/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.motd;

public class Motd {
    private static String motd;

    public static void setMotd(String motd) {
        Motd.motd = motd;
    }

    public static String getMotd() {
        return motd;
    }
}