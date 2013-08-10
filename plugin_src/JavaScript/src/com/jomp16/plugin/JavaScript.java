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
import javax.script.ScriptException;
import java.io.InputStreamReader;
import java.net.URL;

public class JavaScript extends Event {
    private static ScriptEngine scriptEngine = null;

    @CommandFilter(value = "js", level = Level.OWNER)
    public void js(CommandEvent commandEvent) throws Exception {
        if (commandEvent.getArgs().size() >= 1) {
            scriptEngine.put("commandEvent", commandEvent);

            if (commandEvent.getArgs().get(0).equals("load")) {
                if (commandEvent.getArgs().size() >= 2) {
                    if (commandEvent.getArgs().get(1).equals("url")) {
                        if (commandEvent.getArgs().size() >= 3) {
                            Object object = scriptEngine.eval(new InputStreamReader(new URL(commandEvent.getArgs().get(2)).openStream()));
                            if (object != null) {
                                if (!object.equals("null")) {
                                    commandEvent.respond(object, false);
                                }
                            }
                        }
                    }
                }
            } else {
                new Thread(() -> {
                    try {
                        Object object = scriptEngine.eval(commandEvent.getArgs().get(0));

                        if (object != null) {
                            if (!object.equals("null")) {
                                commandEvent.respond(object, false);
                            }
                        }
                    } catch (ScriptException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } else {
            commandEvent.showUsage(this, "js");
        }
    }

    @Override
    public void onInit(InitEvent initEvent) throws Exception {
        initEvent.addHelp(this, new HelpRegister("js", "Run a java/javascript command", "(load url the_url) or ('java/javascript command') (note the quotes)", Level.OWNER));

        if (scriptEngine == null) {
            scriptEngine = new ScriptEngineManager().getEngineByName("JavaScript");
        }
    }

    @Override
    public void onDisable(DisableEvent disableEvent) throws Exception {
        scriptEngine = null;
    }
}
