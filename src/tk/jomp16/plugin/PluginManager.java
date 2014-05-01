/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin;

import com.google.gson.Gson;
import tk.jomp16.ui.MainUI;

import java.io.Closeable;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginManager implements Closeable {
    private List<Plugin> plugins = new ArrayList<>();
    private PluginLoader pluginLoader = new PluginLoader();
    private Gson gson;

    public PluginManager() {
        this.gson = new Gson();
    }

    @SuppressWarnings("ConstantConditions")
    public void loadAll() throws Exception {
        File f = new File(System.getProperty("user.dir").replace("\\", "/") + "/plugins");

        for (File file : f.listFiles()) {
            if (file.getName().endsWith(".jar")) {
                JarFile jarFile = new JarFile(file);
                JarEntry entry = jarFile.getJarEntry("plugin.json");

                if (entry != null) {
                    PluginInfo pluginInfo = gson.fromJson(getPluginInfoInputStream(file), PluginInfo.class);
                    Plugin plugin =
                            new Plugin(pluginInfo, pluginLoader.loadPluginEvent(file),
                                    MainUI.isGui() ? pluginLoader.loadPluginUI(file) : null);

                    plugins.add(plugin);
                }
            }
        }
    }

    private InputStreamReader getPluginInfoInputStream(File file) throws Exception {
        URL url = new URL("jar:file:" + file.getPath() + "!/plugin.json");

        return new InputStreamReader(url.openStream());
    }

    public List<Plugin> getPlugins() {
        return plugins;
    }

    @Override
    public void close() {
        pluginLoader.close();
    }
}
