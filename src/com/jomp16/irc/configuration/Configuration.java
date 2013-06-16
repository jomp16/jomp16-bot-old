/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.configuration;

public class Configuration {
    private String nick;
    private String realName;
    private String hostMask;
    private String server;
    private String prefix;
    private int port;
    private boolean verbose;

    public Configuration(Builder builder) {
        this.nick = builder.getNick();
        this.realName = builder.getRealName();
        this.server = builder.getServer();
        this.hostMask = builder.getHostMask();
        this.prefix = builder.getPrefix();
        this.port = builder.getPort();
        this.verbose = builder.isVerbose();
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getRealName() {
        return realName;
    }

    public String getHostMask() {
        return hostMask;
    }

    public String getServer() {
        return server;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getPort() {
        return port;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public static class Builder {
        private String nick = "jomp16-bot";
        private String realName = "jomp16-bot";
        private String server = "irc.freenode.org";
        private String hostMask = "jomp16-bot";
        private String prefix = "!";
        private int port = 6667;
        private boolean verbose = false;

        public Builder setServer(String server, int port) {
            this.server = server;
            this.port = port;
            return this;
        }

        public Configuration buildConfiguration() {
            return new Configuration(this);
        }

        public String getNick() {
            return nick;
        }

        public Builder setNick(String nick) {
            this.nick = nick;
            return this;
        }

        public String getRealName() {
            return realName;
        }

        public Builder setRealName(String realName) {
            this.realName = realName;
            return this;
        }

        public String getServer() {
            return server;
        }

        public Builder setServer(String server) {
            this.server = server;
            return this;
        }

        public String getHostMask() {
            return hostMask;
        }

        public Builder setHostMask(String hostMask) {
            this.hostMask = hostMask;
            return this;
        }

        public String getPrefix() {
            return prefix;
        }

        public Builder setPrefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public int getPort() {
            return port;
        }

        public boolean isVerbose() {
            return verbose;
        }

        public Builder setVerbose(boolean verbose) {
            this.verbose = verbose;
            return this;
        }
    }
}
