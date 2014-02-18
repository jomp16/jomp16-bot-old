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
import tk.jomp16.irc.event.listener.CommandEvent;
import tk.jomp16.irc.event.listener.InitEvent;
import tk.jomp16.irc.event.listener.ResetEvent;
import tk.jomp16.language.LanguageManager;

import java.util.*;

public class Help extends Event {
    private static List<String> helpNormal = new ArrayList<>();
    private static List<String> helpMod = new ArrayList<>();
    private static List<String> helpAdmin = new ArrayList<>();
    private static List<String> helpOwner = new ArrayList<>();
    private static Map<String, HelpRegister> helpRegisters = new HashMap<>();
    private static LanguageManager languageManager;

    private static void registerHelp() {
        for (PrivMsgEvent.EventRegister eventRegister : PrivMsgEvent.getEventRegisters()) {
            for (HelpRegister helpRegister : eventRegister.event.getHelpRegister()) {
                helpRegisters.put(helpRegister.getCommand(), helpRegister);

                addToList(helpRegister.getCommand(), helpRegister.getLevel());

                if (helpRegister.getOptCommands() != null && helpRegister.getOptCommands().length != 0) {
                    for (String s : helpRegister.getOptCommands()) {
                        // TODO: ADD OPT COMMANDS INSIDE REAL COMMAND, LIKE on help: money [currency], other command [other opt command], other command
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
            registerHelp();
        }

        if (commandEvent.getArgs().size() > 0) {
            if (commandEvent.getArgs().get(0).equals("all")) {
                switch (commandEvent.getUser().getLevel()) {
                    case NORMAL:
                        commandEvent.respond(languageManager.getAsString("help.text.available.commands", StringUtils.join(helpNormal, ", ")));
                        break;
                    case MOD:
                        commandEvent.respond(languageManager.getAsString("help.text.available.commands", StringUtils.join(helpMod, ", ")));
                        break;
                    case ADMIN:
                        commandEvent.respond(languageManager.getAsString("help.text.available.commands", StringUtils.join(helpAdmin, ", ")));
                        break;
                    case OWNER:
                        commandEvent.respond(languageManager.getAsString("help.text.available.commands", StringUtils.join(helpOwner, ", ")));
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
                    commandEvent.respond(languageManager.getAsString("help.text.not.found"));
                }
            }
        } else {
            commandEvent.showUsage(this, "help");
        }
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
                        helpRegister.getUsage());
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

    public static void reloadHelp() {
        helpRegisters.clear();
        helpNormal.clear();
        helpMod.clear();
        helpAdmin.clear();
        helpOwner.clear();

        registerHelp();
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
