/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.ui.sql;

import tk.jomp16.sqlite.SQLiteDatabase;
import tk.jomp16.ui.plugin.PluginUI;

import javax.swing.*;
import java.sql.SQLException;

public class ChannelConfigurator implements PluginUI {
    private JPanel panel1;
    private JTable channelTable;
    private JButton okButton;
    private JButton addButton;
    private JButton removeButton;
    private ChannelTableModel channelTableModel;

    public ChannelConfigurator(SQLiteDatabase sqLiteDatabase) {
        channelTableModel = new ChannelTableModel();
        channelTable.setModel(channelTableModel);

        addButton.addActionListener(e -> showJFrame(new AddThingForm(channelTableModel).getJPanel(), "Add channel"));

        removeButton.addActionListener(e -> channelTableModel.remove(channelTable.getSelectedRow()));

        okButton.addActionListener(e -> {
            if (channelTableModel.getChannels().size() != 0) {
                channelTableModel.getChannels().parallelStream().forEach(channel -> {
                    try {
                        sqLiteDatabase.insertData("joinChannels", channel);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                });

                JOptionPane.showMessageDialog(null, "Saved channels to database!");
                BotConfigurator.jFrame.dispose();
            }
        });
    }

    @Override
    public JPanel getJPanel() {
        return panel1;
    }
}
