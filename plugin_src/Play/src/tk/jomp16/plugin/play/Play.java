/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.play;

import tk.jomp16.irc.event.Command;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.Level;
import tk.jomp16.irc.event.listener.event.CommandEvent;
import tk.jomp16.irc.event.listener.event.InitEvent;
import tk.jomp16.irc.event.listener.event.PrivMsgEvent;
import tk.jomp16.language.LanguageManager;
import tk.jomp16.plugin.play.database.PlayOpenHelper;
import tk.jomp16.sqlite.SQLiteDatabase;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class Play extends Event {
    private SQLiteDatabase database;
    private Map<String, PlayRegister> playRegisterHashMap = new HashMap<>();
    private LanguageManager languageManager;

    @Command("play")
    public void play(CommandEvent commandEvent) throws Exception {
        if (commandEvent.getArgs().size() > 0) {
            switch (commandEvent.getArgs().get(0)) {
                case "record":
                    if (commandEvent.getArgs().size() >= 2) {
                        if (!playRegisterHashMap.containsKey(commandEvent.getUser().getUserName())) {
                            database.insertData("musics", null, commandEvent.getArgs().get(1), commandEvent.getUser().getUserName());

                            ResultSet resultSet = database.getResultSet("SELECT * FROM musics ORDER BY musicID DESC");

                            PlayRegister playRegister = new PlayRegister(resultSet.getInt("musicID"), commandEvent.getArgs().get(1), commandEvent.getUser().getUserName());
                            playRegisterHashMap.put(commandEvent.getUser().getUserName(), playRegister);

                            commandEvent.respond("Now recording content for music: " + commandEvent.getArgs().get(1));
                        }
                    }
                    break;
                case "stop":
                    if (playRegisterHashMap.containsKey(commandEvent.getUser().getUserName())) {
                        playRegisterHashMap.remove(commandEvent.getUser().getUserName());

                        commandEvent.respond("Stopped recording!");
                    }
                    break;
                case "delete":
                    if (commandEvent.getArgs().size() >= 2) {
                        String musicName = commandEvent.getArgs().get(1);
                        String authorName = commandEvent.getUser().getUserName();
                        int musicID;

                        ResultSet resultSet = database.getResultSet("SELECT * FROM musics WHERE author = ? AND musicName = ?", authorName, musicName);

                        if (resultSet.next()) {
                            musicID = resultSet.getInt("musicID");

                            database.executeFastUpdateQuery("DELETE FROM musics WHERE musicID = ?", musicID);
                            database.executeFastUpdateQuery("DELETE FROM records WHERE musicID = ?", musicID);

                            commandEvent.respond("Deleted!");
                        } else {
                            commandEvent.respond("Hey bro, or this music doesn't exists, or you aren't the author of teh music! So... GTFO mate!");
                        }
                    } else if (commandEvent.getArgs().size() >= 3) {
                        if (commandEvent.getUser().getLevel().equals(Level.MOD)
                                || commandEvent.getUser().getLevel().equals(Level.ADMIN)
                                || commandEvent.getUser().getLevel().equals(Level.OWNER)) {
                            String musicName = commandEvent.getArgs().get(1);
                            String authorName = commandEvent.getArgs().get(2);
                            int musicID;

                            ResultSet resultSet = database.getResultSet("SELECT * FROM musics WHERE author = ? AND musicName = ?", authorName, musicName);

                            if (resultSet.next()) {
                                musicID = resultSet.getInt("musicID");

                                database.executeFastUpdateQuery("DELETE FROM musics WHERE musicID = ?", musicID);
                                database.executeFastUpdateQuery("DELETE FROM records WHERE musicID = ?", musicID);

                                commandEvent.respond("Deleted!");
                            } else {
                                commandEvent.respond("No result found");
                            }
                        }
                    }
                    break;
                default:
                    if (commandEvent.getArgs().size() >= 2) {
                        String musicName = commandEvent.getArgs().get(0);
                        String authorName = commandEvent.getArgs().get(1);

                        ResultSet resultSet = database.getResultSet("SELECT * from musics JOIN records ON records.musicID = musics.musicID WHERE musics.musicName = ? AND musics.author = ?", musicName, authorName);

                        if (resultSet.next()) {
                            commandEvent.respond("Starting to plugin: '" + musicName + "' from " + authorName, false);

                            commandEvent.respond(resultSet.getString("content"), false);
                            while (resultSet.next()) {
                                commandEvent.respond(resultSet.getString("content"), false);
                            }
                        } else {
                            commandEvent.respond("No music found with that name and/or the music isn't from this author!");
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onPrivMsg(PrivMsgEvent privMsgEvent) throws Exception {
        if (playRegisterHashMap.containsKey(privMsgEvent.getUser().getUserName())) {
            PlayRegister playRegister = playRegisterHashMap.get(privMsgEvent.getUser().getUserName());

            database.insertData("records", playRegister.getMusicID(), privMsgEvent.getMessage());
        }
    }

    @Override
    public void onInit(InitEvent initEvent) throws Exception {
        database = new PlayOpenHelper(initEvent.getPluginPath(this) + "/database.db", 1).getDatabase();

        languageManager = initEvent.getLanguageManager(this, "tk.jomp16.plugin.play.resource.Strings");
    }
}
