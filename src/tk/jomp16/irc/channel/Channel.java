/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.channel;

import java.util.ArrayList;
import java.util.HashMap;

public class Channel {
    private final String targetName;

    public Channel(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetName() {
        return targetName;
    }

    public HashMap<String, ChannelLevel> getAllUsersWithLevel() {
        return ChannelList.getListUsers(targetName);
    }

    public ArrayList<String> getAllUsers() {
        return new ArrayList<>(ChannelList.getListUsers(targetName).keySet());
    }

    public ArrayList<String> getAllOpUsers() {
        ArrayList<String> opUsers = new ArrayList<>();

        ChannelList.getListUsers(targetName).forEach((user, level) -> {
            if (level.equals(ChannelLevel.OP)) {
                opUsers.add(user);
            }
        });

        return opUsers;
    }

    public ArrayList<String> getAllVoiceUsers() {
        ArrayList<String> voiceUsers = new ArrayList<>();

        ChannelList.getListUsers(targetName).forEach((user, level) -> {
            if (level.equals(ChannelLevel.VOICE)) {
                voiceUsers.add(user);
            }
        });

        return voiceUsers;
    }

    public ArrayList<String> getAllNormalUsers() {
        ArrayList<String> normalUsers = new ArrayList<>();

        ChannelList.getListUsers(targetName).forEach((user, level) -> {
            if (level.equals(ChannelLevel.NORMAL)) {
                normalUsers.add(user);
            }
        });

        return normalUsers;
    }

    public String getChannelTopic() {
        return ChannelList.getChannelTopic(this.targetName);
    }
}
