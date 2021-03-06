/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.bot.plugin;

import tk.jomp16.irc.channel.ChannelList;
import tk.jomp16.irc.event.Command;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.Level;
import tk.jomp16.irc.event.listener.event.CommandEvent;
import tk.jomp16.utils.Utils;

public class TestPlugin extends Event {
    @Command("ram")
    public void doSomething3(CommandEvent commandEvent) {
        commandEvent.respond(Utils.getRamUsage());
    }

    @Command(value = "join", level = Level.ADMIN)
    public void doSomething5(CommandEvent commandEvent) {
        if (commandEvent.getArgs().size() > 0) {
            if (commandEvent.getArgs().get(0).startsWith("#")) {
                commandEvent.getIrcManager().getOutputIRC().joinChannel(commandEvent.getArgs().get(0));
            }
        } else {
            commandEvent.respond("Mmmmmmmmm");
        }
    }

    @Command(value = "say", level = Level.ADMIN)
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

    @Command(value = "part", level = Level.ADMIN)
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

    @Command("showUsers")
    public void showUsers(CommandEvent commandEvent) {
        if (commandEvent.getArgs().size() >= 1) {
            commandEvent.respond("Size of list: " + ChannelList.getListUsers(commandEvent.getArgs().get(0)).keySet().size());
        } else {
            //commandEvent.respond(StringUtils.join(commandEvent.getChannel().getAllUsersWithLevel().keySet(), ", "));
            commandEvent.respond("Size of list: " + commandEvent.getChannel().getAllUsersWithLevel().keySet().size());
            commandEvent.respond(commandEvent.getChannel().getAllUsers());
        }
    }

    @Command("testMessage")
    public void testRawMessage(CommandEvent commandEvent) {
        commandEvent.respond(commandEvent.getMessage());
        commandEvent.respond(commandEvent.getRawMessage());
    }

    @Command("ctcp")
    public void ctcp(CommandEvent commandEvent) {
        if (commandEvent.getArgs().size() >= 2) {
            commandEvent.getIrcManager().getOutputCTCP().sendResponseNotice(commandEvent.getArgs().get(0), commandEvent.getArgs().get(1));
        }
    }
}
