/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.plugin;

import com.jomp16.irc.event.CommandFilter;
import com.jomp16.irc.event.Event;
import com.jomp16.irc.event.Level;
import com.jomp16.irc.event.listener.CommandEvent;
import com.jomp16.irc.event.listener.DisableEvent;
import com.jomp16.irc.event.listener.InitEvent;
import com.jomp16.irc.plugin.help.HelpRegister;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class JavaScript extends Event {
    private static ScriptEngine scriptEngine = null;

    @CommandFilter(value = "js", level = Level.OWNER)
    public void js(CommandEvent commandEvent) throws Exception {
        if (commandEvent.getMessage().length() > 4) {
            scriptEngine.put("commandEvent", commandEvent);
            scriptEngine.put("testEvent", this);
            try {
                Object object = scriptEngine.eval(commandEvent.getMessage().substring(4));
                if (object != null) {
                    if (!object.equals("null")) {
                        commandEvent.respond(object, false);
                    }
                }
            } catch (Exception e) {
                commandEvent.respond(e);
            }
        } else {
            commandEvent.showUsage("js");
        }
    }

    @Override
    public void onInit(InitEvent initEvent) throws Exception {
        if (scriptEngine == null) {
            getLog().debug("Created new instance of ScriptEngine");
            scriptEngine = new ScriptEngineManager().getEngineByName("JavaScript");
        }
        initEvent.addHelp(this, new HelpRegister("js", "Run a java/javascript command", "js java/javascript command", Level.OWNER));
    }

    @Override
    public void onDisable(DisableEvent disableEvent) throws Exception {
        super.onDisable(disableEvent);
        scriptEngine = null;
    }
}
