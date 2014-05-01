/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.utils.translate;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import tk.jomp16.irc.event.Command;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.listener.event.CommandEvent;
import tk.jomp16.irc.event.listener.event.InitEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

public class Translate extends Event {
    private String URL_TRANSLATOR = "http://translate.google.com/translate_a/t?client=p&text=%s&sl=%s&tl=%s";
    private Gson gson = new Gson();

    @Command(value = "translate", args = {"from:", "to:", "text:"})
    public void translate(CommandEvent commandEvent) throws Exception {
        URL url = new URL(String.format(URL_TRANSLATOR, URLEncoder.encode((String) commandEvent.getOptionSet().valueOf("text"), "UTF-8"),
                URLEncoder.encode((String) commandEvent.getOptionSet().valueOf("from"), "UTF-8"),
                URLEncoder.encode((String) commandEvent.getOptionSet().valueOf("to"), "UTF-8")));

        TranslateJson translateJson;

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            JsonReader reader = new JsonReader(bufferedReader);

            translateJson = gson.fromJson(reader, TranslateJson.class);
        }

        if (translateJson.sentences.size() != 0) {
            commandEvent.respond(translateJson.sentences.get(0).trans);
        }
    }

    @Override
    public void onInit(InitEvent initEvent) throws Exception {
        System.setProperty("http.agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.116 Safari/537.36");
    }
}
