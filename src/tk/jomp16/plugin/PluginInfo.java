/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin;

public class PluginInfo {
    private String pluginName;
    private String pluginAuthor;
    private double version;

    public String getPluginName() {
        return pluginName;
    }

    public String getPluginAuthor() {
        return pluginAuthor;
    }

    public double getVersion() {
        return version;
    }
}
