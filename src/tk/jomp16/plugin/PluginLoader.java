/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import tk.jomp16.irc.event.Event;
import tk.jomp16.ui.plugin.PluginUI;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

@SuppressWarnings("ConstantConditions")
public class PluginLoader implements Closeable {
    private Logger log = LogManager.getLogger(this.getClass());
    private Map<String, URLClassLoader> urlClassLoaders = new HashMap<>();

    public List<Event> loadPluginEvent(File pluginFile) throws Exception {
        if (pluginFile.getName().endsWith(".jar")) {
            List<Event> events = new ArrayList<>();

            URL[] urls = new URL[]{pluginFile.toURI().toURL()};
            URLClassLoader urlClassLoader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());
            urlClassLoaders.put(pluginFile.getName().replace(".jar", ""), urlClassLoader);

            Reflections reflections = new Reflections(new ConfigurationBuilder().addUrls(new URL("file:" + pluginFile.getPath())));
            Set<String> classes = reflections.getStore().getSubTypesOf(Event.class.getName());

            for (String s : classes) {
                Class<? extends Event> eventClass = Class.forName(s, true, urlClassLoader).asSubclass(Event.class);
                Constructor<? extends Event> eventConstructor = eventClass.getConstructor();

                Event event = eventConstructor.newInstance();

                events.add(event);
            }

            return events;
        } else {
            throw new UnsupportedOperationException("File isn't .jar!");
        }
    }

    public List<PluginUI> loadPluginUI(File pluginFile) throws Exception {
        if (pluginFile.getName().endsWith(".jar")) {
            List<PluginUI> pluginUIs = new ArrayList<>();

            URL[] urls = new URL[]{pluginFile.toURI().toURL()};
            URLClassLoader urlClassLoader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());
            urlClassLoaders.put(pluginFile.getName().replace(".jar", ""), urlClassLoader);

            Reflections reflections = new Reflections(new ConfigurationBuilder().addUrls(new URL("file:" + pluginFile.getPath())));
            Set<String> classes = reflections.getStore().getSubTypesOf(PluginUI.class.getName());

            for (String s : classes) {
                Class<? extends PluginUI> eventClass = Class.forName(s, true, urlClassLoader).asSubclass(PluginUI.class);
                Constructor<? extends PluginUI> eventConstructor = eventClass.getConstructor();

                PluginUI pluginUI = eventConstructor.newInstance();

                pluginUIs.add(pluginUI);
            }

            return pluginUIs;
        } else {
            throw new UnsupportedOperationException("File isn't .jar!");
        }
    }


    public void closePluginClassLoader(String pluginName) throws Exception {
        if (urlClassLoaders.containsKey(pluginName)) {
            urlClassLoaders.get(pluginName).close();
            urlClassLoaders.remove(pluginName);
        }
    }

    @Override
    public void close() {
        for (URLClassLoader urlClassLoader : urlClassLoaders.values()) {
            try {
                urlClassLoader.close();
            } catch (IOException e) {
                log.error(e, e);
            }
        }

        urlClassLoaders.clear();
    }
}
