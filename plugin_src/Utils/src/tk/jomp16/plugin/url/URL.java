/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.url;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.listener.InitEvent;
import tk.jomp16.irc.event.listener.event.PrivMsgEvent;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URL extends Event {
    private Pattern urlPattern = Pattern.compile("((([A-Za-z]{3,9}:(?://)?)(?:[-;:&=\\+\\$,\\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=\\+\\$,\\w]+@)[A-Za-z0-9.-]+)((?:/[\\+~%/.\\w\\-_]*)?\\??(?:[-\\+=&;%@.\\w_]*)#?(?:[.!/\\\\w]*))?)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    @Override
    public void onPrivMsg(PrivMsgEvent privMsgEvent) throws Exception {
        if (!privMsgEvent.getChannel().getTargetName().equals("#teamhacksung-support") && !privMsgEvent.getChannel().getTargetName().equals("#zlotycrew")) {
            if (privMsgEvent.getMessage().contains("http://") || privMsgEvent.getMessage().contains("https://") || privMsgEvent.getMessage().contains("www.")) {
                Matcher matcher = urlPattern.matcher(privMsgEvent.getMessage());

                while (matcher.find()) {
                    String url = matcher.group();

                    if (url.startsWith("www.")) {
                        url = "http://" + url;
                    }

                    Document document = Jsoup.connect(url).userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.10 Safari/537.36").referrer("http://www.google.com").followRedirects(true).get().normalise();
                    privMsgEvent.respond("URL: " + url + " | Title: " + document.title(), false);
                }
            }
        }
    }

    @Override
    public void onInit(InitEvent initEvent) throws Exception {
        SSLContext context = SSLContext.getInstance("SSL");
        context.init(null, new TrustManager[]{new URLDefaultTrustManager()}, new SecureRandom());
        SSLContext.setDefault(context);
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());

        System.setProperty("jsse.enableSNIExtension", "false"); // Needed
    }
}
