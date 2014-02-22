/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin;

import tk.jomp16.irc.event.Event;

import java.util.List;

public class Plugin {
    private PluginInfo pluginInfo;
    private List<Event> events;

    public Plugin(PluginInfo pluginInfo, List<Event> events) {
        this.pluginInfo = pluginInfo;
        this.events = events;
    }

    public PluginInfo getPluginInfo() {
        return pluginInfo;
    }

    public List<Event> getEvents() {
        return events;
    }
}
