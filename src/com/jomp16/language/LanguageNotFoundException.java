/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.language;

public class LanguageNotFoundException extends Exception {
    @Override
    public String getMessage() {
        return "We can't find your language, maybe you mistake the language or the language doesn't exists?";
    }
}
