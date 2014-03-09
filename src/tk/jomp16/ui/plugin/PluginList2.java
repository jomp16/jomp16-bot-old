/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.ui.plugin;

import javax.swing.*;
import java.util.List;

public class PluginList2 implements PluginUI {
    private JPanel pluginsListPanel1;
    private JPanel pluginsListPanel;
    private List<PluginUI> pluginUIs;
    private PluginUI pluginUI;

    public PluginList2(List<PluginUI> pluginUIs) {
        this.pluginUIs = pluginUIs;
    }

    public PluginList2(PluginUI pluginUI) {
        this.pluginUI = pluginUI;
    }

    @Override
    public JPanel getJPanel() {
        return pluginsListPanel;
    }

    private void createUIComponents() {
        pluginsListPanel1 = new JPanel();

        if (pluginUIs != null) {
            pluginUIs.parallelStream().filter(pluginUI -> pluginUI.getJPanel() != null)
                    .forEach(pluginUI -> {
                        JButton jButton = new JButton(pluginUI.getClass().getSimpleName());
                        jButton.addActionListener(e -> showJFrame(pluginUI.getJPanel(), pluginUI.getClass().getSimpleName()));

                        pluginsListPanel1.add(jButton);
                    });
        } else if (pluginUI != null) {
            JButton jButton = new JButton(pluginUI.getClass().getSimpleName());
            jButton.addActionListener(e -> showJFrame(pluginUI.getJPanel(), pluginUI.getClass().getSimpleName()));

            pluginsListPanel1.add(jButton);
        }
    }
}
