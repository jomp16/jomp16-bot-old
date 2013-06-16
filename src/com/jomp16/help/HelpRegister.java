/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.help;

import com.jomp16.irc.event.Level;

public class HelpRegister {
    private String command;
    private String help;
    private String usage;
    private Level level;

    public HelpRegister(String command, String help, String usage, Level level) {
        this.command = command;
        this.help = help;
        this.usage = usage;
        this.level = level;
    }

    public String getCommand() {
        return command;
    }

    public String getHelp() {
        return help;
    }

    public String getUsage() {
        return usage;
    }

    public Level getLevel() {
        return level;
    }
}
