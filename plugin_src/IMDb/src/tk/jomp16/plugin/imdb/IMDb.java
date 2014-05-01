/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.imdb;

import com.google.gson.Gson;
import tk.jomp16.irc.event.Command;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.listener.event.CommandEvent;
import tk.jomp16.irc.event.listener.event.InitEvent;
import tk.jomp16.language.LanguageManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

public class IMDb extends Event {
    private LanguageManager languageManager;
    private String OMDb_API = "http://www.omdbapi.com/?{0}";

    @Command(value = "imdb", optCommands = "omdb", args = {"title:", "year:", "tomatoes"})
    public void imdb(CommandEvent commandEvent) throws Exception {
        if (commandEvent.getOptionSet().has("title")) {
            boolean tomatoes = false;
            String url;

            if (commandEvent.getOptionSet().has("year")) {
                if (commandEvent.getOptionSet().has("tomatoes")) {
                    tomatoes = true;
                    url = languageManager.getAsString("OMDb_API_TITLE_WITH_TOMATOES_WITH_YEAR",
                            getEncodedString(commandEvent.getOptionSet().valueOf("title")),
                            getEncodedString(commandEvent.getOptionSet().valueOf("year")));
                } else {
                    url = languageManager.getAsString("OMDb_API_TITLE_WITH_YEAR",
                            getEncodedString(commandEvent.getOptionSet().valueOf("title")),
                            getEncodedString(commandEvent.getOptionSet().valueOf("year")));
                }
            } else {
                if (commandEvent.getOptionSet().has("tomatoes")) {
                    tomatoes = true;
                    url = languageManager.getAsString("OMDb_API_TITLE_WITH_TOMATOES_WITHOUT_YEAR",
                            getEncodedString(commandEvent.getOptionSet().valueOf("title")));
                } else {
                    url = languageManager.getAsString("OMDb_API_TITLE_WITHOUT_YEAR",
                            getEncodedString(commandEvent.getOptionSet().valueOf("title")));
                }
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
                IMDbJSON imDbJSON = new Gson().fromJson(reader, IMDbJSON.class);

                if (imDbJSON.Response.equals("True")) {
                    if (tomatoes) {
                        commandEvent.respond(languageManager.getAsString("RESPOND_WITH_TOMATOES", imDbJSON.Title,
                                imDbJSON.Year,
                                imDbJSON.imdbRating,
                                imDbJSON.imdbVotes,
                                imDbJSON.tomatoMeter,
                                imDbJSON.tomatoRating,
                                imDbJSON.tomatoReviews,
                                languageManager.getAsString("IMDb_URL_TITLE", imDbJSON.imdbID)));
                    } else {
                        commandEvent.respond(languageManager.getAsString("RESPOND_WITHOUT_TOMATOES", imDbJSON.Title,
                                imDbJSON.Year,
                                imDbJSON.imdbRating,
                                imDbJSON.imdbVotes,
                                languageManager.getAsString("IMDb_URL_TITLE", imDbJSON.imdbID)));
                    }
                } else {
                    commandEvent.respond("An error occurred: " + imDbJSON.Error);
                }
            }
        }
    }

    @Override
    public void onInit(InitEvent initEvent) throws Exception {
        languageManager = initEvent.getLanguageManager(this, "tk.jomp16.plugin.imdb.resource.Strings");
    }

    private String getEncodedString(Object unencoded) throws Exception {
        return URLEncoder.encode((String) unencoded, "UTF-8");
    }
}
