/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.logger;

import javax.swing.*;

public class LogManager {
    public static Logger getLogger(Class<?> aClass) {
        try {
            return new Logger(aClass);
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static Logger getLogger(String className) {
        try {
            return new Logger(className);
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static Logger getLogger(Class<?> aClass, JTextArea jTextPane) {
        try {
            return new Logger(aClass, jTextPane);
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}
