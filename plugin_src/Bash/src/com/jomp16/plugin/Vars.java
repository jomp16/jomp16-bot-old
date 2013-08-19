/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.plugin;

import com.jomp16.language.LanguageManager;
import com.jomp16.language.LanguageNotFoundException;

import java.net.URL;

public class Vars {
    public static LanguageManager languageManager = null;

    public static LanguageManager getLanguageManager(String path) throws Exception {
        if (languageManager == null) {
            String languageName = String.format("/lang/%s_%s.lang", System.getProperty("user.language"), System.getProperty("user.country"));

            try {
                languageManager = new LanguageManager(new URL("jar:file:" + path + "!" + languageName).openStream());
            } catch (LanguageNotFoundException e) {
                try {
                    languageName = String.format("/lang/%s_%s.lang", "en", "US");
                    languageManager = new LanguageManager(new URL("jar:file:" + path + "!" + languageName).openStream());
                } catch (LanguageNotFoundException e1) {
                    // Ignore
                }
            }
        }

        return languageManager;
    }
}
