/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.language;

import java.io.InputStream;
import java.util.Properties;

public class LanguageManager {
    private Properties langProperties = new Properties();

    public LanguageManager(InputStream inputStream) throws LanguageNotFoundException {
        try {
            langProperties.load(inputStream);
        } catch (Exception e) {
            throw new LanguageNotFoundException();
        }
    }

    public String getString(String key) throws LanguageStringNotFoundException {
        try {
            return new String(langProperties.getProperty(key).getBytes("ISO-8859-1"), "UTF-8");
        } catch (Exception e) {
            throw new LanguageStringNotFoundException();
        }
    }

    public String getString(String key, Object... param) throws LanguageStringNotFoundException {
        try {
            return String.format(new String(langProperties.getProperty(key).getBytes("ISO-8859-1"), "UTF-8"), param);
        } catch (Exception e) {
            throw new LanguageStringNotFoundException();
        }
    }
}
