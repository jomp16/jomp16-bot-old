/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.event.listener;

import org.apache.logging.log4j.Logger;
import tk.jomp16.irc.IRCManager;
import tk.jomp16.irc.channel.Channel;
import tk.jomp16.irc.channel.ChannelDAO;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.user.User;
import tk.jomp16.language.LanguageManager;
import tk.jomp16.plugin.PluginInfo;
import tk.jomp16.plugin.help.HelpRegister;
import tk.jomp16.properties.JSONProperties;

import java.io.File;
import java.net.URLDecoder;

public abstract class Listener {
    protected IRCManager ircManager;
    protected User user;
    protected Channel channel;
    protected ChannelDAO channelDAO;
    protected Logger log;
    protected PluginInfo pluginInfo;

    public Listener(IRCManager ircManager, Logger log) {
        this.ircManager = ircManager;
        this.log = log;
    }

    public Listener(IRCManager ircManager, User user, Channel channel, ChannelDAO channelDAO, Logger log, PluginInfo pluginInfo) {
        this.ircManager = ircManager;
        this.user = user;
        this.channel = channel;
        this.channelDAO = channelDAO;
        this.log = log;
        this.pluginInfo = pluginInfo;
    }

    public Listener(IRCManager ircManager, User user, Channel channel, ChannelDAO channelDAO, Logger log) {
        this.ircManager = ircManager;
        this.user = user;
        this.channel = channel;
        this.channelDAO = channelDAO;
        this.log = log;
    }

    public synchronized void respond(Object message) {
        ircManager.getOutputIRC().sendMessage(channel.getTargetName(), user.getUserName(), message);

        try {
            wait(700);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    public synchronized void respond(Object message, boolean showName) {
        if (showName) {
            respond(message);
        } else {
            ircManager.getOutputIRC().sendMessage(channel.getTargetName(), message);
        }

        try {
            wait(700);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    public synchronized void respond(Object target, Object message) {
        ircManager.getOutputIRC().sendMessage(target, message);

        try {
            wait(700);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    public synchronized void respond(Object target, Object user, Object message) {
        ircManager.getOutputIRC().sendMessage(target, user, message);

        try {
            wait(700);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    public synchronized void respond(String user, Object message) {
        ircManager.getOutputIRC().sendMessage(channel.getTargetName(), user, message);

        try {
            wait(700);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    public void addHelp(Event event, HelpRegister helpRegister) {
        event.registerHelp(helpRegister);
    }

    public LanguageManager getLanguageManager(Event event, String resourcePath) {
        return new LanguageManager(resourcePath, event.getClass().getClassLoader());
    }

    public String getPluginPath(Event event) {
        File file = new File("plugins/" + event.getClass().getSimpleName());

        if (!file.exists()) {
            file.mkdir();
        }

        return file.getAbsolutePath();
    }

    public String getPluginPath() {
        if (pluginInfo != null) {
            File f = new File("plugins/" + pluginInfo.getName());

            if (!f.exists()) {
                f.mkdir();
            }

            return f.getPath();
        }

        throw new UnsupportedOperationException("PluginInfo is null!");
    }

    public String getPluginJarPath(Event event) {
        try {
            return URLDecoder.decode(event.getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
        } catch (Exception e) {
            return null;
        }
    }

    public JSONProperties getJsonProperties(Event event, String baseResource) {
        return new JSONProperties(event.getClass().getClassLoader()).load(baseResource);
    }

    public IRCManager getIrcManager() {
        return ircManager;
    }

    public User getUser() {
        return user;
    }

    public Channel getChannel() {
        return channel;
    }

    public Logger getLog() {
        return log;
    }

    public PluginInfo getPluginInfo() {
        return pluginInfo;
    }

    public ChannelDAO getChannelDAO() {
        return channelDAO;
    }
}
