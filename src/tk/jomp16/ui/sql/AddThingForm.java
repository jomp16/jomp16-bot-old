/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.ui.sql;

import tk.jomp16.ui.plugin.PluginUI;

import javax.swing.*;

public class AddThingForm implements PluginUI {
    private JTextField textField1;
    private JButton button1;
    private JLabel label;
    private JPanel addThingPanel;

    public AddThingForm(ChannelTableModel channelTableModel) {
        label.setText("Channel");

        button1.addActionListener(e -> {
            if (textField1.getText() != null) {
                channelTableModel.add(textField1.getText());
            }
        });
    }

    public AddThingForm(UsersTableModel usersTableModel) {
        label.setText("User host mask");

        button1.addActionListener(e -> {
            if (textField1.getText() != null) {
                usersTableModel.add(textField1.getText());
            }
        });
    }

    @Override
    public JPanel getJPanel() {
        return addThingPanel;
    }
}
