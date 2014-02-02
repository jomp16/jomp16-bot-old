/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.utils.currency;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import tk.jomp16.irc.event.Command;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.listener.CommandEvent;
import tk.jomp16.irc.event.listener.InitEvent;
import tk.jomp16.irc.plugin.help.HelpRegister;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class Currency extends Event {
    private String URL_CURRENCY = "http://rate-exchange.appspot.com/currency?from=%s&to=%s&q=%s";

    @Command({"currency", "money"})
    public void currency(CommandEvent commandEvent) throws Exception {
        if (commandEvent.getArgs().size() >= 3) {
            String quantity = commandEvent.getArgs().get(0).toUpperCase();
            String from = commandEvent.getArgs().get(1).toUpperCase();
            String to = commandEvent.getArgs().get(2).toUpperCase();

            URL url = new URL(String.format(URL_CURRENCY, from, to, quantity));

            CurrencyJSON currencyJSON;

            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                JsonReader reader = new JsonReader(bufferedReader);

                currencyJSON = new Gson().fromJson(reader, CurrencyJSON.class);
            }

            commandEvent.respond("Currency: " + quantity + " " + from + " to " + to + " equals " + currencyJSON.getV() + " || 1 " + from + " to " + to + " equals " + currencyJSON.getRate());
        } else {
            commandEvent.showUsage(this, commandEvent.getCommand());
        }
    }

    @Override
    public void onInit(InitEvent initEvent) throws Exception {
        initEvent.addHelp(this, new HelpRegister("currency", new String[]{"money"}, "Get currency", "quantity fromCurrency toCurrency"));
    }
}
