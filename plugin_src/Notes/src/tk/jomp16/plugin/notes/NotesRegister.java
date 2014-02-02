/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.notes;

public class NotesRegister {
    private String user;
    private String title;
    private String message;
    private String date;
    private String time;

    public NotesRegister(String user, String title, String message, String date, String time) {
        this.user = user;
        this.title = title;
        this.message = message;
        this.date = date;
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
