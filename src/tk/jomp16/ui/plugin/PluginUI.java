/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.ui.plugin;

import javax.swing.*;

public interface PluginUI {
    public JPanel getJPanel();

    public default void showJFrame(JPanel jPanel, String title) {
        JFrame jFrame = new JFrame(title);
        jFrame.setContentPane(jPanel);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }

    public default JFrame createJFrame(JPanel jPanel, String title) {
        JFrame jFrame = new JFrame(title);
        jFrame.setContentPane(jPanel);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);

        return jFrame;
    }

    public default JFrame createAndShowJFrame(JPanel jPanel, String title) {
        JFrame jFrame = new JFrame(title);
        jFrame.setContentPane(jPanel);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);

        return jFrame;
    }
}
