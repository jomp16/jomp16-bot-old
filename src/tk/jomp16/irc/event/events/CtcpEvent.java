/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.event.events;

import org.apache.logging.log4j.LogManager;
import tk.jomp16.irc.IRCManager;
import tk.jomp16.irc.channel.Channel;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.listener.event.DccFileSendReceivedEvent;
import tk.jomp16.irc.parser.parsers.CtcpParser;
import tk.jomp16.irc.user.User;

import java.time.Instant;

public class CtcpEvent extends Event {
    private IRCManager ircManager;
    private User user;
    private Channel channel;
    private CtcpParser.CtcpCommands ctcpCommands;
    private String raw;

    public CtcpEvent(IRCManager ircManager, User user, Channel channel, CtcpParser.CtcpCommands ctcpCommands, String raw) {
        this.ircManager = ircManager;
        this.user = user;
        this.channel = channel;
        this.ctcpCommands = ctcpCommands;
        this.raw = raw;
    }

    @Override
    public void respond() throws Exception {
        switch (ctcpCommands) {
            case TIME:
                ircManager.getOutputCTCP().sendResponseNotice(channel.getTargetName(), "TIME " + Instant.now());
                break;
            case DCC:
                String[] split = raw.replace("\001", "").substring(4).split(" ");
                String command = split[0];

                if (command.equals("SEND")) {
                    String fileName = split[1].replace("\"", "");
                    String ip = split[2];
                    String port = split[3];
                    String size = split[4];

                    System.out.println("----------------");
                    System.out.println(fileName);
                    System.out.println(ip);
                    System.out.println(port);
                    System.out.println(size);
                    System.out.println("----------------");

                    // TODO: REMOVE IT LATER!
//                    FileUtils.copyInputStreamToFile(new Socket(ip, Integer.parseInt(port)).getInputStream(), new File("DOWNLOADS/" + fileName));
                    Runnable runnable = () -> ircManager.getEvents().forEach((event) -> {
                        try {
                            DccFileSendReceivedEvent dccFileSendReceivedEvent = new DccFileSendReceivedEvent(ircManager, user, channel, null, LogManager.getLogger(event.getClass()));
                            dccFileSendReceivedEvent.setIp(ip);
                            dccFileSendReceivedEvent.setPort(Integer.parseInt(port));
                            dccFileSendReceivedEvent.setFileName(fileName);
                            dccFileSendReceivedEvent.setFileSize(Integer.parseInt(size));

                            event.onDccFileSendReceived(dccFileSendReceivedEvent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

                    ircManager.getExecutor().execute(runnable);
                }

                break;
            case PING:
                ircManager.getOutputCTCP().sendResponseNotice(user.getUserName(), raw.replace("\001", ""));
                break;
        }

        Runnable runnable = () -> ircManager.getEvents().forEach((event) -> {
            try {
                event.onCtcp(new tk.jomp16.irc.event.listener.event.CtcpEvent(ircManager, user, ctcpCommands, raw, LogManager.getLogger(event.getClass())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        ircManager.getExecutor().execute(runnable);
    }
}
