/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.ruby;

import tk.jomp16.irc.event.Command;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.Level;
import tk.jomp16.irc.event.listener.event.CommandEvent;
import tk.jomp16.irc.event.listener.event.DisableEvent;
import tk.jomp16.irc.event.listener.event.InitEvent;
import tk.jomp16.plugin.help.HelpRegister;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.InputStreamReader;
import java.net.URL;

public class Ruby extends Event {
    private ScriptEngine scriptEngine;

    @Command(value = "ruby", optCommands = "jruby", level = Level.OWNER, args = {"url:"})
    public void ruby(CommandEvent commandEvent) throws Exception {
        if (commandEvent.getMessage().length() > 0 || commandEvent.getArgs().size() >= 1) {
            scriptEngine.put("commandEvent", commandEvent);

            if (commandEvent.getOptionSet().has("url")) {
                Object object = scriptEngine.eval(new InputStreamReader(
                        new URL((String) commandEvent.getOptionSet().valueOf("url")).openStream()));

                if (object != null) {
                    if (!object.equals("null")) {
                        commandEvent.respond(object, false);
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
        initEvent.addHelp(this, new HelpRegister("ruby", new String[]{"jruby"}, "Run a ruby/jruby command", "-url the url or ruby/jruby command", Level.OWNER));

        scriptEngine = new ScriptEngineManager(this.getClass().getClassLoader()).getEngineByName("ruby");
    }

    @Override
    public void onDisable(DisableEvent disableEvent) throws Exception {
        scriptEngine = null;
    }
}
