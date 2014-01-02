/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.logger;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

public class Logger {
    private Class<?> aClass;
    private String className;
    private static JTextArea textPane;
    private Properties logProperties;
    private ArrayList<String> show;

    public Logger(Class<?> aClass) throws Exception {
        this.aClass = aClass;
        this.logProperties = new Properties();
        this.show = new ArrayList<>();

        this.logProperties.load(new FileInputStream("log.properties"));

        Collections.addAll(this.show, logProperties.getProperty("show").split(","));

        if (textPane != null) {
            DefaultCaret caret = (DefaultCaret) textPane.getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        }
    }

    public Logger(String className) throws Exception {
        this.className = className;
        this.logProperties = new Properties();
        this.show = new ArrayList<>();

        this.logProperties.load(new FileInputStream("log.properties"));

        Collections.addAll(this.show, logProperties.getProperty("show").split(","));

        if (textPane != null) {
            DefaultCaret caret = (DefaultCaret) textPane.getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        }
    }

    public Logger(Class<?> aClass, JTextArea textPane) throws Exception {
        this.aClass = aClass;
        Logger.textPane = textPane;
        this.logProperties = new Properties();
        this.show = new ArrayList<>();

        this.logProperties.load(new FileInputStream("log.properties"));

        Collections.addAll(this.show, logProperties.getProperty("show").split(","));

        if (textPane != null) {
            DefaultCaret caret = (DefaultCaret) textPane.getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        }
    }

    public Logger(String className, JTextArea textPane) throws Exception {
        this.className = className;
        Logger.textPane = textPane;
        this.logProperties = new Properties();
        this.show = new ArrayList<>();

        this.logProperties.load(new FileInputStream("log.properties"));

        Collections.addAll(this.show, logProperties.getProperty("show").split(","));

        if (textPane != null) {
            DefaultCaret caret = (DefaultCaret) textPane.getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        }
    }

    public void log(Object obj) {
        if (show.contains("log") || show.contains("all")) {
            String tmp = logProperties.getProperty("format")
                    .replace("%level", "LOG  ")
                    .replace("%className", aClass != null ? aClass.getSimpleName() : className)
                    .replace("%message", obj.toString())
                    .replace("%n", "\n");

            System.out.print(tmp);

            if (textPane != null) {
                textPane.setText(textPane.getText() + tmp);
            }
        }
    }

    public void debug(Object obj) {
        if (show.contains("debug") || show.contains("all")) {
            String tmp = logProperties.getProperty("format")
                    .replace("%level", "DEBUG")
                    .replace("%className", aClass != null ? aClass.getSimpleName() : className)
                    .replace("%message", obj.toString())
                    .replace("%n", "\n");

            System.out.print(tmp);

            if (textPane != null) {
                textPane.setText(textPane.getText() + tmp);
            }
        }
    }

    public void trace(Object obj) {
        if (show.contains("trace") || show.contains("all")) {
            String tmp = logProperties.getProperty("format")
                    .replace("%level", "TRACE")
                    .replace("%className", aClass != null ? aClass.getSimpleName() : className)
                    .replace("%message", obj.toString())
                    .replace("%n", "\n");

            System.out.print(tmp);

            if (textPane != null) {
                textPane.append(tmp);
            }
        }
    }

    public void error(Object obj) {
        if (show.contains("error") || show.contains("all")) {
            String tmp = logProperties.getProperty("format")
                    .replace("%level", "ERROR")
                    .replace("%className", aClass != null ? aClass.getSimpleName() : className)
                    .replace("%message", obj.toString())
                    .replace("%n", "\n");

            System.err.print(tmp);

            if (textPane != null) {
                textPane.append(tmp);
            }
        }
    }

    public void error(Throwable throwable) {
        if (show.contains("error") || show.contains("all")) {
            StringWriter writer = null;

            if (Boolean.parseBoolean(logProperties.getProperty("errorPrintStackTrace"))) {
                writer = new StringWriter();
                throwable.printStackTrace(new PrintWriter(writer));
            }

            String tmp = logProperties.getProperty("format")
                    .replace("%level", "ERROR")
                    .replace("%className", aClass != null ? aClass.getSimpleName() : className)
                    .replace("%message", (writer != null) ? "Stack trace:\n" + writer.toString() : "Exception in class: " + throwable.getClass().getSimpleName() + "; Exception: " + throwable.getMessage())
                    .replace("%n", "\n");

            System.err.print(tmp);

            if (textPane != null) {
                textPane.setText(textPane.getText() + tmp);
            }
        }
    }

    public void info(Object obj) {
        if (show.contains("info") || show.contains("all")) {
            String tmp = logProperties.getProperty("format")
                    .replace("%level", "INFO ")
                    .replace("%className", aClass != null ? aClass.getSimpleName() : className)
                    .replace("%message", obj.toString())
                    .replace("%n", "\n");

            System.out.print(tmp);

            if (textPane != null) {
                textPane.setText(textPane.getText() + tmp);
            }
        }
    }
}
