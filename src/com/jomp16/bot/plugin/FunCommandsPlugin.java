/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.bot.plugin;

import com.jomp16.irc.event.CommandFilter;
import com.jomp16.irc.event.Event;
import com.jomp16.irc.event.listener.CommandEvent;
import com.jomp16.irc.event.listener.InitEvent;
import com.jomp16.irc.plugin.help.HelpRegister;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class FunCommandsPlugin extends Event {
    @CommandFilter("fortune")
    public void fortune(CommandEvent commandEvent) throws Exception {
        if (commandEvent.getArgs().size() >= 0) {
            Process process = (commandEvent.getArgs().size() >= 1)
                    ? Runtime.getRuntime().exec("fortune", new String[]{commandEvent.getArgs().get(0)})
                    : Runtime.getRuntime().exec("fortune");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String tmp;
                while ((tmp = reader.readLine()) != null) {
                    commandEvent.respondWithoutLock(tmp, false);
                }
            }
        }
    }

    @Override
    public void onInit(InitEvent initEvent) throws Exception {
        initEvent.addHelp(this, new HelpRegister("fortune", "Get a fortune by using the fortune command from Linux", "fortune <optional param>"));
    }
}
