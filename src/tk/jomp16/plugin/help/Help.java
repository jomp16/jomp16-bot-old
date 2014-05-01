/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.help;

import org.apache.commons.lang3.StringUtils;
import tk.jomp16.irc.IRCManager;
import tk.jomp16.irc.event.Command;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.Level;
import tk.jomp16.irc.event.events.PrivMsgEvent;
import tk.jomp16.irc.event.listener.event.CommandEvent;
import tk.jomp16.irc.event.listener.event.InitEvent;
import tk.jomp16.irc.event.listener.event.ResetEvent;
import tk.jomp16.language.LanguageManager;

import java.util.HashMap;
import java.util.Map;

public class Help extends Event {
    private static Map<String, String[]> helpNormal = new HashMap<>();
    private static Map<String, String[]> helpMod = new HashMap<>();
    private static Map<String, String[]> helpAdmin = new HashMap<>();
    private static Map<String, String[]> helpOwner = new HashMap<>();
    private static Map<String, HelpRegister> helpRegisters = new HashMap<>();
    private static LanguageManager languageManager;

    private static void registerHelp() {
        PrivMsgEvent.getEventRegisters().parallelStream().forEach(eventRegister -> eventRegister.event.getHelpRegister().parallelStream()
                .filter(helpRegister -> !helpRegisters.containsKey(helpRegister.getCommand()))
                .forEach(helpRegister -> {
                    helpRegisters.put(helpRegister.getCommand(), helpRegister);

                    addToList(helpRegister.getCommand(), helpRegister.getOptCommands(), helpRegister.getLevel());

                    /*if (helpRegister.getOptCommands() != null && helpRegister.getOptCommands().length != 0) {
                        for (String s : helpRegister.getOptCommands()) {
                            if (!helpRegisters.containsKey(s)) {
                                // TODO: ADD OPT COMMANDS INSIDE REAL COMMAND, LIKE on help: money [currency], other command [other opt command], other command
                                helpRegisters.put(s, helpRegister);

                                addToList(helpRegister.getCommand(), s, helpRegister.getLevel());
                            }
                        }
                    }*/
                }));

        /*Collections.sort(helpNormal, String::compareTo);
        Collections.sort(helpMod, String::compareTo);
        Collections.sort(helpAdmin, String::compareTo);
        Collections.sort(helpOwner, String::compareTo);*/
    }

    private static void addToList(String command, String[] command1, Level level) {
        switch (level) {
            case NORMAL:
                helpNormal.put(command, command1);
                helpMod.put(command, command1);
                helpAdmin.put(command, command1);
                helpOwner.put(command, command1);
                break;
            case MOD:
                helpMod.put(command, command1);
                helpAdmin.put(command, command1);
                helpOwner.put(command, command1);
                break;
            case ADMIN:
                helpAdmin.put(command, command1);
                helpOwner.put(command, command1);
                break;
            case OWNER:
                helpOwner.put(command, command1);
                break;
        }
    }

    public static void reloadHelp() {
        helpRegisters.clear();
        helpNormal.clear();
        helpMod.clear();
        helpAdmin.clear();
        helpOwner.clear();

        registerHelp();
    }

    public static Map<String, String[]> getHelpNormal() {
        return helpNormal;
    }

    public static Map<String, String[]> getHelpMod() {
        return helpMod;
    }

    public static Map<String, String[]> getHelpAdmin() {
        return helpAdmin;
    }

    public static Map<String, String[]> getHelpOwner() {
        return helpOwner;
    }

    public static Map<String, HelpRegister> getHelpRegisters() {
        return helpRegisters;
    }

    @Command("help")
    public void help(CommandEvent commandEvent) throws Exception {
        if (helpRegisters.size() == 0) {
            registerHelp();
        }

        if (commandEvent.getArgs().size() > 0) {
            if (commandEvent.getArgs().get(0).equals("all")) {
                commandEvent.respond(languageManager.getAsString("help.text.available.commands", getAllHelp(commandEvent.getUser().getLevel())));
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
                    commandEvent.respond(languageManager.getAsString("help.text.not.found"));
                }
            }
        } else {
            commandEvent.showUsage(this, "help");
        }
    }

    private String getAllHelp(Level level) {
        StringBuilder stringBuilder = new StringBuilder();

        switch (level) {
            case NORMAL:
                helpNormal.forEach((key, value) -> {
                    stringBuilder.append(key);

                    if (value != null) {
                        stringBuilder.append(" [")
                                .append(StringUtils.join(value, ", "))
                                .append("]");
                    }

                    stringBuilder.append(", ");
                });

                break;
            case MOD:
                helpMod.forEach((key, value) -> {
                    stringBuilder.append(key);

                    if (value != null) {
                        stringBuilder.append(" [")
                                .append(StringUtils.join(value, ", "))
                                .append("]");
                    }

                    stringBuilder.append(", ");
                });

                break;
            case ADMIN:
                helpAdmin.forEach((key, value) -> {
                    stringBuilder.append(key);

                    if (value != null) {
                        stringBuilder.append(" [")
                                .append(StringUtils.join(value, ", "))
                                .append("]");
                    }

                    stringBuilder.append(", ");
                });

                break;
            case OWNER:
                helpOwner.forEach((key, value) -> {
                    stringBuilder.append(key);

                    if (value != null) {
                        stringBuilder.append(" [")
                                .append(StringUtils.join(value, ", "))
                                .append("]");
                    }

                    stringBuilder.append(", ");
                });

                break;
        }

        String tmp = stringBuilder.toString();

        return tmp.substring(0, tmp.length() - 2);
    }

    private String getHelpInfo(IRCManager ircManager, HelpRegister helpRegister) {
        if (helpRegister.getOptCommands() != null && helpRegister.getOptCommands().length != 0) {
            String usage = helpRegister.getUsage();

            if (usage != null) {
                return languageManager.getAsString("help.text.with.opt.command",
                        ircManager.getConfiguration().getPrefix(),
                        helpRegister.getCommand(),
                        StringUtils.join(helpRegister.getOptCommands(), ", "),
                        helpRegister.getHelp(),
                        helpRegister.getUsage());
            } else {
                return languageManager.getAsString("help.text.with.opt.command",
                        ircManager.getConfiguration().getPrefix(),
                        helpRegister.getCommand(),
                        StringUtils.join(helpRegister.getOptCommands(), ", "),
                        helpRegister.getHelp(),
                        "");
            }
        } else {
            String usage = helpRegister.getUsage();

            if (usage != null) {
                return languageManager.getAsString("help.text.without.opt.command",
                        ircManager.getConfiguration().getPrefix(),
                        helpRegister.getCommand(),
                        helpRegister.getHelp(),
                        helpRegister.getUsage());
            } else {
                return languageManager.getAsString("help.text.without.opt.command",
                        ircManager.getConfiguration().getPrefix(),
                        helpRegister.getCommand(),
                        helpRegister.getHelp(),
                        "");
            }
        }
    }

    @Override
    public void onInit(InitEvent initEvent) throws Exception {
        languageManager = new LanguageManager("tk.jomp16.plugin.resource.Strings");

        initEvent.addHelp(this, new HelpRegister("help",
                languageManager.getAsString("help.help.text"),
                languageManager.getAsString("help.help.usage")));
    }

    @Override
    public void onReset(ResetEvent resetEvent) throws Exception {
        helpRegisters.clear();
        helpNormal.clear();
        helpMod.clear();
        helpAdmin.clear();
        helpOwner.clear();

        registerHelp();
    }
}
