/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.event.events;

import com.jomp16.irc.IRCManager;
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
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrivMsgEvent extends Event {
    private static ArrayList<EventRegister> eventRegisters = new ArrayList<>();
    private IRCManager ircManager;
    private User user;
    private String message;
    private Channel channel;
    private ArrayList<String> args = new ArrayList<>();
    private Logger log = LogManager.getLogger(this.getClass().getSimpleName());

    public PrivMsgEvent(IRCManager ircManager, User user, Channel channel, String message, ArrayList<Event> events) {
        this.ircManager = ircManager;
        this.user = user;
        this.message = message;
        this.channel = channel;

        parseLine(message);

        if (eventRegisters.size() == 0) {
            registerArrayList(events);
        }

        try {
            invoke(eventRegisters, args, ircManager.getConfiguration().getPrefix());
        } catch (Exception e) {
            log.error("An error occurred: " + e);
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

    private void registerArrayList(ArrayList<Event> events) {
        for (Event event : events) {
            Method[] methods = event.getClass().getMethods();
            for (Method method : methods) {
                Annotation annotation = method.getAnnotation(CommandFilter.class);
                if (annotation != null) {
                    CommandFilter commandFilter = (CommandFilter) annotation;
                    EventRegister eventRegister = new EventRegister(commandFilter.value(), event, commandFilter.level(), method);
                    eventRegisters.add(eventRegister);
                }
            }
        }
    }

    public void invoke(ArrayList<EventRegister> eventRegisters, ArrayList<String> args, String prefix) throws Exception {
        if (args.get(0).startsWith(prefix)) {
            args.set(0, args.get(0).substring(1));
            for (EventRegister eventRegister : eventRegisters) {
                if (args.get(0).equals(eventRegister.command)) {
                    args.remove(0);
                    CommandEvent commandEvent = new CommandEvent(ircManager, user, channel, message, eventRegister.event, args, LogManager.getLogger(eventRegister.event.getClass().getSimpleName()));
                    switch (eventRegister.level) {
                        case NORMAL:
                            eventRegister.method.invoke(eventRegister.event, commandEvent);
                            break;
                        case MOD:
                            if (ircManager.getMods().contains(user.getCompleteHost())) {
                                eventRegister.method.invoke(eventRegister.event, commandEvent);
                            } else {
                                System.out.println(user.getCompleteHost());
                                ircManager.getOutputIRC().sendMessage(channel.getTargetName(), user.getUserName(), "No enough permission to run that command =(");
                            }
                            break;
                        case ADMIN:
                            if (ircManager.getAdmins().contains(user.getCompleteHost())) {
                                eventRegister.method.invoke(eventRegister.event, commandEvent);
                            } else {
                                System.out.println(user.getCompleteHost());
                                ircManager.getOutputIRC().sendMessage(channel.getTargetName(), user.getUserName(), "No enough permission to run that command =(");
                            }
                            break;
                    }
                    break;
                }
            }
        }
    }

    private class EventRegister {
        private String command;
        private Method method;
        private Event event;
        private Level level;

        public EventRegister(String command, Event event, Level level, Method method) {
            this.command = command;
            this.method = method;
            this.event = event;
            this.level = level;
        }
    }
}
