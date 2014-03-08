/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.ui.uis;

import tk.jomp16.irc.IRCManager;
import tk.jomp16.ui.plugin.PluginUI;

import javax.swing.*;

public class JoinChannel implements PluginUI {
    private JPanel joinChannelPanel;
    private JTextField channelToJoin;
    private JButton joinChannelButton;

    public JoinChannel(IRCManager ircManager) {
        joinChannelButton.addActionListener(e -> {
            if (channelToJoin.getText().length() != 0)
                ircManager.getOutputIRC().joinChannel(channelToJoin.getText());
        });
    }

    @Override
    public JPanel getJPanel() {
        return joinChannelPanel;
    }
}
