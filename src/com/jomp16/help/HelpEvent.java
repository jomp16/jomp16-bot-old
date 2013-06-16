/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.help;

import com.jomp16.irc.event.CommandFilter;
import com.jomp16.irc.event.Event;
import com.jomp16.irc.event.listener.CommandEvent;
import com.jomp16.irc.event.listener.InitEvent;
import com.jomp16.irc.event.listener.ReloadEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class HelpEvent extends Event {
    private static ArrayList<HelpRegister> help = new ArrayList<>();

    private static ArrayList<HelpRegister> registerHelp(ArrayList<Event> events) {
        ArrayList<HelpRegister> helpTmp = new ArrayList<>();

        for (Event event : events) {
            helpTmp.addAll(event.getHelp());
        }
        return helpTmp;
    }

    @CommandFilter("help")
    public void help(CommandEvent commandEvent) {
        if (commandEvent.getArgs().size() > 0) {
            StringBuilder builder = new StringBuilder();
            if (commandEvent.getArgs().get(0).equals("all")) {

                ArrayList<String> helpTmp = new ArrayList<>();
                for (HelpRegister helpRegister : help) {
                    switch (helpRegister.getLevel()) {
                        case NORMAL:
                            helpTmp.add(helpRegister.getCommand());
                            break;
                        case MOD:
                            if (commandEvent.getIrcManager().getMods().contains(commandEvent.getUser().getCompleteHost())) {
                                helpTmp.add(helpRegister.getCommand());
                            }
                            break;
                        case ADMIN:
                            if (commandEvent.getIrcManager().getAdmins().contains(commandEvent.getUser().getCompleteHost())) {
                                helpTmp.add(helpRegister.getCommand());
                            }
                            break;
                    }
                }

                builder.append("Available help for that commands: ")
                        .append(StringUtils.join(helpTmp, ", "));
            } else {
                for (HelpRegister helpRegister : help) {
                    if (helpRegister.getCommand().equals(commandEvent.getArgs().get(0))) {
                        builder.append("Command: ")
                                .append(commandEvent.getIrcManager().getConfiguration().getPrefix())
                                .append(helpRegister.getCommand())
                                .append(" || ")
                                .append("Help: ")
                                .append(helpRegister.getHelp())
                                .append(" || ")
                                .append("Usage: ")
                                .append(commandEvent.getIrcManager().getConfiguration().getPrefix())
                                .append(helpRegister.getUsage());
                    }
                }
            }

            commandEvent.respond(builder);
        } else {
            commandEvent.respond("Usage goes here...");
        }
    }

    @Override
    public void onInit(InitEvent initEvent) throws Exception {
        help.addAll(registerHelp(initEvent.getIrcManager().getEvents()));
    }

    @Override
    public void onReload(ReloadEvent reloadEvent) throws Exception {
        help.clear();
        help.addAll(registerHelp(reloadEvent.getIrcManager().getEvents()));
    }
}
