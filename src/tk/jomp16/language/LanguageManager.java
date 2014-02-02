// Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
// This work is free. You can redistribute it and/or modify it under the
// terms of the Do What The Fuck You Want To Public License, Version 2,
// as published by Sam Hocevar. See the COPYING file for more details.

package tk.jomp16.language;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class LanguageManager {
    private ResourceBundle resourceBundle;

    public LanguageManager(String path) {
        resourceBundle = ResourceBundle.getBundle(path, new ResourceBundleControl());
    }

    public String getString(String key) {
        return resourceBundle.getString(key);
    }

    public String getString(String key, Object... params) {
        return MessageFormat.format(resourceBundle.getString(key), params);
    }
}
