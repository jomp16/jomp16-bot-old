/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.configuration;

public class Configuration {
    private String nick;
    private String realName;
    private String password;
    private String identify;
    private String server;
    private String prefix;
    private int port;
    private int commandLock;
    private boolean verbose;
    private Builder builder;

    public Configuration(Builder builder) {
        this.nick = builder.getNick();
        this.realName = builder.getRealName();
        this.password = builder.getPassword();
        this.server = builder.getServer();
        this.identify = builder.getIdentify();
        this.prefix = builder.getPrefix();
        this.port = builder.getPort();
        this.commandLock = builder.getCommandLock();
        this.verbose = builder.isVerbose();
        this.builder = builder;
    }

    public String getNick() {
        return nick;
    }

    public String getRealName() {
        return realName;
    }

    public String getPassword() {
        return password;
    }

    public String getIdentify() {
        return identify;
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

    public int getCommandLock() {
        return commandLock;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public Builder getBuilder() {
        return builder;
    }

    public static class Builder {
        private String nick = "jomp16-bot";
        private String realName = "jomp16-bot";
        private String password = null;
        private String server = "irc.freenode.org";
        private String identify = "jomp16-bot";
        private String prefix = "!";
        private int port = 6667;
        private int commandLock = 5;
        private boolean verbose = false;

        public Builder() {

        }

        public Builder(Builder builder) {
            this.nick = builder.getNick();
            this.realName = builder.getRealName();
            this.password = builder.getPassword();
            this.server = builder.getServer();
            this.identify = builder.getIdentify();
            this.prefix = builder.getPrefix();
            this.port = builder.getPort();
            this.commandLock = builder.getCommandLock();
            this.verbose = builder.isVerbose();
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

        public String getPassword() {
            return password;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public String getServer() {
            return server;
        }

        public Builder setServer(String server) {
            this.server = server;
            return this;
        }

        public Builder setServer(String server, int port) {
            this.server = server;
            this.port = port;
            return this;
        }

        public int getCommandLock() {
            return commandLock;
        }

        public Builder setCommandLock(int commandLock) {
            this.commandLock = commandLock;
            return this;
        }

        public String getIdentify() {
            return identify;
        }

        public Builder setIdentify(String identify) {
            this.identify = identify;
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
