/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc;

public class Source {
    private String raw;
    private String nick;
    private String user;
    private String host;

    public Source(String raw) {
        this.raw = raw;
        if (!raw.contains("@")) {
            this.host = raw;
        } else {
            if (raw.contains("!")) {
                this.nick = raw.substring(0, raw.indexOf('!'));
                this.user = raw.substring(raw.indexOf('!') + 1, raw.indexOf('@'));
                this.host = raw.substring(raw.indexOf('@') + 1);
            } else {
                this.nick = raw.substring(0, raw.indexOf('@'));
                this.host = raw.substring(raw.indexOf('@') + 1);
            }
        }
    }

    public Source(String nick, String user, String host) {
        this.nick = nick;
        this.user = user;
        this.host = host;
        this.raw = nick + "!" + user + "@" + host;
    }

    public static boolean match(String host, String mask) {
        String[] sections = mask.split("\\*");
        for (String section : sections) {
            int index = host.indexOf(section);
            if (index == -1) {
                return false;
            }
            host = host.substring(index + section.length());
        }
        return true;
    }

    public boolean match(String mask) {
        return match(host, mask);
    }

    public String getRaw() {
        return raw;
    }

    public String getNick() {
        return nick;
    }

    public String getUser() {
        return user;
    }

    public String getHost() {
        return host;
    }

    @Override
    public String toString() {
        return "Source{" +
                "raw='" + raw + '\'' +
                ", nick='" + nick + '\'' +
                ", user='" + user + '\'' +
                ", host='" + host + '\'' +
                '}';
    }
}
