/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.output;

import tk.jomp16.irc.IRCManager;
import tk.jomp16.utils.Utils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class OutputCTCP {
    private final IRCManager ircManager;

    public OutputCTCP(IRCManager ircManager) {
        this.ircManager = ircManager;
    }

    public void sendAction(Object target, Object action) {
        ircManager.getOutputRaw().writeRaw("PRIVMSG " + target + " :\001ACTION " + action + "\001");
    }

    public void sendTime(Object target) {
        ircManager.getOutputRaw().writeRaw("PRIVMSG " + target + " :\001TIME\001");
    }

    public void sendResponsePrivMsgEvent(Object target, Object response) {
        ircManager.getOutputRaw().writeRaw("PRIVMSG " + target + " :\001" + response + "\001");
    }

    public void sendResponseNotice(Object target, Object response) {
        ircManager.getOutputRaw().writeRaw("NOTICE " + target + " :\001" + response + "\001");
    }

    public void sendFile(Object target, File f) throws Exception {
        // todo: DCC RESUME
        // PRIVMSG jomp16-bot :DCC SEND 768_multirom_dar.zip 0 51089 1098669
        ServerSocket serverSocket = new ServerSocket(0, 1);

        Timer timer = new Timer(300000, e -> {
            if (!serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        timer.setRepeats(false);
        timer.start();
        sendResponsePrivMsgEvent(target, "DCC SEND \"" + f.getName() + "\" 0 " + serverSocket.getLocalPort() + " " + f.length());
        Socket socket = serverSocket.accept();
        serverSocket.close();

        Utils.sendDCCFile(socket, f, 0);
    }
}
