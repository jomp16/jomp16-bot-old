/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.play;

import tk.jomp16.irc.event.Command;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.listener.CommandEvent;
import tk.jomp16.irc.event.listener.InitEvent;
import tk.jomp16.play.database.PlayOpenHelper;
import tk.jomp16.sqlite1.SQLiteDatabase;

public class Play extends Event {
    private SQLiteDatabase database;

    @Command("play")
    public void play(CommandEvent commandEvent) {

    }

    @Override
    public void onInit(InitEvent initEvent) throws Exception {
        database = new PlayOpenHelper(initEvent.getPluginPath(this) + "/database.db", 1).getDatabase();
    }
}
