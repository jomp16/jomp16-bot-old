/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.commands;

import org.apache.commons.lang3.StringUtils;
import tk.jomp16.irc.event.Command;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.Level;
import tk.jomp16.irc.event.events.PrivMsgEvent;
import tk.jomp16.irc.event.listener.event.CommandEvent;
import tk.jomp16.irc.event.listener.event.InitEvent;
import tk.jomp16.language.LanguageManager;
import tk.jomp16.plugin.help.HelpRegister;

import java.util.HashMap;
import java.util.Map;

public class Commands extends Event {
    private static Map<String, String[]> commandsNormal = new HashMap<>();
    private static Map<String, String[]> commandsMod = new HashMap<>();
    private static Map<String, String[]> commandsAdmin = new HashMap<>();
    private static Map<String, String[]> commandsOwner = new HashMap<>();
    private LanguageManager languageManager;

    public static void removeCommand(PrivMsgEvent.EventRegister register) {
        switch (register.level) {
            case NORMAL:
                commandsNormal.remove(register.command);
                commandsMod.remove(register.command);
                commandsAdmin.remove(register.command);
                commandsOwner.remove(register.command);
                break;
            case MOD:
                commandsMod.remove(register.command);
                commandsAdmin.remove(register.command);
                commandsOwner.remove(register.command);
                break;
            case ADMIN:
                commandsAdmin.remove(register.command);
                commandsOwner.remove(register.command);
                break;
            case OWNER:
                commandsOwner.remove(register.command);
                break;
        }
    }

    public static void addCommand(PrivMsgEvent.EventRegister register) {
        switch (register.level) {
            case NORMAL:
                commandsNormal.put(register.command, register.optCommands);
                commandsMod.put(register.command, register.optCommands);
                commandsAdmin.put(register.command, register.optCommands);
                commandsOwner.put(register.command, register.optCommands);
                break;
            case MOD:
                commandsMod.put(register.command, register.optCommands);
                commandsAdmin.put(register.command, register.optCommands);
                commandsOwner.put(register.command, register.optCommands);
                break;
            case ADMIN:
                commandsAdmin.put(register.command, register.optCommands);
                commandsOwner.put(register.command, register.optCommands);
                break;
            case OWNER:
                commandsOwner.put(register.command, register.optCommands);
                break;
        }
    }

    @Command("commands")
    public void commands(CommandEvent commandEvent) {
        commandEvent.respond(languageManager.getAsString("commands.text.available.commands", getAvailableCommands(commandEvent.getUser().getLevel())));
    }

    private String getAvailableCommands(Level level) {
        StringBuilder stringBuilder = new StringBuilder();

        switch (level) {
            case NORMAL:
                commandsNormal.forEach((key, value) -> {
                    stringBuilder.append(key);

                    if (value != null && !value[0].equals("")) {
                        stringBuilder.append(" [")
                                .append(StringUtils.join(value, ", "))
                                .append("]");
                    }

                    stringBuilder.append(", ");
                });

                break;
            case MOD:
                commandsMod.forEach((key, value) -> {
                    stringBuilder.append(key);

                    if (value != null && !value[0].equals("")) {
                        stringBuilder.append(" [")
                                .append(StringUtils.join(value, ", "))
                                .append("]");
                    }

                    stringBuilder.append(", ");
                });

                break;
            case ADMIN:
                commandsAdmin.forEach((key, value) -> {
                    stringBuilder.append(key);

                    if (value != null && !value[0].equals("")) {
                        stringBuilder.append(" [")
                                .append(StringUtils.join(value, ", "))
                                .append("]");
                    }

                    stringBuilder.append(", ");
                });

                break;
            case OWNER:
                commandsOwner.forEach((key, value) -> {
                    stringBuilder.append(key);

                    if (value != null && !value[0].equals("")) {
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

    @Override
    public void onInit(InitEvent initEvent) throws Exception {
        languageManager = new LanguageManager("tk.jomp16.plugin.resource.Strings");

        initEvent.addHelp(this, new HelpRegister("commands", languageManager.getAsString("commands.help.text")));
    }
}
