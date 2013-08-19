/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.channel;

import java.util.HashMap;

public class Channel {
    private final String targetName;

    public Channel(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetName() {
        return targetName;
    }

    public HashMap<String, ChannelLevel> getUsers() {
        return ChannelList.getListUsers(targetName);
    }

    public String getChannelTopic() {
        return ChannelList.getChannelTopic(this.targetName);
    }
}
