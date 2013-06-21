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
import com.jomp16.irc.event.listener.CommandEvent;
import com.jomp16.irc.event.listener.InitEvent;
import com.jomp16.irc.event.listener.ReloadEvent;
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
                helpRegisters.put(helpRegister.getCommand(), helpRegister);

                switch (helpRegister.getLevel()) {
                    case NORMAL:
                        helpNormal.add(helpRegister.getCommand());
                        helpMod.add(helpRegister.getCommand());
                        helpAdmin.add(helpRegister.getCommand());
                        helpOwner.add(helpRegister.getCommand());
                        break;
                    case MOD:
                        helpMod.add(helpRegister.getCommand());
                        helpAdmin.add(helpRegister.getCommand());
                        helpOwner.add(helpRegister.getCommand());
                        break;
                    case ADMIN:
                        helpAdmin.add(helpRegister.getCommand());
                        helpOwner.add(helpRegister.getCommand());
                        break;
                    case OWNER:
                        helpOwner.add(helpRegister.getCommand());
                        break;
                }
            }
        }
    }

    @CommandFilter("help")
    public void help(CommandEvent commandEvent) {
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
                    switch (helpRegister.getLevel()) {
                        case NORMAL:
                            commandEvent.respond(getHelpInfo(commandEvent.getIrcManager(), helpRegister));
                            break;
                        case MOD:
                            if (commandEvent.getIrcManager().getMods().contains(commandEvent.getUser().getCompleteHost())
                                    || commandEvent.getIrcManager().getAdmins().contains(commandEvent.getUser().getCompleteHost())
                                    || commandEvent.getIrcManager().getOwners().contains(commandEvent.getUser().getCompleteHost())) {
                                commandEvent.respond(getHelpInfo(commandEvent.getIrcManager(), helpRegister));
                            }
                            break;
                        case ADMIN:
                            if (commandEvent.getIrcManager().getAdmins().contains(commandEvent.getUser().getCompleteHost())
                                    || commandEvent.getIrcManager().getOwners().contains(commandEvent.getUser().getCompleteHost())) {
                                commandEvent.respond(getHelpInfo(commandEvent.getIrcManager(), helpRegister));
                            }
                            break;
                        case OWNER:
                            if (commandEvent.getIrcManager().getOwners().contains(commandEvent.getUser().getCompleteHost())) {
                                commandEvent.respond(getHelpInfo(commandEvent.getIrcManager(), helpRegister));
                            }
                            break;
                    }
                } else {
                    commandEvent.respond("No help found for that command, maybe the help doesn't exists or you mistake the command?");
                }
            }
        } else {
            commandEvent.showUsage("help");
        }
    }

    private String getHelpInfo(IRCManager ircManager, HelpRegister helpRegister) {
        return "Command: " + ircManager.getConfiguration().getPrefix() + helpRegister.getCommand() + " || " + "Help: " + helpRegister.getHelp() + " || " + "Usage: " + ircManager.getConfiguration().getPrefix() + helpRegister.getUsage();
    }

    @Override
    public void onInit(InitEvent initEvent) throws Exception {
        initEvent.addHelp(this, new HelpRegister("help", "get help of a plugin", "help <all||plugin_name>"));
    }

    @Override
    public void onReload(ReloadEvent reloadEvent) throws Exception {
        helpRegisters.clear();
        helpNormal.clear();
        helpMod.clear();
        helpAdmin.clear();
        helpOwner.clear();
        registerHelp(reloadEvent.getIrcManager().getEvents());
    }
}
