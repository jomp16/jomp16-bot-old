/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.currency;

public class CurrencyJSON {
    private String to;
    private String rate;
    private String from;
    private String v;

    public CurrencyJSON(String to, String rate, String from, String v) {
        this.to = to;
        this.rate = rate;
        this.from = from;
        this.v = v;
    }

    public String getTo() {
        return to;
    }

    public String getRate() {
        return rate;
    }

    public String getFrom() {
        return from;
    }

    public String getV() {
        return v;
    }
}
