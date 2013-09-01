/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin;

import tk.jomp16.irc.event.CommandFilter;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.Level;
import tk.jomp16.irc.event.listener.CommandEvent;

public class Manager extends Event {
    // TODO: TERMINATE THIS SHIT
    @CommandFilter(value = "manager", level = Level.OWNER)
    public void manager(CommandEvent commandEvent) {
        if (commandEvent.getArgs().size() >= 2) {
            switch (commandEvent.getArgs().remove(0)) {
                case "add":
                    processAddCommand(commandEvent);
                    break;
                case "remove":
                    processRemoveCommand(commandEvent);
                    break;
                default:
                    commandEvent.respond("What you mean?");
                    break;
            }
        }
    }

    private void processAddCommand(CommandEvent commandEvent) {
        if (commandEvent.getArgs().size() >= 2) {
            switch (commandEvent.getArgs().remove(0)) {
                case "owner":
                    if (commandEvent.getArgs().get(0).contains("@")) {
                        commandEvent.getIrcManager().addOwner(commandEvent.getArgs().remove(0));
                        commandEvent.respond("Okay, added that user to owner rank");
                    }
                    break;
                case "admin":
                    if (commandEvent.getArgs().get(0).contains("@")) {
                        commandEvent.getIrcManager().addAdmin(commandEvent.getArgs().remove(0));
                        commandEvent.respond("Okay, added that user to admin rank");
                    }
                    break;
                case "mod":
                    if (commandEvent.getArgs().get(0).contains("@")) {
                        commandEvent.getIrcManager().addMod(commandEvent.getArgs().remove(0));
                        commandEvent.respond("Okay, added that user to mod rank");
                    }
                    break;
                default:

                    break;
            }
        }
    }

    private void processRemoveCommand(CommandEvent commandEvent) {
        if (commandEvent.getArgs().size() >= 2) {
            switch (commandEvent.getArgs().remove(0)) {
                case "owner":
                    if (commandEvent.getArgs().get(0).contains("@")) {
                        commandEvent.getIrcManager().getOwners().remove(commandEvent.getArgs().remove(0));
                        commandEvent.respond("Okay, removed the user in owner rank");
                    }
                    break;
                case "admin":
                    if (commandEvent.getArgs().get(0).contains("@")) {
                        commandEvent.getIrcManager().getAdmins().remove(commandEvent.getArgs().remove(0));
                        commandEvent.respond("Okay, removed the user in admin rank");
                    }
                    break;
                case "mod":
                    if (commandEvent.getArgs().get(0).contains("@")) {
                        commandEvent.getIrcManager().getMods().remove(commandEvent.getArgs().remove(0));
                        commandEvent.respond("Okay, removed the user in mod rank");
                    }
                    break;
                default:
                    commandEvent.respond("What you mean?");
                    break;
            }
        }
    }
}
