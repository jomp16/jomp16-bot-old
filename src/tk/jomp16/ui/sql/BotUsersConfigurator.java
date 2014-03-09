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

public class BotUsersConfigurator implements PluginUI {
    private JPanel botUsersConfigurator;
    private JTabbedPane tabbedPane1;
    private JTable ownerTable;
    private JTable adminTable;
    private JTable modTable;
    private JButton okButton;
    private JButton addButton;
    private JButton removeButton;
    private UsersTableModel ownerTableModel;
    private UsersTableModel adminTableModel;
    private UsersTableModel modTableModel;

    public BotUsersConfigurator(SQLiteDatabase sqLiteDatabase) {
        ownerTableModel = new UsersTableModel();
        adminTableModel = new UsersTableModel();
        modTableModel = new UsersTableModel();

        ownerTable.setModel(ownerTableModel);
        adminTable.setModel(adminTableModel);
        modTable.setModel(modTableModel);

        addButton.addActionListener(e -> {
            switch(tabbedPane1.getSelectedIndex()) {
                case 0:
                    showJFrame(new AddThingForm(ownerTableModel).getJPanel(), "Add owner");
                    break;
                case 1:
                    showJFrame(new AddThingForm(adminTableModel).getJPanel(), "Add admin");
                    break;
                case 2:
                    showJFrame(new AddThingForm(modTableModel).getJPanel(), "Add mod");
                    break;
            }
        });

        removeButton.addActionListener(e -> {
            switch(tabbedPane1.getSelectedIndex()) {
                case 0:
                    ownerTableModel.remove(ownerTable.getSelectedRow());
                    break;
                case 1:
                    adminTableModel.remove(adminTable.getSelectedRow());
                    break;
                case 2:
                    modTableModel.remove(modTable.getSelectedRow());
                    break;
            }
        });

        okButton.addActionListener(e -> {
            if (ownerTableModel.getHostMasks().size() != 0) {
                ownerTableModel.getHostMasks().parallelStream().forEach(hostMask -> {
                    try {
                        sqLiteDatabase.insertData("owners", hostMask);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                });
            }

            if (adminTableModel.getHostMasks().size() != 0) {
                adminTableModel.getHostMasks().parallelStream().forEach(hostMask -> {
                    try {
                        sqLiteDatabase.insertData("admins", hostMask);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                });
            }

            if (modTableModel.getHostMasks().size() != 0) {
                modTableModel.getHostMasks().parallelStream().forEach(hostMask -> {
                    try {
                        sqLiteDatabase.insertData("mods", hostMask);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                });
            }

            JOptionPane.showMessageDialog(null, "Saved users to database!");
            BotConfigurator.jFrame.dispose();
        });
    }

    @Override
    public JPanel getJPanel() {
        return botUsersConfigurator;
    }


}
