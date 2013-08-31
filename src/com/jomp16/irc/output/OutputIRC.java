/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.output;

import com.jomp16.configuration.Configuration;
import com.jomp16.irc.IRCManager;

public class OutputIRC {
    private final IRCManager ircManager;

    public OutputIRC(IRCManager ircManager) {
        this.ircManager = ircManager;
    }

    public void joinChannel(Object channel) {
        ircManager.getOutputRaw().writeRaw("JOIN " + channel);
    }

    public void partChannel(Object channel) {
        ircManager.getOutputRaw().writeRaw("PART " + channel);
    }

    public void partChannel(Object channel, Object reason) {
        ircManager.getOutputRaw().writeRaw("PART " + channel + " :" + reason);
    }

    public void sendMessage(Object target, Object message) {
        ircManager.getOutputRaw().writeRaw("PRIVMSG " + target + " :" + message);
    }

    public void sendMessage(Object target, Object username, Object message) {
        ircManager.getOutputRaw().writeRaw("PRIVMSG " + target + " :" + username + ": " + message);
    }

    public void changeNick(String nick) {
        Configuration.Builder builder = ircManager.getConfiguration().getBuilder();
        builder.setNick(nick);

        ircManager.setConfiguration(builder.buildConfiguration());
        ircManager.getOutputRaw().writeRaw("NICK " + nick);
    }
}
