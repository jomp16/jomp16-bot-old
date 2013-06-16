/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.plugin;

import com.jomp16.irc.event.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Properties;

public class PluginLoader {
    private Logger log = LogManager.getLogger(this.getClass().getSimpleName());

    public ArrayList<Event> load() throws Exception {
        ArrayList<Event> events = new ArrayList<>();

        try {
            File f = new File(System.getProperty("user.dir").replace("\\", "/") + "/plugins");
            for (File file : f.listFiles()) {
                if (file.getName().endsWith(".jar")) {
                    URL[] urls = new URL[]{file.toURI().toURL()};
                    ClassLoader classLoader = this.getClass().getClassLoader();

                    try (URLClassLoader urlClassLoader = new URLClassLoader(urls, classLoader)) {
                        URL url = new URL("jar:file:" + file.getAbsolutePath() + "!/plugin.properties");
                        Properties properties = new Properties();
                        properties.load(url.openStream());

                        Class<? extends Event> eventClass = Class.forName(properties.getProperty("MainClass"), true, urlClassLoader).asSubclass(Event.class);
                        Constructor<? extends Event> eventConstructor = eventClass.getConstructor();

                        events.add(eventConstructor.newInstance());
                    }
                }
            }
        } catch (Exception e) {
            log.error(e);
        }

        return events;
    }
}
