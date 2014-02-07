/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Channel {
    private final String targetName;

    public Channel(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetName() {
        return targetName;
    }

    public Map<String, ChannelLevel> getAllUsersWithLevel() {
        return ChannelList.getListUsers(targetName);
    }

    public List<String> getAllUsers() {
        return new ArrayList<>(ChannelList.getListUsers(targetName).keySet());
    }

    public List<String> getAllOpUsers() {
        List<String> opUsers = new ArrayList<>();

        ChannelList.getListUsers(targetName).forEach((user, level) -> {
            if (level.equals(ChannelLevel.OP)) {
                opUsers.add(user);
            }
        });

        return opUsers;
    }

    public List<String> getAllVoiceUsers() {
        List<String> voiceUsers = new ArrayList<>();

        ChannelList.getListUsers(targetName).forEach((user, level) -> {
            if (level.equals(ChannelLevel.VOICE)) {
                voiceUsers.add(user);
            }
        });

        return voiceUsers;
    }

    public List<String> getAllNormalUsers() {
        List<String> normalUsers = new ArrayList<>();

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
