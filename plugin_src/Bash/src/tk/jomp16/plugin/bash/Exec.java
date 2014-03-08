/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.bash;

import tk.jomp16.irc.event.Command;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.Level;
import tk.jomp16.irc.event.listener.event.CommandEvent;
import tk.jomp16.irc.event.listener.event.InitEvent;
import tk.jomp16.language.LanguageManager;
import tk.jomp16.plugin.help.HelpRegister;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Exec extends Event {
    private LanguageManager languageManager;

    @Command(value = "exec", level = Level.OWNER)
    public void exec(CommandEvent commandEvent) {
        Process process;

        if (commandEvent.getMessage().length() > 0) {
            String tmp = commandEvent.getMessage();

            try {
                process = Runtime.getRuntime().exec(tmp);
            } catch (Exception e) {
                commandEvent.respond(languageManager.getAsString("error"));
                return;
            }

            try (BufferedReader commandReader = new BufferedReader(new InputStreamReader(process.getInputStream())); BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String tmp1;
                while ((tmp1 = commandReader.readLine()) != null) {
                    if (tmp1.length() == 0) {
                        commandEvent.respond(" ", false);
                    } else {
                        commandEvent.respond(tmp1, false);
                    }
                }

                while ((tmp1 = errorReader.readLine()) != null) {
                    if (tmp1.length() == 0) {
                        commandEvent.respond(" ", false);
                    } else {
                        commandEvent.respond(tmp1, false);
                    }
                }
            } catch (Exception e) {
                commandEvent.respond(languageManager.getAsString("error"));
            }
        }
    }

    @Override
    public void onInit(InitEvent initEvent) throws Exception {
        languageManager = initEvent.getLanguageManager(this, "tk.jomp16.plugin.bash.resource.Strings");

        initEvent.addHelp(this, new HelpRegister("exec", languageManager.getAsString("help.exec"), languageManager.getAsString("usage.exec"), Level.OWNER));
    }
}
