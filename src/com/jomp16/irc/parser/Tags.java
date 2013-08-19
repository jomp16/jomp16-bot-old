/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.parser;

public enum Tags {
    COMMAND_PING("PING"),
    COMMAND_PONG("PONG"),
    COMMAND_PRIVMSG("PRIVMSG"),
    COMMAND_NOTICE("NOTICE"),
    COMMAND_JOIN("JOIN"),
    COMMAND_PART("PART"),
    COMMAND_QUIT("QUIT"),
    COMMAND_TOPIC("TOPIC"),
    COMMAND_INVITE("INVITE"),
    COMMAND_NICK("NICK"),
    COMMAND_KICK("KICK"),
    COMMAND_MODE("MODE"),

    RESPONSE_SERVER_CONNECTED("001"),
    RESPONSE_SERVER_INFO("004"),
    RESPONSE_USER_HOST("302"),
    RESPONSE_MOTD_START("375"),
    RESPONSE_MOTD_CONTENT("372"),
    RESPONSE_MOTD_END("376"),
    RESPONSE_INFO_START("373"),
    RESPONSE_INFO_CONTENT("371"),
    RESPONSE_INFO_END("374"),
    RESPONSE_TOPIC_NONE("331"),
    RESPONSE_TOPIC_MESSAGE("332"),
    RESPONSE_TOPIC_SETBY("333"),
    RESPONSE_NAMES_LIST("353"),
    RESPONSE_NAMES_END("366"),
    RESPONSE_CHANNEL_URL("328"),

    ERROR_NO_SUCH_NICK("401"),
    ERROR_NO_SUCH_SERVER("402"),
    ERROR_NO_SUCH_CHANNEL("403"),
    ERROR_CANNOT_SEND_TO_CHANNEL("404"),
    ERROR_TOO_MANY_CHANNELS("405"),
    ERROR_NO_ORIGIN("409"),
    ERROR_NO_RECIPIENT("411"),
    ERROR_NO_TEXT_TO_SEND("412"),
    ERROR_NO_MOTD("422"),
    ERROR_NO_NICK_GIVEN("431"),
    ERROR_NICK_INVALID("432"),
    ERROR_NICK_IN_USE("433"),
    ERROR_NOT_ON_CHANNEL("442"),
    ERROR_WRONG_PASSWORD("464");
    private String command;

    private Tags(String command) {
        this.command = command;
    }

    public static Tags getTag(String tag) {
        for (Tags tags : Tags.values()) {
            if (tags.command.equals(tag)) {
                return tags;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return command;
    }
}
