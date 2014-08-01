/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin;

import com.google.gson.Gson;
import org.apache.commons.codec.digest.DigestUtils;
import tk.jomp16.irc.IRCManager;
import tk.jomp16.ui.MainUI;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class PluginManager implements Closeable {
    private List<Plugin> plugins = new ArrayList<>();
    private PluginLoader pluginLoader = new PluginLoader();
    private Gson gson;
    private IRCManager ircManager;

    public PluginManager(IRCManager ircManager) {
        this.ircManager = ircManager;
        this.gson = new Gson();
    }

    @SuppressWarnings("ConstantConditions")
    public void loadAll() throws Exception {
        File f = new File("plugins");

        for (File file : f.listFiles()) {
            if (file.getName().endsWith(".jar")) {
                System.out.println(file.getName());

                JarFile jarFile = new JarFile(file);
                JarEntry entry = jarFile.getJarEntry("plugin.json");

                if (entry != null) {
                    PluginInfo pluginInfo = gson.fromJson(getPluginInfoInputStream(file), PluginInfo.class);
                    Plugin plugin =
                            new Plugin(pluginInfo, DigestUtils.md5Hex(new FileInputStream(file)),
                                    pluginLoader.loadPluginEvent(file),
                                    MainUI.isGui() ? pluginLoader.loadPluginUI(file) : null);

                    plugins.add(plugin);

                    ircManager.registerPluginEvent(plugin);
                }
            }
        }
    }

    public boolean loadPlugin(String pluginName) throws Exception {
        File f = new File("plugins/" + pluginName + ".jar");

        if (f.exists()) {
            JarFile jarFile = new JarFile(f);
            JarEntry entry = jarFile.getJarEntry("plugin.json");

            if (entry != null) {
                PluginInfo pluginInfo = gson.fromJson(getPluginInfoInputStream(f), PluginInfo.class);
                Plugin plugin =
                        new Plugin(pluginInfo, DigestUtils.md5Hex(new FileInputStream(f)),
                                pluginLoader.loadPluginEvent(f),
                                MainUI.isGui() ? pluginLoader.loadPluginUI(f) : null);

                if (plugins.parallelStream().filter(plugin1 -> plugin1.getPluginInfo().getName().equals(plugin.getPluginInfo().getName())).count() != 0 &&
                        plugins.parallelStream().filter(plugin1 -> plugin1.getMd5sums().equals(plugin.getMd5sums())).count() != 0) {
                    return false;
                }

                plugins.add(plugin);

                ircManager.registerPluginEvent(plugin);

                return true;
            }
        }

        return false;
    }

    public boolean reloadPlugin(String pluginName) throws Exception {
        File f = new File("plugins/" + pluginName + ".jar");

        if (f.exists()) {
            JarFile jarFile = new JarFile(f);
            JarEntry entry = jarFile.getJarEntry("plugin.json");

            if (entry != null) {
                PluginInfo pluginInfo = gson.fromJson(getPluginInfoInputStream(f), PluginInfo.class);
                Plugin plugin =
                        new Plugin(pluginInfo, DigestUtils.md5Hex(new FileInputStream(f)),
                                pluginLoader.loadPluginEvent(f),
                                MainUI.isGui() ? pluginLoader.loadPluginUI(f) : null);

                if (plugins.parallelStream().filter(plugin1 -> plugin1.getPluginInfo().getName().equals(plugin.getPluginInfo().getName())).count() != 0 &&
                        plugins.parallelStream().filter(plugin1 -> plugin1.getMd5sums().equals(plugin.getMd5sums())).count() == 0) {
                    for (Plugin plugin1 : plugins.parallelStream().filter(plugin2 -> plugin2.getPluginInfo().getName().equals(plugin.getPluginInfo().getName())).collect(Collectors.toList())) {
                        pluginLoader.closePluginClassLoader(pluginName);

                        plugins.remove(plugin1);
                        plugins.add(plugin);

                        ircManager.unregisterPluginEvent(plugin1);
                        ircManager.registerPluginEvent(plugin);
                    }

                    return true;
                }
            }
        }

        return false;
    }

    public boolean unloadPlugin(String pluginName) throws Exception {
        if (plugins.parallelStream().filter(plugin -> plugin.getPluginInfo().getName().equals(pluginName)).count() != 0) {
            for (Plugin plugin1 : plugins.parallelStream().filter(plugin -> plugin.getPluginInfo().getName().equals(pluginName)).collect(Collectors.toList())) {
                pluginLoader.closePluginClassLoader(pluginName);
                plugins.remove(plugin1);
                ircManager.unregisterPluginEvent(plugin1);
            }

            return true;
        }

        return false;
    }

    private InputStreamReader getPluginInfoInputStream(File file) throws Exception {
        URL url = new URL("jar:file:" + file.getPath() + "!/plugin.json");

        return new InputStreamReader(url.openStream());
    }

    public List<Plugin> getPlugins() {
        return plugins;
    }

    public PluginLoader getPluginLoader() {
        return pluginLoader;
    }

    @Override
    public void close() {
        pluginLoader.close();
    }
}
