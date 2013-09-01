/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.parser;

import tk.jomp16.irc.Source;

import java.util.ArrayList;

public class ParserToken {
    private final String rawLine;
    private final Source source;
    private final String command;
    private final ArrayList<String> params;

    public ParserToken(String rawLine, Source source, String command, ArrayList<String> params) {
        this.rawLine = rawLine;
        this.source = source;
        this.command = command;
        this.params = params;
    }

    public String getRawLine() {
        return rawLine;
    }

    public Source getSource() {
        return source;
    }

    public String getCommand() {
        return command;
    }

    public ArrayList<String> getParams() {
        return params;
    }
}
