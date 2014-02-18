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
import tk.jomp16.irc.event.events.PrivMsgEvent;
import tk.jomp16.irc.event.listener.CommandEvent;
import tk.jomp16.irc.event.listener.InitEvent;
import tk.jomp16.irc.event.listener.ResetEvent;
import tk.jomp16.language.LanguageManager;
import tk.jomp16.plugin.help.HelpRegister;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Commands extends Event {
    private static List<String> commandsNormal = new ArrayList<>();
    private static List<String> commandsMod = new ArrayList<>();
    private static List<String> commandsAdmin = new ArrayList<>();
    private static List<String> commandsOwner = new ArrayList<>();
    private LanguageManager languageManager;
    private static boolean flag = false;

    private static void registerCommands() {
        for (PrivMsgEvent.EventRegister register : PrivMsgEvent.getEventRegisters()) {
            switch (register.level) {
                case NORMAL:
                    commandsNormal.add(register.command);
                    commandsMod.add(register.command);
                    commandsAdmin.add(register.command);
                    commandsOwner.add(register.command);
                    break;
                case MOD:
                    commandsMod.add(register.command);
                    commandsAdmin.add(register.command);
                    commandsOwner.add(register.command);
                    break;
                case ADMIN:
                    commandsAdmin.add(register.command);
                    commandsOwner.add(register.command);
                    break;
                case OWNER:
                    commandsOwner.add(register.command);
                    break;
            }

            flag = true;
        }

        Collections.sort(commandsNormal, String::compareTo);
        Collections.sort(commandsMod, String::compareTo);
        Collections.sort(commandsAdmin, String::compareTo);
        Collections.sort(commandsOwner, String::compareTo);
    }

    @Command("commands")
    public void commands(CommandEvent commandEvent) {
        if (!flag) {
            registerCommands();
        }

        switch (commandEvent.getUser().getLevel()) {
            case NORMAL:
                commandEvent.respond(languageManager.getAsString("commands.text.available.commands", StringUtils.join(commandsNormal, ", ")));
                break;
            case MOD:
                commandEvent.respond(languageManager.getAsString("commands.text.available.commands", StringUtils.join(commandsMod, ", ")));
                break;
            case ADMIN:
                commandEvent.respond(languageManager.getAsString("commands.text.available.commands", StringUtils.join(commandsAdmin, ", ")));
                break;
            case OWNER:
                commandEvent.respond(languageManager.getAsString("commands.text.available.commands", StringUtils.join(commandsOwner, ", ")));
                break;
        }
    }

    @Override
    public void onInit(InitEvent initEvent) throws Exception {
        languageManager = new LanguageManager("tk.jomp16.plugin.resource.Strings");

        initEvent.addHelp(this, new HelpRegister("commands", languageManager.getAsString("commands.help.text")));
    }

    @Override
    public void onReset(ResetEvent resetEvent) throws Exception {
        reload();
    }

    public static void reload() {
        commandsNormal.clear();
        commandsMod.clear();
        commandsAdmin.clear();
        commandsOwner.clear();
        registerCommands();
    }
}
