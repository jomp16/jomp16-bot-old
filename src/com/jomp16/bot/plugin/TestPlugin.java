/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.bot.plugin;

import com.jomp16.irc.event.CommandFilter;
import com.jomp16.irc.event.Event;
import com.jomp16.irc.event.Level;
import com.jomp16.irc.event.listener.CommandEvent;
import com.jomp16.irc.event.listener.JoinEvent;

public class TestPlugin extends Event {
    public String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    @CommandFilter("lol")
    public void doSomething(CommandEvent commandEvent) {
        if (commandEvent.getArgs().size() > 0) {
            commandEvent.respond(commandEvent.getArgs().toString());
        } else {
            commandEvent.respond("='(", false);
        }
    }

    @CommandFilter("umad?")
    public void doSomething2(CommandEvent commandEvent) {
        commandEvent.respond("UMAD BRO?", true);
    }

    @CommandFilter("ram")
    public void doSomething3(CommandEvent commandEvent) {
        commandEvent.respond(humanReadableByteCount(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(), true));
    }

    @CommandFilter("ram2")
    public void doSomething4(CommandEvent commandEvent) {
        commandEvent.respond(humanReadableByteCount(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(), false));
    }

    @CommandFilter(value = "join", level = Level.ADMIN)
    public void doSomething5(CommandEvent commandEvent) {
        if (commandEvent.getArgs().size() > 0) {
            if (commandEvent.getArgs().get(0).startsWith("#")) {
                commandEvent.getIrcManager().getOutputIRC().joinChannel(commandEvent.getArgs().get(0));
            }
        } else {
            commandEvent.respond("Mmmmmmmmm");
        }
    }

    @CommandFilter(value = "say", level = Level.ADMIN)
    public void say(CommandEvent commandEvent) {
        if (commandEvent.getArgs().size() > 0) {
            if (commandEvent.getArgs().size() == 1) {
                commandEvent.respond(commandEvent.getArgs().get(0), false);
            } else if (commandEvent.getArgs().size() == 2) {
                if (commandEvent.getArgs().get(0).startsWith("#")) {
                    commandEvent.respond(commandEvent.getArgs().get(0), commandEvent.getArgs().get(1));
                } else {
                    commandEvent.respond(commandEvent.getChannel().getTargetName(), commandEvent.getArgs().get(0), commandEvent.getArgs().get(1));
                }
            } else if (commandEvent.getArgs().size() >= 3) {
                commandEvent.respond(commandEvent.getArgs().get(0), commandEvent.getArgs().get(1), commandEvent.getArgs().get(2));
            }
        }
    }

    @CommandFilter(value = "part", level = Level.ADMIN)
    public void part(CommandEvent commandEvent) {
        if (commandEvent.getArgs().size() > 0) {
            if (commandEvent.getArgs().size() == 1) {
                if (commandEvent.getArgs().get(0).startsWith("#")) {
                    commandEvent.getIrcManager().getOutputIRC().partChannel(commandEvent.getArgs().get(0));
                }
            } else if (commandEvent.getArgs().size() >= 2) {
                if (commandEvent.getArgs().get(0).startsWith("#")) {
                    commandEvent.getIrcManager().getOutputIRC().partChannel(commandEvent.getArgs().get(0), commandEvent.getArgs().get(1));
                }
            }
        } else {
            commandEvent.getChannelDAO().partChannel();
        }
    }

    @Override
    public void onJoin(JoinEvent joinEvent) {
        if (joinEvent.getIrcManager().getAdmins().contains(joinEvent.getUser().getCompleteHost())) {
            joinEvent.getChannelDAO().giveOP(joinEvent.getUser());
        } else {
            joinEvent.getChannelDAO().giveVoice(joinEvent.getUser());
        }
    }
}