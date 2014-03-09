/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.ui.sql;

import tk.jomp16.bot.database.BotOpenHelper;
import tk.jomp16.sqlite.SQLiteDatabase;
import tk.jomp16.ui.plugin.PluginUI;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class BotConfigurator implements PluginUI {
    private JTextField nickNameTextField;
    private JTextField realNameTextField;
    private JTextField identifyTextField;
    private JTextField serverTextField;
    private JTextField prefixTextField;
    private JButton nextButton;
    private JPanel botConfiguratorPanel;
    private JPasswordField passwordField;
    public static JFrame jFrame;

    public BotConfigurator(SQLiteDatabase sqLiteDatabase) {
        JOptionPane.showMessageDialog(null, "Because it's is your first start, you need to setup your bot");

        jFrame = createAndShowJFrame(this.getJPanel(), "Bot Configurator");

        nextButton.addActionListener(e -> {
            if (nickNameTextField.getText() != null &&
                    realNameTextField.getText() != null &&
                    identifyTextField.getText() != null &&
                    serverTextField.getText() != null &&
                    prefixTextField.getText() != null) {
                try {
                    sqLiteDatabase.insertData("bot_config", nickNameTextField.getText(),
                            realNameTextField.getText(),
                            passwordField.getText() != null ? passwordField.getText() : "null",
                            identifyTextField.getText(),
                            serverTextField.getText(),
                            prefixTextField.getText());
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Some of theses textboxs are null!");
            }

            jFrame.dispose();
            showUsersThing(sqLiteDatabase);
        });
    }

    private void showUsersThing(SQLiteDatabase sqLiteDatabase) {
        JOptionPane.showMessageDialog(null, "Now you will add users to database");
        jFrame = createAndShowJFrame(new BotUsersConfigurator(sqLiteDatabase).getJPanel(), "Add users");

        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                showChannelThing(sqLiteDatabase);
            }
        });
    }

    private void showChannelThing(SQLiteDatabase sqLiteDatabase) {
        JOptionPane.showMessageDialog(null, "Now you will add channels to your bot join automatically");
        jFrame = createAndShowJFrame(new ChannelConfigurator(sqLiteDatabase).getJPanel(), "Channels");

        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                BotOpenHelper.wait = false;
            }
        });
    }

    @Override
    public JPanel getJPanel() {
        return botConfiguratorPanel;
    }
}
