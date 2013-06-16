/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.user;

import com.jomp16.irc.event.Level;

public class User {
    private String userName;
    private String hostName;
    private String hostMask;
    private Level level;

    public User(String userName, String hostName, String hostMask, Level level) {
        this.userName = userName;
        this.hostName = hostName;
        this.hostMask = hostMask;
        this.level = level;
    }

    public String getUserName() {
        return userName;
    }

    public String getHostName() {
        return hostName;
    }

    public String getHostMask() {
        return hostMask;
    }

    public String getCompleteHost() {
        return hostName + "@" + hostMask;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
