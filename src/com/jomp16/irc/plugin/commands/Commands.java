/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.plugin.commands;

import com.jomp16.irc.event.CommandFilter;
import com.jomp16.irc.event.Event;
import com.jomp16.irc.event.events.PrivMsgEvent;
import com.jomp16.irc.event.listener.CommandEvent;
import com.jomp16.irc.event.listener.ResetEvent;
import com.sun.deploy.util.StringUtils;

import java.util.ArrayList;

public class Commands extends Event {
    private ArrayList<String> commandsNormal = new ArrayList<>();
    private ArrayList<String> commandsMod = new ArrayList<>();
    private ArrayList<String> commandsAdmin = new ArrayList<>();
    private ArrayList<String> commandsOwner = new ArrayList<>();
    private boolean flag = false;

    private void registerCommands() {
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
    }

    @CommandFilter("commands")
    public void commands(CommandEvent commandEvent) {
        if (!flag) {
            registerCommands();
        }

        switch (commandEvent.getUser().getLevel()) {
            case NORMAL:
                commandEvent.respond("Available commands: " + StringUtils.join(commandsNormal, ", "));
                break;
            case MOD:
                commandEvent.respond("Available commands: " + StringUtils.join(commandsMod, ", "));
                break;
            case ADMIN:
                commandEvent.respond("Available commands: " + StringUtils.join(commandsAdmin, ", "));
                break;
            case OWNER:
                commandEvent.respond("Available commands: " + StringUtils.join(commandsOwner, ", "));
                break;
        }
    }

    @Override
    public void onReset(ResetEvent resetEvent) throws Exception {
        commandsNormal.clear();
        commandsMod.clear();
        commandsAdmin.clear();
        commandsOwner.clear();
        registerCommands();
    }
}
