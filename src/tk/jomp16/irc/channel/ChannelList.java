/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.channel;

import com.google.common.collect.HashMultimap;
import tk.jomp16.irc.event.events.ModeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChannelList {
    private static HashMultimap<String, ChannelUser> hashMapChannelUsers = HashMultimap.create();
    private static HashMap<String, String> channelTopic = new HashMap<>();

    public static void setTopic(String channel, String topic) {
        if (channelTopic.containsKey(channel)) {
            channelTopic.replace(channel, topic);
        } else {
            channelTopic.put(channel, topic);
        }
    }

    public static String getChannelTopic(String channel) {
        if (channelTopic.containsKey(channel)) {
            return channelTopic.get(channel);
        }

        return null;
    }

    public static void addUserToChannel(String channel, String user, ChannelLevel level) {
        ChannelUser channelUser = new ChannelUser(user, level);

        hashMapChannelUsers.put(channel, channelUser);
    }

    public static void removeUserToChannel(String channel, String user) {
        HashMultimap<String, ChannelUser> tmp = hashMapChannelUsers;
        ArrayList<ChannelUser> users = new ArrayList<>();

        for (String s : tmp.keySet()) {
            if (s.equals(channel)) {
                for (ChannelUser channelUser : hashMapChannelUsers.get(s)) {
                    if (!channelUser.getUser().equals(user)) {
                        users.add(channelUser);
                    }
                }
            }
        }

        hashMapChannelUsers.replaceValues(channel, users);
    }

    public static void removeUserFromAllChannel(String user) {
        HashMultimap<String, ChannelUser> tmp = hashMapChannelUsers;

        for (String s : tmp.keySet()) {
            ArrayList<ChannelUser> users = new ArrayList<>();

            for (ChannelUser channelUser : hashMapChannelUsers.get(s)) {
                if (!channelUser.getUser().equals(user)) {
                    users.add(channelUser);
                }
            }

            hashMapChannelUsers.replaceValues(s, users);
        }
    }

    public static void changeNick(String oldNick, String newNick) {
        HashMultimap<String, ChannelUser> tmp = hashMapChannelUsers;

        for (String s : tmp.keySet()) {
            ArrayList<ChannelUser> users = new ArrayList<>();

            for (ChannelUser channelUser : hashMapChannelUsers.get(s)) {
                if (channelUser.getUser().equals(oldNick)) {
                    channelUser.setUser(newNick);
                }

                users.add(channelUser);
            }

            hashMapChannelUsers.replaceValues(s, users);
        }
    }

    public static void changeUserLevel(String channel, String nick, ModeEvent.Modes mode) {
        HashMultimap<String, ChannelUser> tmp = hashMapChannelUsers;

        ArrayList<ChannelUser> users = new ArrayList<>();

        for (ChannelUser channelUser : tmp.get(channel)) {
            if (channelUser.getUser().equals(nick)) {
                ChannelLevel level = ChannelLevel.NORMAL;

                switch (mode) {
                    case VOICE:
                        level = ChannelLevel.VOICE;
                        break;
                    case OP:
                        level = ChannelLevel.OP;
                        break;
                }

                channelUser.setChannelLevel(level);
            }

            users.add(channelUser);
        }

        hashMapChannelUsers.replaceValues(channel, users);
    }

    public static HashMap<String, ChannelLevel> getListUsers(String channel) {
        HashMap<String, ChannelLevel> tmp = new HashMap<>();

        for (Map.Entry<String, ChannelUser> entry : hashMapChannelUsers.entries()) {
            if (entry.getKey().equals(channel)) {
                ChannelUser channelUser = entry.getValue();
                tmp.put(channelUser.getUser(), channelUser.getChannelLevel());
            }
        }

        return tmp;
    }
}