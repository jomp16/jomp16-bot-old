/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.plugin;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import tk.jomp16.irc.event.Event;
import tk.jomp16.logger.LogManager;
import tk.jomp16.logger.Logger;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Set;

@SuppressWarnings("ConstantConditions")
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
                    URLClassLoader urlClassLoader = new URLClassLoader(urls, classLoader);
                    Reflections reflections = new Reflections(new ConfigurationBuilder().addUrls(new URL("file:" + file.getPath())));
                    Set<String> classes = reflections.getStore().getSubTypesOf(Event.class.getName());

                    for (String s : classes) {
                        Class<? extends Event> eventClass = Class.forName(s, true, urlClassLoader).asSubclass(Event.class);
                        Constructor<? extends Event> eventConstructor = eventClass.getConstructor();

                        Event event = eventConstructor.newInstance();

                        events.add(event);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e);
        }

        return events;
    }

    public ArrayList<Event> load(File pluginFile) throws Exception {
        ArrayList<Event> events = new ArrayList<>();

        URL[] urls = new URL[]{pluginFile.toURI().toURL()};
        ClassLoader classLoader = this.getClass().getClassLoader();
        URLClassLoader urlClassLoader = new URLClassLoader(urls, classLoader);

        Reflections reflections = new Reflections(new ConfigurationBuilder().addUrls(new URL("file:" + pluginFile.getPath())));
        Set<String> classes = reflections.getStore().getSubTypesOf(Event.class.getName());

        for (String s : classes) {
            Class<? extends Event> eventClass = Class.forName(s, true, urlClassLoader).asSubclass(Event.class);
            Constructor<? extends Event> eventConstructor = eventClass.getConstructor();

            Event event = eventConstructor.newInstance();

            events.add(event);
        }

        return events;
    }
}
