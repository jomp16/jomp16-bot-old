/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.help;

import tk.jomp16.irc.event.Level;

public class HelpRegister {
    private String command;
    private String[] optCommands;
    private String help;
    private String usage;
    private Level level;

    /**
     * Help class for register a help
     *
     * @param command the command
     * @param help    the description/help of the command
     * @param usage   the usage of the command, like (command doSomething "Hello World!")
     */
    public HelpRegister(String command, String[] optCommands, String help, String usage) {
        this.command = command;
        this.optCommands = optCommands;
        this.help = help;
        this.usage = usage;
        this.level = Level.NORMAL;
    }

    public HelpRegister(String command, String help, String usage) {
        this.command = command;
        this.help = help;
        this.usage = usage;
        this.level = Level.NORMAL;
    }

    public HelpRegister(String command, String[] optCommands, String help) {
        this.command = command;
        this.optCommands = optCommands;
        this.help = help;
        this.level = Level.NORMAL;
    }

    public HelpRegister(String command, String help) {
        this.command = command;
        this.help = help;
        this.level = Level.NORMAL;
    }

    /**
     * Help class for register a help
     *
     * @param command the command
     * @param help    the description/help of the command
     * @param usage   the usage of the command, like (command doSomething "Hello World!")
     * @param level   the level who the command need for level greater than NORMAL
     */
    public HelpRegister(String command, String[] optCommands, String help, String usage, Level level) {
        this.command = command;
        this.optCommands = optCommands;
        this.help = help;
        this.usage = usage;
        this.level = level;
    }

    public HelpRegister(String command, String help, String usage, Level level) {
        this.command = command;
        this.help = help;
        this.usage = usage;
        this.level = level;
    }

    public HelpRegister(String command, String[] optCommands, String help, Level level) {
        this.command = command;
        this.optCommands = optCommands;
        this.help = help;
        this.level = level;
    }

    public HelpRegister(String command, String help, Level level) {
        this.command = command;
        this.help = help;
        this.level = level;
    }

    /**
     * Get the command
     *
     * @return the command
     */
    public String getCommand() {
        return command;
    }

    public String[] getOptCommands() {
        return optCommands;
    }

    /**
     * Return the description/help of the command
     *
     * @return the description/help of the command
     */
    public String getHelp() {
        return help;
    }

    /**
     * Return the usage of the command
     *
     * @return the usage of the command
     */
    public String getUsage() {
        return usage;
    }

    /**
     * Return the level of the command
     *
     * @return the level of the command
     */
    public Level getLevel() {
        return level;
    }
}
