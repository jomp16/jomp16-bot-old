/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.event.listener.event;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import tk.jomp16.irc.IRCManager;
import tk.jomp16.irc.channel.Channel;
import tk.jomp16.irc.channel.ChannelDAO;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.listener.Listener;
import tk.jomp16.irc.user.User;
import tk.jomp16.plugin.PluginInfo;

import java.io.File;
import java.net.Socket;

public class DccFileSendReceivedEvent extends Listener {
    private String ip;
    private int port;
    private String fileName;
    private int fileSize;

    public DccFileSendReceivedEvent(IRCManager ircManager, Logger log) {
        super(ircManager, log);
    }

    public DccFileSendReceivedEvent(IRCManager ircManager, User user, Channel channel, ChannelDAO channelDAO, Logger log, PluginInfo pluginInfo) {
        super(ircManager, user, channel, channelDAO, log, pluginInfo);
    }

    public DccFileSendReceivedEvent(IRCManager ircManager, User user, Channel channel, ChannelDAO channelDAO, Logger log) {
        super(ircManager, user, channel, channelDAO, log);
    }


    public void acceptFile(Event event) throws Exception {
        Socket socket = new Socket(ip, port);
        FileUtils.copyInputStreamToFile(socket.getInputStream(), new File(getFilePluginPath(event, "DOWNLOADS"), fileName));
        socket.close();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }
}
