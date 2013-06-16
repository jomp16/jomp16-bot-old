/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.event;

import com.jomp16.help.Help;
import com.jomp16.irc.event.listener.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public abstract class Event {
    private ArrayList<Help> help = new ArrayList<>();

    /**
     * Void to run something when a join is detected, you can put auto-voice, etc here
     *
     * @param joinEvent
     */
    public void onJoin(JoinEvent joinEvent) {

    }

    /**
     * Basic void to respond to any event without something special, like ping/pong, quit, etc
     *
     * @param commandEvent
     */
    public void respond(CommandEvent commandEvent) {

    }

    /**
     * Register the help
     *
     * @param help
     */
    public void registerHelp(Help help) {
        this.help.add(help);
    }

    /**
     * Get the help for HelpEvent
     *
     * @return a array of helpRegister
     */
    public ArrayList<Help> getHelp() {
        return help;
    }

    public Logger getLog() {
        return LogManager.getLogger(this.getClass().getSimpleName());
    }

    /**
     * InitEvent to the plugin developer register whatever the fuck instead to put on class constructor
     *
     * @param initEvent
     * @throws Exception
     */
    public void onInit(InitEvent initEvent) throws Exception {

    }

    /**
     * DisableEvent called when someone call it
     *
     * @param disableEvent
     * @throws Exception
     */
    public void onDisable(DisableEvent disableEvent) throws Exception {
        disableEvent.getIrcManager().getEvents().remove(this);
    }

    /**
     * ReloadEvent called when someone call it
     *
     * @param reloadEvent
     * @throws Exception
     */
    public void onReload(ReloadEvent reloadEvent) throws Exception {

    }
}
