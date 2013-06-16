/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.channel;

import java.util.ArrayList;

public class Channel {
    private final String targetName;
    private final ArrayList<String> OPs;
    private final ArrayList<String> voices;
    private final ArrayList<String> normals;
    private final ArrayList<String> usersChannel;

    public Channel(String targetName, ArrayList<String> OPs, ArrayList<String> voices, ArrayList<String> normals, ArrayList<String> usersChannel) {
        this.targetName = targetName;
        this.OPs = OPs;
        this.voices = voices;
        this.normals = normals;
        this.usersChannel = usersChannel;
    }

    public String getTargetName() {
        return targetName;
    }

    public ArrayList<String> getOPs() {
        return OPs;
    }

    public ArrayList<String> getVoices() {
        return voices;
    }

    public ArrayList<String> getNormals() {
        return normals;
    }

    public ArrayList<String> getUsersChannel() {
        return usersChannel;
    }


}
