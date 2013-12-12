/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.plugin.help;

import org.apache.commons.lang3.StringUtils;
import tk.jomp16.irc.IRCManager;
import tk.jomp16.irc.event.Command;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.Level;
import tk.jomp16.irc.event.listener.CommandEvent;
import tk.jomp16.irc.event.listener.InitEvent;
import tk.jomp16.irc.event.listener.ResetEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Help extends Event {
    private static ArrayList<String> helpNormal = new ArrayList<>();
    private static ArrayList<String> helpMod = new ArrayList<>();
    private static ArrayList<String> helpAdmin = new ArrayList<>();
    private static ArrayList<String> helpOwner = new ArrayList<>();
    private static HashMap<String, HelpRegister> helpRegisters = new HashMap<>();

    private static void registerHelp(ArrayList<Event> events) {
        for (Event event : events) {
            for (HelpRegister helpRegister : event.getHelpRegister()) {
                helpRegisters.put(helpRegister.getCommand(), helpRegister);

                addToList(helpRegister.getCommand(), helpRegister.getLevel());

                if (helpRegister.getOptCommands() != null && helpRegister.getOptCommands().length != 0) {
                    for (String s : helpRegister.getOptCommands()) {
                        helpRegisters.put(s, helpRegister);

                        addToList(s, helpRegister.getLevel());
                    }
                }
            }
        }
    }

    private static void addToList(String command, Level level) {
        switch (level) {
            case NORMAL:
                helpNormal.add(command);
                helpMod.add(command);
                helpAdmin.add(command);
                helpOwner.add(command);
                break;
            case MOD:
                helpMod.add(command);
                helpAdmin.add(command);
                helpOwner.add(command);
                break;
            case ADMIN:
                helpAdmin.add(command);
                helpOwner.add(command);
                break;
            case OWNER:
                helpOwner.add(command);
                break;
        }

        Collections.sort(helpNormal, String::compareTo);
        Collections.sort(helpMod, String::compareTo);
        Collections.sort(helpAdmin, String::compareTo);
        Collections.sort(helpOwner, String::compareTo);
    }

    @Command("help")
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
        if (helpRegister.getOptCommands() != null && helpRegister.getOptCommands().length != 0) {
            return "Command: " + ircManager.getConfiguration().getPrefix() + helpRegister.getCommand() + " || " + "Optional commands: " + StringUtils.join(helpRegister.getOptCommands(), ", ") + " || " + "Help: " + helpRegister.getHelp() + " || " + "Usage: " + ircManager.getConfiguration().getPrefix() + helpRegister.getCommand() + " " + helpRegister.getUsage();
        } else {
            return "Command: " + ircManager.getConfiguration().getPrefix() + helpRegister.getCommand() + " || " + "Help: " + helpRegister.getHelp() + " || " + "Usage: " + ircManager.getConfiguration().getPrefix() + helpRegister.getCommand() + " " + helpRegister.getUsage();
        }
    }

    public static void reloadHelp(ArrayList<Event> events) {
        helpRegisters.clear();
        helpNormal.clear();
        helpMod.clear();
        helpAdmin.clear();
        helpOwner.clear();
        registerHelp(events);
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
