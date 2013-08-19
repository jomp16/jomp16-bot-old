/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.channel;

public class ChannelUser {
    private String user;
    private ChannelLevel channelLevel;

    public ChannelUser(String user, ChannelLevel level) {
        this.user = user;
        this.channelLevel = level;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public ChannelLevel getChannelLevel() {
        return channelLevel;
    }

    public void setChannelLevel(ChannelLevel channelLevel) {
        this.channelLevel = channelLevel;
    }
}
