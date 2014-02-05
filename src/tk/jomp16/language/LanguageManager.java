/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.language;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class LanguageManager {
    private ResourceBundle resourceBundle;
    private ResourceBundleControl resourceBundleControl;

    public LanguageManager(String path) {
        resourceBundleControl = new ResourceBundleControl();

        resourceBundle = ResourceBundle.getBundle(path, resourceBundleControl);
    }

    public String getString(String key) {
        if (resourceBundleControl.isJSON()) {
            return this.getJsonElement(key).getAsString();
        } else {
            return resourceBundle.getString(key);
        }
    }

    public String getString(String key, Object... params) {
        if (resourceBundleControl.isJSON()) {
            return MessageFormat.format(this.getJsonElement(key).getAsString(), params);
        } else {
            return MessageFormat.format(resourceBundle.getString(key), params);
        }
    }

    public Object getObject(String key) {
        return resourceBundle.getObject(key);
    }

    public JsonElement getJsonElement(String key) {
        if (resourceBundleControl.isJSON()) {
            return (JsonElement) this.getObject(key);
        } else {
            throw new UnsupportedOperationException("LanguageManager isn't JSON!");
        }
    }

    public List<JsonElement> getArrayJsonElement(String key) {
        if (resourceBundleControl.isJSON()) {
            List<JsonElement> elements = new ArrayList<>();

            JsonArray jsonArray = (JsonArray) this.getObject(key);

            for (JsonElement element : jsonArray) {
                elements.add(element);
            }

            return elements;
        } else {
            throw new UnsupportedOperationException("LanguageManager isn't JSON!");
        }
    }

    public List<String> getArrayAsString(String key) {
        if (resourceBundleControl.isJSON()) {
            List<String> elements = new ArrayList<>();

            JsonArray jsonArray = (JsonArray) this.getObject(key);

            for (JsonElement element : jsonArray) {
                elements.add(element.getAsString());
            }

            return elements;
        } else {
            return Arrays.asList(resourceBundle.getStringArray(key));
        }
    }

    public List<String> getArrayAsString(String key, Object... params) {
        if (resourceBundleControl.isJSON()) {
            List<String> elements = new ArrayList<>();

            JsonArray jsonArray = (JsonArray) this.getObject(key);

            for (JsonElement element : jsonArray) {
                elements.add(MessageFormat.format(element.getAsString(), params));
            }

            return elements;
        } else {
            List<String> elements = new ArrayList<>();

            for (String tmp : resourceBundle.getStringArray(key)) {
                elements.add(MessageFormat.format(tmp, params));
            }

            return elements;
        }
    }
}
