/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringEscapeUtils;
import tk.jomp16.irc.event.Command;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.listener.CommandEvent;
import tk.jomp16.irc.event.listener.InitEvent;
import tk.jomp16.irc.plugin.help.HelpRegister;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

public class Google extends Event {
    private static String GOOGLE = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=%s";

    @Command("google")
    public void googleSearch(CommandEvent commandEvent) throws Exception {
        if (commandEvent.getArgs().size() >= 1) {
            String url = String.format(GOOGLE, URLEncoder.encode(commandEvent.getArgs().get(0), "UTF-8"));

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
                GoogleSearch search = new Gson().fromJson(reader, GoogleSearch.class);

                if (!search.responseStatus.equals("200")) {
                    commandEvent.respond("Error while searching");
                    return;
                }

                if (search.responseData.results.size() <= 0) {
                    commandEvent.respond("No results found :'(");
                    return;
                }

                String tmp = "\u0002Link #%s: %s\u000F (at %s)";

                if (commandEvent.getArgs().size() >= 2) {
                    for (int i = 0; i < Integer.parseInt(commandEvent.getArgs().get(1)); i++) {
                        String title = StringEscapeUtils.unescapeHtml4(search.responseData.results.get(i).titleNoFormatting);
                        String url2 = URLDecoder.decode(search.responseData.results.get(i).unescapedUrl, "UTF-8");
                        commandEvent.respond(String.format(tmp, (i + 1), title, url2));
                    }
                } else {
                    String title = StringEscapeUtils.unescapeHtml4(search.responseData.results.get(0).titleNoFormatting);
                    String url2 = URLDecoder.decode(search.responseData.results.get(0).unescapedUrl, "UTF-8");
                    commandEvent.respond(String.format(tmp, 1, title, url2));
                }
            }
        } else {
            commandEvent.showUsage(this, "google");
        }
    }

    @Override
    public void onInit(InitEvent initEvent) throws Exception {
        initEvent.addHelp(this, new HelpRegister("google", "Search on google using the term given", "'<the term to search>' N (where N is the number of links to give, current maximum is 4, optional)"));
    }

    private class GoogleSearch {
        public String responseStatus;
        public ResponseData responseData;

        public class ResponseData {
            public List<Result> results;

            public class Result {
                public String unescapedUrl;
                public String url;
                public String visibleUrl;
                public String title;
                public String titleNoFormatting;
                public String content;
            }
        }
    }
}
