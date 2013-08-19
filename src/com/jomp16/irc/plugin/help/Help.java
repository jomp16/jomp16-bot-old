/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.plugin.help;

import com.jomp16.irc.IRCManager;
import com.jomp16.irc.event.CommandFilter;
import com.jomp16.irc.event.Event;
import com.jomp16.irc.event.Level;
import com.jomp16.irc.event.listener.CommandEvent;
import com.jomp16.irc.event.listener.InitEvent;
import com.jomp16.irc.event.listener.ResetEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class Help extends Event {
    private ArrayList<String> helpNormal = new ArrayList<>();
    private ArrayList<String> helpMod = new ArrayList<>();
    private ArrayList<String> helpAdmin = new ArrayList<>();
    private ArrayList<String> helpOwner = new ArrayList<>();
    private HashMap<String, HelpRegister> helpRegisters = new HashMap<>();

    private void registerHelp(ArrayList<Event> events) {
        for (Event event : events) {
            for (HelpRegister helpRegister : event.getHelpRegister()) {
                for (String s : helpRegister.getCommand()) {
                    helpRegisters.put(s, helpRegister);

                    switch (helpRegister.getLevel()) {
                        case NORMAL:
                            helpNormal.add(s);
                            helpMod.add(s);
                            helpAdmin.add(s);
                            helpOwner.add(s);
                            break;
                        case MOD:
                            helpMod.add(s);
                            helpAdmin.add(s);
                            helpOwner.add(s);
                            break;
                        case ADMIN:
                            helpAdmin.add(s);
                            helpOwner.add(s);
                            break;
                        case OWNER:
                            helpOwner.add(s);
                            break;
                    }
                }
            }
        }
    }

    @CommandFilter("help")
    public void help(CommandEvent commandEvent) throws Exception {
        if (helpRegisters.size() == 0) {
            registerHelp(commandEvent.getIrcManager().getEvents());
        }

        if (commandEvent.getArgs().size() > 0) {
            if (commandEvent.getArgs().get(0).equals("all")) {
                switch (commandEvent.getUser().getLevel()) {
                    case NORMAL:
                        commandEvent.respond("Available help for that commands: " + StringUtils.join(helpNormal, ", "));
                        break;
                    case MOD:
                        commandEvent.respond("Available help for that commands: " + StringUtils.join(helpMod, ", "));
                        break;
                    case ADMIN:
                        commandEvent.respond("Available help for that commands: " + StringUtils.join(helpAdmin, ", "));
                        break;
                    case OWNER:
                        commandEvent.respond("Available help for that commands: " + StringUtils.join(helpOwner, ", "));
                        break;
                }
            } else {
                if (helpRegisters.containsKey(commandEvent.getArgs().get(0))) {
                    HelpRegister helpRegister = helpRegisters.get(commandEvent.getArgs().get(0));
                    Level level = commandEvent.getUser().getLevel();

                    switch (helpRegister.getLevel()) {
                        case NORMAL:
                            commandEvent.respond(getHelpInfo(commandEvent.getIrcManager(), helpRegister));
                            break;
                        case MOD:
                            if (level.equals(Level.MOD)) {
                                commandEvent.respond(getHelpInfo(commandEvent.getIrcManager(), helpRegister));
                            }
                            break;
                        case ADMIN:
                            if (level.equals(Level.ADMIN)) {
                                commandEvent.respond(getHelpInfo(commandEvent.getIrcManager(), helpRegister));
                            }
                            break;
                        case OWNER:
                            if (level.equals(Level.OWNER)) {
                                commandEvent.respond(getHelpInfo(commandEvent.getIrcManager(), helpRegister));
                            }
                            break;
                    }
                } else {
                    commandEvent.respond("No help found for that command, maybe the help doesn't exists or you mistyped the command?");
                }
            }
        } else {
            commandEvent.showUsage(this, "help");
        }
    }

    private String getHelpInfo(IRCManager ircManager, HelpRegister helpRegister) {
        return "Command: " + ircManager.getConfiguration().getPrefix() + helpRegister.getCommand() + " || " + "Help: " + helpRegister.getHelp() + " || " + "Usage: " + ircManager.getConfiguration().getPrefix() + helpRegister.getCommand() + " " + helpRegister.getUsage();
    }

    @Override
    public void onInit(InitEvent initEvent) throws Exception {
        initEvent.addHelp(this, new HelpRegister("help", "get help of a plugin", "<all||plugin_name>"));
    }

    @Override
    public void onReset(ResetEvent resetEvent) throws Exception {
        helpRegisters.clear();
        helpNormal.clear();
        helpMod.clear();
        helpAdmin.clear();
        helpOwner.clear();
        registerHelp(resetEvent.getIrcManager().getEvents());
    }
}
