/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.play;

public class PlayRegister {
    private int musicID;
    private String musicName;
    private String author;

    public PlayRegister(int musicID, String musicName, String author) {
        this.musicID = musicID;
        this.musicName = musicName;
        this.author = author;
    }

    public int getMusicID() {
        return musicID;
    }

    public String getMusicName() {
        return musicName;
    }

    public String getAuthor() {
        return author;
    }
}
