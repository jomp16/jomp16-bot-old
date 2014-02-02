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
import tk.jomp16.irc.event.listener.CommandEvent;
import tk.jomp16.irc.event.listener.InitEvent;
import tk.jomp16.irc.plugin.help.HelpRegister;
import tk.jomp16.language.LanguageManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Exec extends Event {
    private LanguageManager languageManager;

    @Command(value = "exec", level = Level.OWNER)
    public void exec(CommandEvent commandEvent) {
        Process process = null;

        if (commandEvent.getMessage().length() > 0) {
            String tmp = commandEvent.getMessage();

            try {
                process = Runtime.getRuntime().exec(tmp);
            } catch (Exception e) {
                commandEvent.respond(languageManager.getString("error"));
            }

            assert process != null;
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
                commandEvent.respond(languageManager.getString("error"));
            }
        }
    }

    @Override
    public void onInit(InitEvent initEvent) throws Exception {
        languageManager = Vars.getLanguageManager("tk.jomp16.plugin.bash.resource.Strings");

        initEvent.addHelp(this, new HelpRegister("exec", languageManager.getString("help.exec"), languageManager.getString("usage.exec"), Level.OWNER));
    }
}
