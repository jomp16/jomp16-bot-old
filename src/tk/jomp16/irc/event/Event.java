/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.event;

import tk.jomp16.irc.event.listener.event.*;
import tk.jomp16.plugin.help.HelpRegister;

import java.util.ArrayList;
import java.util.List;

public abstract class Event {
    private List<HelpRegister> helpRegister = new ArrayList<>();

    public void registerHelp(HelpRegister helpRegister) {
        this.helpRegister.add(helpRegister);
    }

    public List<HelpRegister> getHelpRegister() {
        return helpRegister;
    }

    public void onJoin(JoinEvent joinEvent) throws Exception {

    }

    public void onPart(PartEvent partEvent) throws Exception {

    }

    public void onQuit(QuitEvent quitEvent) throws Exception {

    }

    public void onNick(NickEvent nickEvent) throws Exception {

    }

    public void onMode(ModeEvent modeEvent) throws Exception {

    }

    public void onPrivMsg(PrivMsgEvent privMsgEvent) throws Exception {

    }

    public void onKick(KickEvent kickEvent) throws Exception {

    }

    public void onInit(InitEvent initEvent) throws Exception {

    }

    public void onReset(ResetEvent resetEvent) throws Exception {

    }

    public void onDisable(DisableEvent disableEvent) throws Exception {

    }

    public void respond() throws Exception {

    }
}
