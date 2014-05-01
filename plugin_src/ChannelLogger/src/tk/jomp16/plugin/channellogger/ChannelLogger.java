/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.channellogger;

import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.listener.event.*;
import tk.jomp16.plugin.channellogger.database.ChannelOpenHelper;
import tk.jomp16.sqlite.SQLiteDatabase;

public class ChannelLogger extends Event {
    private SQLiteDatabase sqLiteDatabase;

    @Override
    public void onInit(InitEvent initEvent) throws Exception {
        sqLiteDatabase = new ChannelOpenHelper(initEvent.getStringPluginPath(this, "database.db"), 1).getDatabase();
    }

    @Override
    public void onJoin(JoinEvent joinEvent) throws Exception {
        sqLiteDatabase.insertData("logs", joinEvent.getChannel().getTargetName(), System.currentTimeMillis(), "JOIN", "", joinEvent.getUser().getUserName());
    }

    @Override
    public void onPrivMsg(PrivMsgEvent privMsgEvent) throws Exception {
        sqLiteDatabase.insertData("logs", privMsgEvent.getChannel().getTargetName(), System.currentTimeMillis(), "PRIVMSG", privMsgEvent.getMessage(), privMsgEvent.getUser().getUserName());
    }

    @Override
    public void onPart(PartEvent partEvent) throws Exception {
        sqLiteDatabase.insertData("logs", partEvent.getChannel().getTargetName(), System.currentTimeMillis(), "PART", "", partEvent.getUser().getUserName());
    }

    @Override
    public void onNick(NickEvent nickEvent) throws Exception {
        sqLiteDatabase.insertData("logs", nickEvent.getChannel().getTargetName(), System.currentTimeMillis(), "NICK", nickEvent.getOldNick(), nickEvent.getNewNick());
    }

    @Override
    public void onKick(KickEvent kickEvent) throws Exception {
        sqLiteDatabase.insertData("logs", kickEvent.getChannel().getTargetName(), System.currentTimeMillis(), "KICK", kickEvent.getUserKicked() + "||" + kickEvent.getReason(), kickEvent.getUser().getUserName());
    }

    @Override
    public void onMode(ModeEvent modeEvent) throws Exception {
        sqLiteDatabase.insertData("logs", modeEvent.getChannel().getTargetName(), System.currentTimeMillis(), "MODE", modeEvent.getUserModed() + "||" + modeEvent.getMode().toString(), modeEvent.getUser().getUserName());
    }

    @Override
    public void onQuit(QuitEvent quitEvent) throws Exception {
        sqLiteDatabase.insertData("logs", quitEvent.getChannel().getTargetName(), System.currentTimeMillis(), "QUIT", quitEvent.getReason(), quitEvent.getUser().getUserName());
    }
}
