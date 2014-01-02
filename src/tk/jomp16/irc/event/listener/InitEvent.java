/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.event.listener;

import tk.jomp16.irc.IRCManager;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.plugin.help.HelpRegister;
import tk.jomp16.logger.Logger;
import tk.jomp16.sqlite.SQLiteManager;

import java.io.File;
import java.net.URLDecoder;

public class InitEvent {
    private IRCManager ircManager;
    private Logger log;

    public InitEvent(IRCManager ircManager, Logger log) {
        this.ircManager = ircManager;
        this.log = log;
    }

    public IRCManager getIrcManager() {
        return ircManager;
    }

    public Logger getLog() {
        return log;
    }

    public void addHelp(Event event, HelpRegister helpRegister) {
        event.registerHelp(helpRegister);
    }

    /**
     * Get the plugin path
     *
     * @param event the event of the plugin
     * @return the full path, including the plugin jar
     */
    public String getFullPluginPath(Event event) {
        try {
            return URLDecoder.decode(event.getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
        } catch (Exception e) {
            return null;
        }
    }

    public String getPluginPath(Event event) {
        File file = new File("plugins/" + event.getClass().getSimpleName());

        if (!file.exists()) {
            file.mkdir();
        }

        return file.getAbsolutePath();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public SQLiteManager getSqlLiteManager(Event event, String database) throws Exception {
        File file = new File("plugins/" + event.getClass().getSimpleName());

        if (!file.exists()) {
            file.mkdir();
        }

        return new SQLiteManager(database.endsWith(".db") ? file.getAbsolutePath() + "/" + database : file.getAbsolutePath() + "/" + database + ".db");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public SQLiteManager getSqlLiteManager(Event event) throws Exception {
        File file = new File("plugins/" + event.getClass().getSimpleName());

        if (!file.exists()) {
            file.mkdir();
        }

        return new SQLiteManager(file.getAbsolutePath() + "/database.db");
    }
}
