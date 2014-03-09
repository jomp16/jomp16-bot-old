/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.ui.plugin;

import tk.jomp16.irc.IRCManager;

import javax.swing.*;

// TODO
public class PluginList1 implements PluginUI {
    private JPanel pluginsListPanel;
    private JPanel pluginsListPanel1;
    private IRCManager ircManager;

    public PluginList1(IRCManager ircManager) {
        this.ircManager = ircManager;
    }

    private void createUIComponents() {
        pluginsListPanel1 = new JPanel();

        ircManager.getPlugins().parallelStream()
                .filter(pluginList -> pluginList.getPluginUIs().size() != 0)
                .forEach(pluginList -> pluginList.getPluginUIs().parallelStream()
                        .filter(pluginUI -> pluginUI.getJPanel() != null)
                        .forEach(stub -> {
                            JButton jButton = new JButton(pluginList.getPluginInfo().getName());
                            jButton.addActionListener(e ->
                                    showJFrame(new PluginList2(pluginList.getPluginUIs()).getJPanel(),
                                            pluginList.getPluginInfo().getName()));

                            pluginsListPanel1.add(jButton);
                        }));

        ircManager.getBundledPluginUIs().parallelStream()
                .filter(pluginUI -> pluginUI.getJPanel() != null)
                .forEach(pluginUI -> {
                    JButton jButton = new JButton(pluginUI.getClass().getSimpleName());
                    jButton.addActionListener(e -> showJFrame(pluginUI.getJPanel(), pluginUI.getClass().getSimpleName()));

                    pluginsListPanel1.add(jButton);
                });
    }

    @Override
    public JPanel getJPanel() {
        return pluginsListPanel;
    }
}
