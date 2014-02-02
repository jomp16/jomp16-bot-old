// Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
// This work is free. You can redistribute it and/or modify it under the
// terms of the Do What The Fuck You Want To Public License, Version 2,
// as published by Sam Hocevar. See the COPYING file for more details.

package tk.jomp16.plugin.notes;

import tk.jomp16.irc.event.Command;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.listener.CommandEvent;
import tk.jomp16.irc.event.listener.DisableEvent;
import tk.jomp16.irc.event.listener.InitEvent;
import tk.jomp16.irc.event.listener.ResetEvent;
import tk.jomp16.irc.plugin.help.HelpRegister;
import tk.jomp16.plugin.notes.database.NotesOpenHelper;
import tk.jomp16.sqlite1.SQLiteDatabase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Notes extends Event {
    private SQLiteDatabase database;
    private SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
    private ArrayList<NotesRegister> notes = new ArrayList<>();
    private int DATABASE_VERSION = 1;

    @Override
    public void onInit(InitEvent initEvent) throws Exception {
        initEvent.addHelp(this, new HelpRegister("notes", "Save/see a note for you or for a user", "add/see (when is add, usage is add (user for give a message to a user) or 'title' 'message' (to register and you will see it)"));

        database = new NotesOpenHelper(initEvent.getPluginPath(this) + "/database.db", DATABASE_VERSION).getDatabase();

        ResultSet resultSet = database.getResultSet("SELECT * FROM notes");

        while (resultSet.next()) {
            notes.add(new NotesRegister(resultSet.getString("user"), resultSet.getString("title"), resultSet.getString("message"), resultSet.getString("date"), resultSet.getString("time")));
        }
    }

    @Override
    public void onReset(ResetEvent resetEvent) throws Exception {
        notes.clear();

        ResultSet resultSet = database.getResultSet("SELECT * FROM notes");

        while (resultSet.next()) {
            notes.add(new NotesRegister(resultSet.getString("user"), resultSet.getString("title"), resultSet.getString("message"), resultSet.getString("date"), resultSet.getString("time")));
        }
    }

    @Override
    public void onDisable(DisableEvent disableEvent) throws Exception {
        notes.clear();
        database.close();
    }

    @Command("notes")
    public void onNotes(CommandEvent commandEvent) throws SQLException {
        if (commandEvent.getArgs().size() >= 1) {
            if (commandEvent.getArgs().get(0).equals("add")) {
                if (commandEvent.getArgs().size() >= 3) {
                    if (commandEvent.getArgs().get(1).equals("user")) {
                        // TODO
                    } else {
                        String tmpTitle = commandEvent.getArgs().get(1);
                        String tmpMessage = commandEvent.getArgs().get(2);
                        Date dat = new Date();
                        String date = formatDate.format(dat);
                        String time = formatTime.format(dat);

                        database.insertData("notes", commandEvent.getUser().getUserName(), tmpTitle, tmpMessage, date, time);
                        notes.add(new NotesRegister(commandEvent.getUser().getUserName(), tmpTitle, tmpMessage, date, time));
                    }
                }
            } else if (commandEvent.getArgs().get(0).equals("see")) {
                if (commandEvent.getArgs().size() >= 2) {
                    if (commandEvent.getArgs().get(1).equals("all")) {
                        String tmp = "";
                        int tmp1 = 0;
                        for (NotesRegister register : notes) {
                            tmp1++;

                            if (register.getUser().equals(commandEvent.getUser().getUserName())) {
                                tmp += "ID: " + tmp1 + " | Title: " + register.getTitle() + " | Date: " + register.getDate() + " | Time: " + register.getTime() + " || ";
                            }
                        }

                        if (tmp.length() != 0) {
                            commandEvent.respond(tmp.substring(0, tmp.length() - 4));
                        } else {
                            commandEvent.respond("No notes available for you =(");
                        }
                    } else {
                        try {
                            Integer tmpInt = Integer.parseInt(commandEvent.getArgs().get(1));

                            ArrayList<NotesRegister> tmpNotes = new ArrayList<>();

                            for (NotesRegister register : notes) {
                                if (register.getUser().equals(commandEvent.getUser().getUserName())) {
                                    tmpNotes.add(register);
                                }
                            }

                            if (tmpInt - 1 >= tmpNotes.size()) {
                                commandEvent.respond("Integer great than note size!");
                            } else {
                                NotesRegister register = tmpNotes.get(tmpInt - 1);

                                commandEvent.respond("Title: " + register.getTitle() + " | Message: " + register.getMessage());
                            }
                        } catch (Exception e) {
                            commandEvent.respond("Not a integer!");
                        }
                    }
                }
            } else if (commandEvent.getArgs().get(0).equals("clear")) {
                if (commandEvent.getArgs().size() >= 2) {
                    if (commandEvent.getArgs().get(1).equals("all")) {
                        database.executeFastUpdateQuery("DELETE FROM notes WHERE user = ?", commandEvent.getUser().getUserName());
                        ArrayList<NotesRegister> tmpNotes = new ArrayList<>();

                        for (NotesRegister register : notes) {
                            if (register.getUser().equals(commandEvent.getUser().getUserName())) {
                                tmpNotes.add(register);
                            }
                        }

                        notes.removeAll(tmpNotes);
                    } else {
                        try {
                            Integer tmpInt = Integer.parseInt(commandEvent.getArgs().get(1));

                            if (tmpInt - 1 >= notes.size()) {
                                commandEvent.respond("Integer great than note size!");
                            } else {
                                NotesRegister register = notes.get(tmpInt - 1);

                                if (register.getUser().equals(commandEvent.getUser().getUserName())) {
                                    notes.remove(register);
                                    database.executeFastUpdateQuery("DELETE FROM notes WHERE user = ? AND title = ? AND message = ? AND date = ? AND time = ?", register.getUser(), register.getTitle(), register.getMessage(), register.getDate(), register.getTime());
                                }
                            }
                        } catch (Exception e) {
                            commandEvent.respond("Not a integer!");
                        }
                    }
                }
            }
        }
    }
}
