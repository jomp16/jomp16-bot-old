/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.event;

import com.jomp16.irc.event.listener.DisableEvent;
import com.jomp16.irc.event.listener.InitEvent;
import com.jomp16.irc.event.listener.ResetEvent;
import com.jomp16.irc.event.listener.event.*;
import com.jomp16.irc.plugin.help.HelpRegister;

import java.util.ArrayList;

public abstract class Event {
    private ArrayList<HelpRegister> helpRegister = new ArrayList<>();

    public void registerHelp(HelpRegister helpRegister) {
        this.helpRegister.add(helpRegister);
    }

    public ArrayList<HelpRegister> getHelpRegister() {
        return helpRegister;
    }

    public void onJoin(JoinEvent joinEvent) {

    }

    public void onPart(PartEvent partEvent) {

    }

    public void onQuit(QuitEvent quitEvent) {

    }

    public void onNick(NickEvent nickEvent) {

    }

    public void onMode(ModeEvent modeEvent) {

    }

    public void onPrivMsg(PrivMsgEvent privMsgEvent) {

    }

    public void onKick(KickEvent kickEvent) {

    }

    public void onInit(InitEvent initEvent) throws Exception {

    }

    public void onReset(ResetEvent resetEvent) throws Exception {

    }

    public void onDisable(DisableEvent disableEvent) throws Exception {

    }

    public void respond() {

    }
}
