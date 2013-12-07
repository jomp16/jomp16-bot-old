/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.plugin.commands;

import org.apache.commons.lang3.StringUtils;
import tk.jomp16.irc.event.Command;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.events.PrivMsgEvent;
import tk.jomp16.irc.event.listener.CommandEvent;
import tk.jomp16.irc.event.listener.InitEvent;
import tk.jomp16.irc.event.listener.ResetEvent;
import tk.jomp16.irc.plugin.help.HelpRegister;

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

    @Command("commands")
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
    public void onInit(InitEvent initEvent) throws Exception {
        initEvent.addHelp(this, new HelpRegister("commands", "Return the available commands for you", "commands"));
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
