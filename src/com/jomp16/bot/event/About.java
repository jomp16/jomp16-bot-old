package com.jomp16.bot.event;

import com.jomp16.irc.event.CommandFilter;
import com.jomp16.irc.event.Event;
import com.jomp16.irc.event.listener.CommandEvent;

public class About extends Event {
    @CommandFilter("about")
    public void about(CommandEvent commandEvent) {
        commandEvent.respond("jomp16-bot written by jomp16, with help (code and tips) from nebkat, licensed under WTFPL, source code at https://github.com/jomp16/jomp16-bot");
    }
}
