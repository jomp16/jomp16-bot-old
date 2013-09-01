/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.bot.plugin;

import tk.jomp16.irc.event.CommandFilter;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.listener.CommandEvent;
import tk.jomp16.irc.event.listener.InitEvent;
import tk.jomp16.irc.event.listener.event.PrivMsgEvent;
import tk.jomp16.irc.plugin.help.HelpRegister;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class FunCommandsPlugin extends Event {
    @CommandFilter("fortune")
    public void fortune(CommandEvent commandEvent) throws Exception {
        if (commandEvent.getArgs().size() >= 0) {
            Process process;
            if (commandEvent.getArgs().size() >= 1) {
                commandEvent.getArgs().add(0, "fortune");
                String[] a = commandEvent.getArgs().toArray(new String[commandEvent.getArgs().size()]);
                process = Runtime.getRuntime().exec(a);
            } else {
                process = Runtime.getRuntime().exec("fortune");
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String tmp;
                while ((tmp = reader.readLine()) != null) {
                    if (tmp.length() == 0) {
                        commandEvent.respond(" ", false);
                    } else {
                        commandEvent.respond(tmp, false);
                    }
                }
            }
        }
    }

    @Override
    public void onInit(InitEvent initEvent) throws Exception {
        initEvent.addHelp(this, new HelpRegister("fortune", "Get a fortune by using the fortune command from Linux", "fortune <optional param>"));
    }

    @Override   // move it
    public void onPrivMsg(PrivMsgEvent privMsgEvent) {
        String tmp = privMsgEvent.getMessage().toLowerCase();

        if (tmp.startsWith(privMsgEvent.getIrcManager().getConfiguration().getNick() + ": ")) {
            if (tmp.contains("i love you")) {
                privMsgEvent.respond("I love you too dawg!");
            } else if (tmp.contains("i don't love you") || tmp.contains("i do not love you")) {
                privMsgEvent.respond("Why? Anyways, I still love you, and hope you love me too in the future =)");
            }
        }
    }
}
