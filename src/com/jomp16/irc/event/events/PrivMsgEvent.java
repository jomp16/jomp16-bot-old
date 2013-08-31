/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.event.events;

import com.jomp16.irc.IRCManager;
import com.jomp16.irc.Source;
import com.jomp16.irc.channel.Channel;
import com.jomp16.irc.event.CommandFilter;
import com.jomp16.irc.event.Event;
import com.jomp16.irc.event.Level;
import com.jomp16.irc.event.listener.CommandEvent;
import com.jomp16.irc.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrivMsgEvent extends Event {
    private static ArrayList<EventRegister> eventRegisters = new ArrayList<>();
    private static HashMap<String, Integer> spamLock = new HashMap<>();
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ss");
    private IRCManager ircManager;
    private User user;
    private String message;
    private Channel channel;
    private ArrayList<String> args = new ArrayList<>();
    private Logger log = LogManager.getLogger(this.getClass().getSimpleName());

    public PrivMsgEvent(IRCManager ircManager, User user, Channel channel, String message, PrivMSGTag tag) {
        this.ircManager = ircManager;
        this.user = user;
        this.message = message;
        this.channel = channel;

        switch (tag) {
            case NORMAL:
                runNormal(ircManager.getEvents());
                break;
            case PING:
                // TODO: see this
                break;
        }
    }

    public static void reloadEvents(ArrayList<Event> events) {
        eventRegisters.clear();
        registerArrayList(events);
    }

    public static ArrayList<EventRegister> getEventRegisters() {
        return eventRegisters;
    }

    private static void registerArrayList(ArrayList<Event> events) {
        for (Event event : events) {
            Method[] methods = event.getClass().getMethods();
            for (Method method : methods) {
                Annotation annotation = method.getAnnotation(CommandFilter.class);
                if (annotation != null) {
                    CommandFilter commandFilter = (CommandFilter) annotation;
                    for (String s : commandFilter.value()) {
                        EventRegister eventRegister = new EventRegister(s, event, commandFilter.level(), method);
                        eventRegisters.add(eventRegister);
                    }
                }
            }
        }
    }

    private void runNormal(ArrayList<Event> events) {
        parseLine(message);

        if (eventRegisters.size() == 0) {
            registerArrayList(events);
        }

        try {
            ircManager.getEvents().forEach((event) -> {
                try {
                    event.onPrivMsg(new com.jomp16.irc.event.listener.event.PrivMsgEvent(ircManager, user, channel, message, args, LogManager.getLogger(event.getClass().getSimpleName())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            invoke(eventRegisters, args, ircManager.getConfiguration().getPrefix());
        } catch (Exception e) {
            log.error("An error occurred: " + e.toString());
            e.printStackTrace();
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

    public void invoke(ArrayList<EventRegister> eventRegisters, ArrayList<String> args, String prefix) throws Exception {
        if (args.size() >= 1) {
            if (!isLocked()) {
                if (args.get(0).startsWith(prefix)) {
                    args.set(0, args.get(0).substring(1));

                    for (EventRegister eventRegister : eventRegisters) {
                        if (args.get(0).equals(eventRegister.command)) {
                            args.remove(0);

                            CommandEvent commandEvent = new CommandEvent(ircManager, user, channel, message, eventRegister.command, args, LogManager.getLogger(eventRegister.event.getClass().getSimpleName()));

                            Level level = Source.loopMask(ircManager, user.getCompleteRawLine());
                            switch (eventRegister.level) {
                                case NORMAL:
                                    invoke(eventRegister.method, eventRegister.event, commandEvent);
                                    break;
                                case MOD:
                                    if (level.equals(Level.MOD) || level.equals(Level.ADMIN) || level.equals(Level.OWNER)) {
                                        invoke(eventRegister.method, eventRegister.event, commandEvent);
                                    }
                                    break;
                                case ADMIN:
                                    if (level.equals(Level.ADMIN) || level.equals(Level.OWNER)) {
                                        invoke(eventRegister.method, eventRegister.event, commandEvent);
                                    }
                                    break;
                                case OWNER:
                                    if (level.equals(Level.OWNER)) {
                                        invoke(eventRegister.method, eventRegister.event, commandEvent);
                                    }
                                    break;
                            }

                            break;
                        }
                    }
                }
            }
        }
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

    private boolean isLocked() {
        int currentSec = Integer.parseInt(simpleDateFormat.format(new Date(System.currentTimeMillis())));
        if (spamLock.containsKey(this.user.getUserName())) {
            int timeLock = spamLock.get(this.user.getUserName());
            int timeOut = ircManager.getConfiguration().getCommandLock();
            if (timeLock > transform(currentSec + timeOut) || timeLock < transform(currentSec - timeOut)) {
                spamLock.replace(this.user.getUserName(), currentSec);
                return false;
            }
        } else {
            if (user.getLevel() == Level.OWNER || user.getLevel() == Level.ADMIN || user.getLevel() == Level.MOD) {
                return false;
            } else {
                spamLock.put(this.user.getUserName(), currentSec);
                return false;
            }
        }
        return true;
    }

    private void invoke(Method method, Event event, CommandEvent commandEvent) {
        Runnable runnable = () -> {
            try {
                method.invoke(event, commandEvent);
            } catch (Exception e) {
                log.error(e);
                e.printStackTrace();
            }
        };

        ircManager.getExecutor().execute(runnable);
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
        public Method method;
        public Event event;
        public Level level;

        public EventRegister(String command, Event event, Level level, Method method) {
            this.command = command;
            this.method = method;
            this.event = event;
            this.level = level;
        }
    }
}
