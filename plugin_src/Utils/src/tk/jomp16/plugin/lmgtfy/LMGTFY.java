/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.lmgtfy;

import tk.jomp16.irc.event.CommandFilter;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.listener.CommandEvent;
import tk.jomp16.irc.event.listener.InitEvent;
import tk.jomp16.irc.plugin.help.HelpRegister;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class LMGTFY extends Event {
    private String URL = "http://lmgtfy.com/?q=%s";

    @CommandFilter("lmgtfy")
    public void lmgtfy(CommandEvent commandEvent) throws UnsupportedEncodingException {
        if (commandEvent.getArgs().size() >= 2) {
            String formmated = String.format(URL, URLEncoder.encode(commandEvent.getArgs().get(1), "UTF-8"));

            commandEvent.respond(commandEvent.getArgs().get(0), formmated);
        } else {
            commandEvent.showUsage(this, commandEvent.getCommand());
        }
    }

    @Override
    public void onInit(InitEvent initEvent) throws Exception {
        initEvent.addHelp(this, new HelpRegister("lmgtfy", "Generate a lmgtfy link for a stupid user", "user 'term'"));
    }
}
