/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.bash;

import tk.jomp16.language.LanguageManager;

public class Vars {
    public static LanguageManager languageManager = null;

    public static LanguageManager getLanguageManager(String path) throws Exception {
        if (languageManager == null) {
            languageManager = new LanguageManager(path);
        }

        return languageManager;
    }
}
