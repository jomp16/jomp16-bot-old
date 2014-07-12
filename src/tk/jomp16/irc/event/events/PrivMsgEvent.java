/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.event.events;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tk.jomp16.irc.IRCManager;
import tk.jomp16.irc.Source;
import tk.jomp16.irc.channel.Channel;
import tk.jomp16.irc.channel.ChannelDAO;
import tk.jomp16.irc.event.Command;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.Level;
import tk.jomp16.irc.event.listener.event.CommandEvent;
import tk.jomp16.irc.user.User;
import tk.jomp16.plugin.PluginInfo;
import tk.jomp16.plugin.commands.Commands;
import tk.jomp16.plugin.help.Help;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PrivMsgEvent extends Event {
    public static IRCManager ircManager;
    private static List<EventRegister> eventRegisters = new ArrayList<>();
    private static Map<String, Integer> spamLock = new HashMap<>();
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ss");
    private User user;
    private String message;
    private Channel channel;
    private PrivMSGTag tag;
    private List<String> args = new ArrayList<>();
    private Logger log = LogManager.getLogger(this.getClass());

    public PrivMsgEvent(IRCManager ircManager, User user, Channel channel, String message, PrivMSGTag tag) {
        PrivMsgEvent.ircManager = ircManager;
        this.user = user;
        this.message = message;
        this.channel = channel;
        this.tag = tag;
    }

    public static List<EventRegister> getEventRegisters() {
        return eventRegisters;
    }

    public static void addCommandsFromEvent(Event event) {
        Method[] methods = event.getClass().getDeclaredMethods();

        for (Method method : methods) {
            Annotation annotation = method.getAnnotation(Command.class);
            if (annotation != null) {
                method.setAccessible(true);

                Command command = (Command) annotation;

                EventRegister eventRegister;
                PluginInfo pluginInfo = ircManager.getPluginInfoHashMap().get(event.getClass().getSimpleName());

                if (pluginInfo != null) {
                    eventRegister =
                            new EventRegister(command.value(), command.optCommands(), command.args(), pluginInfo, event, command.level(), method);
                } else {
                    eventRegister =
                            new EventRegister(command.value(), command.optCommands(), command.args(), event, command.level(), method);
                }

                eventRegisters.add(eventRegister);
                Commands.addCommand(eventRegister);
            }
        }
    }

    public static void removeCommandsFromEventName(Event event) {
        eventRegisters.parallelStream().filter(eventRegister -> eventRegister.event.equals(event)).collect(Collectors.toList()).forEach(eventRegister -> {
            eventRegisters.remove(eventRegister);
            Commands.removeCommand(eventRegister);
        });

        Help.removeHelp(event);
    }

    @Override
    public void respond() throws Exception {
        Runnable runnable = () -> {
            switch (tag) {
                case NORMAL:
                case ACTION:
                    runNormalAndAction();
                    break;
                case PING:
                    // TODO: see this
                    break;
            }
        };

        ircManager.getExecutor().execute(runnable);
    }

    private void runNormalAndAction() {
        parseLine(message);

        try {
            invoke(eventRegisters, args, ircManager.getConfiguration().getPrefix());
        } catch (Exception e) {
            log.error("An error occurred", e);
        }
    }

    public void parseLine(String message) {
        Matcher matcher = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'").matcher(message);

        while (matcher.find()) {
            if (matcher.group(1) != null) {
                // Add double-quoted string without the quotes
                args.add(matcher.group(1));
            } else if (matcher.group(2) != null) {
                // Add single-quoted string without the quotes
                args.add(matcher.group(2));
            } else {
                // Add unquoted word
                args.add(matcher.group());
            }
        }
    }

    public void invoke(List<EventRegister> eventRegisters, List<String> args, String prefix) throws Exception {
        if (args.size() >= 1) {
            if (args.get(0).startsWith(prefix)) {
                args.set(0, args.get(0).substring(1));

                String command = null;
                EventRegister eventRegister = null;

                for (EventRegister eventRegister1 : eventRegisters) {
                    if (args.get(0).equals(eventRegister1.command)) {
                        eventRegister = eventRegister1;
                        command = eventRegister1.command;
                        break;
                    } else {
                        for (String optCommand : eventRegister1.optCommands) {
                            if (args.get(0).equals(optCommand)) {
                                eventRegister = eventRegister1;
                                command = optCommand;
                                break;
                            }
                        }
                    }
                }

                if (eventRegister != null && command.length() != 0) {
                    args.remove(0);

                    String messageWithoutCommand =
                            message.substring(message.substring(1).length() > command.length()
                                    ? command.length() + 2
                                    : message.length());

                    OptionParser parser = new OptionParser();
                    parser.allowsUnrecognizedOptions();

                    for (String arg : eventRegister.args) {
                        if (arg.endsWith("::")) {
                            parser.accepts(arg.substring(0, arg.length() - 2)).withOptionalArg();
                        } else if (arg.endsWith(":")) {
                            parser.accepts(arg.substring(0, arg.length() - 1)).withRequiredArg();
                        } else {
                            parser.accepts(arg);
                        }
                    }

                    OptionSet optionSet = parser.parse(args);
                    CommandEvent commandEvent;

                    if (eventRegister.pluginInfo != null) {
                        commandEvent = new CommandEvent(ircManager, user, channel,
                                new ChannelDAO(ircManager, channel),
                                LogManager.getLogger(eventRegister.event.getClass()),
                                eventRegister.pluginInfo);

                        commandEvent.setMessage(messageWithoutCommand);
                        commandEvent.setArgs(args);
                        commandEvent.setCommand(command);
                        commandEvent.setOptionSet(optionSet);
                        commandEvent.setRawMessage(message);
                    } else {
                        commandEvent = new CommandEvent(ircManager, user, channel,
                                new ChannelDAO(ircManager, channel),
                                LogManager.getLogger(eventRegister.event.getClass()));

                        commandEvent.setMessage(messageWithoutCommand);
                        commandEvent.setArgs(args);
                        commandEvent.setCommand(command);
                        commandEvent.setOptionSet(optionSet);
                        commandEvent.setRawMessage(message);
                    }

                    Level level = Source.loopMask(ircManager, user.getCompleteRawLine());
                    switch (eventRegister.level) {
                        case NORMAL:
                            invoke(eventRegister.method, eventRegister.event, commandEvent);
                            break;
                        case MOD:
                            if (level.equals(Level.MOD)
                                    || level.equals(Level.ADMIN)
                                    || level.equals(Level.OWNER)) {
                                invoke(eventRegister.method, eventRegister.event, commandEvent);
                            }
                            break;
                        case ADMIN:
                            if (level.equals(Level.ADMIN)
                                    || level.equals(Level.OWNER)) {
                                invoke(eventRegister.method, eventRegister.event, commandEvent);
                            }
                            break;
                        case OWNER:
                            if (level.equals(Level.OWNER)) {
                                invoke(eventRegister.method, eventRegister.event, commandEvent);
                            }
                            break;
                    }
                }
            } else {
                execPrivAction();
            }
        } else {
            execPrivAction();
        }
    }

    private void execPrivAction() throws Exception {
        ircManager.getEvents().forEach((event) -> {
            try {
                tk.jomp16.irc.event.listener.event.PrivMsgEvent privMsgEvent =
                        new tk.jomp16.irc.event.listener.event.PrivMsgEvent(ircManager, user, channel,
                                new ChannelDAO(ircManager, channel), LogManager.getLogger(event.getClass()));
                privMsgEvent.setArgs(args);
                privMsgEvent.setMessage(message);
                privMsgEvent.setTag(tag);

                event.onPrivMsg(privMsgEvent);
            } catch (Exception e) {
                log.error(e, e);
            }
        });
    }

    private int transform(int i) {
        if (i >> 31 != 0) {
            i = i * -1;
        }

        if (i > 60) {
            return 60;
        }

        return i;
    }

    // TODO: I THINK THIS ISN'T WORKING
    private boolean isLocked() {
        int currentSec = Integer.parseInt(simpleDateFormat.format(new Date(System.currentTimeMillis())));

        if (spamLock.containsKey(this.user.getUserName())) {
            int timeLock = spamLock.get(this.user.getUserName());
            int timeOut = ircManager.getConfiguration().getCommandLock();

            if (timeLock > transform(currentSec + timeOut) && timeLock < transform(currentSec - timeOut)) {
                spamLock.replace(this.user.getUserName(), currentSec);
                return false;
            }
        } else {
            if (user.getLevel() == Level.OWNER
                    || user.getLevel() == Level.ADMIN
                    || user.getLevel() == Level.MOD) {
                return false;
            } else {
                spamLock.put(this.user.getUserName(), currentSec);
                return false;
            }
        }
        return true;
    }

    private void invoke(Method method, Event event, CommandEvent commandEvent) {
        try {
            method.invoke(event, commandEvent);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    public enum PrivMSGTag {
        VERSION,
        ACTION,
        PING,
        TIME,
        FINGER,
        DCC,
        NORMAL
    }

    public static class EventRegister {
        public String command;
        public String[] optCommands;
        public String[] args;
        public Method method;
        public Event event;
        public Level level;
        public PluginInfo pluginInfo;

        public EventRegister(String command, String[] optCommands, String[] args, Event event, Level level, Method method) {
            this.command = command;
            this.optCommands = optCommands;
            this.args = args;
            this.method = method;
            this.event = event;
            this.level = level;
        }

        public EventRegister(String command, String[] optCommands, String[] args, PluginInfo pluginInfo, Event event, Level level, Method method) {
            this.command = command;
            this.optCommands = optCommands;
            this.args = args;
            this.method = method;
            this.pluginInfo = pluginInfo;
            this.event = event;
            this.level = level;
        }
    }
}
