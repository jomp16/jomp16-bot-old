/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin;

import tk.jomp16.irc.event.Command;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.Level;
import tk.jomp16.irc.event.listener.CommandEvent;
import tk.jomp16.irc.event.listener.DisableEvent;
import tk.jomp16.irc.event.listener.InitEvent;
import tk.jomp16.irc.plugin.help.HelpRegister;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.InputStreamReader;
import java.net.URL;

public class JavaScript extends Event {
    private static ScriptEngine scriptEngine = null;

    @Command(value = {"js", "javascript"}, level = Level.OWNER)
    public void js(CommandEvent commandEvent) throws Exception {
        if (commandEvent.getMessage().length() > 0 || commandEvent.getArgs().size() >= 1) {
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
                Object object = scriptEngine.eval(commandEvent.getMessage());

                if (object != null) {
                    if (!object.equals("null")) {
                        commandEvent.respond(object, false);
                    }
                }
            }
        } else {
            commandEvent.showUsage(this, commandEvent.getCommand());
        }
    }

    @Override
    public void onInit(InitEvent initEvent) throws Exception {
        initEvent.addHelp(this, new HelpRegister("js", new String[]{"javascript"}, "Run a java/javascript command", "(load url the_url) or (java/javascript command)", Level.OWNER));

        if (scriptEngine == null) {
            scriptEngine = new ScriptEngineManager().getEngineByName("JavaScript");
        }
    }

    @Override
    public void onDisable(DisableEvent disableEvent) throws Exception {
        scriptEngine = null;
    }
}
