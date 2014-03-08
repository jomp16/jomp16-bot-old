/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tk.jomp16.bot.BotMain;
import tk.jomp16.bot.plugin.FunCommandsPlugin;
import tk.jomp16.bot.plugin.TestPlugin;
import tk.jomp16.configuration.Configuration;
import tk.jomp16.irc.IRCManager;
import tk.jomp16.language.LanguageManager;
import tk.jomp16.logger.TextAreaAppender;
import tk.jomp16.sqlite_old.SQLiteManager;
import tk.jomp16.sqlite_old.configurator.SQLite_Configurator;
import tk.jomp16.ui.plugin.PluginList1;
import tk.jomp16.ui.plugin.PluginUI;
import tk.jomp16.ui.uis.JoinChannel;
import tk.jomp16.utils.Utils;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainUI implements PluginUI {
    private static Logger log = LogManager.getLogger(BotMain.class);
    private static IRCManager ircManager;
    private JPanel mainUI;
    private JTextArea consoleOutput;
    private JScrollPane consoleScrollPane;
    private JTextField commandTextField;
    private JTabbedPane tabbedPane1;
    private JTable channelsTable;
    private JButton showRAMUsageButton;
    private JButton joinChannelButton;

    public MainUI() throws Exception {
        TextAreaAppender.jTextArea = consoleOutput;
        TextAreaAppender.jScrollPane = consoleScrollPane;

        // Menu Start
        JMenuBar jMenuBar = new JMenuBar();
        JMenu jMenu = new JMenu("Arquivos");
        JMenuItem jMenuItem = new JMenuItem("Sobre");
        jMenuItem.addActionListener(e -> JOptionPane.showMessageDialog(null, "Hello World!"));

        jMenu.add(jMenuItem);
        jMenuBar.add(jMenu);
        // Menu End

        initIRCBot();

        tabbedPane1.setComponentAt(1, new PluginList1(ircManager).getJPanel());

        JFrame frame = new JFrame("MainUI");
        frame.setContentPane(mainUI);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setJMenuBar(jMenuBar);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        showRAMUsageButton.addActionListener(e -> JOptionPane.showMessageDialog(null, Utils.getRamUsage()));
        joinChannelButton.addActionListener(e -> createJFrame(new JoinChannel(ircManager).getJPanel(), "Join channel"));
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0 || args.length >= 1 && !args[0].equals("nogui")) {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            new MainUI();
        } else {
            initIRCBot();
        }

        ircManager.connect();
        ircManager.getOutputIRC().joinChannel("#jomp16-bot");
    }

    public static void initIRCBot() throws Exception {
        // TODO: CHANGE TO SQLITEOPENHELPER

        SQLiteManager sqLiteManager = new SQLiteManager("database");

        try {
            sqLiteManager.getPreparedStatement("SELECT * FROM bot_config").close();
        } catch (SQLException e) {
            // If the sql fails, it is because the database is new
            new SQLite_Configurator();
        }

        LanguageManager languageManager = new LanguageManager("tk.jomp16.resource.Strings");

        log.trace(languageManager.getAsString("welcome", System.getProperty("user.name")));

        ResultSet ircConf = sqLiteManager.getResultSet("SELECT * FROM bot_config");

        ircManager = new IRCManager(new Configuration.Builder()
                .setNick(ircConf.getString("nick"))
                .setRealName(ircConf.getString("realName"))
                .setPassword(ircConf.getString("password"))
                .setPrefix(ircConf.getString("prefix"))
                .setIdentify(ircConf.getString("identify"))
                .setServer(ircConf.getString("server"))
                .setVerbose(true)
                .buildConfiguration());

        ircManager.registerEvent(new TestPlugin(), true);
        ircManager.registerEvent(new FunCommandsPlugin(), true);

        ResultSet resultSet = sqLiteManager.getResultSet("SELECT * FROM owners");
        while (resultSet.next()) {
            ircManager.addOwner(resultSet.getString("mask"));
        }
    }

    @Override
    public JPanel getJPanel() {
        return null;
    }
}
