/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.basicfileserver;

import tk.jomp16.irc.event.Command;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.listener.event.CommandEvent;
import tk.jomp16.irc.event.listener.event.DccFileSendReceivedEvent;

import java.io.File;

public class BasicFileServer extends Event {
    @Override
    public void onDccFileSendReceived(DccFileSendReceivedEvent dccFileSendReceivedEvent) throws Exception {
        dccFileSendReceivedEvent.acceptFile(this);
    }

    @Command(value = "download", args = {"file:"})
    public void download(CommandEvent commandEvent) throws Exception {
        // DCC SEND "454756.jpg" 0 37491 91819
        if (commandEvent.getOptionSet().has("file")) {
            File f = new File(commandEvent.getFilePluginPath(this, "DOWNLOADS"), (String) commandEvent.getOptionSet().valueOf("file"));

            if (f.exists()) {
                commandEvent.getIrcManager().getOutputCTCP().sendFile(commandEvent.getChannel().getTargetName(), f);
            } else {
                commandEvent.respond("No file found!");
            }
        }
    }
}
