/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.language;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class ResourceBundleControl extends ResourceBundle.Control {
    private final String CHARSET = "UTF-8";
    private final String FORMAT_JSON = "json";
    private final List<String> FORMATS = new ArrayList<>(FORMAT_DEFAULT);

    public ResourceBundleControl() {
        FORMATS.add(FORMAT_JSON);
    }

    @Override
    public List<String> getFormats(String baseName) {
        return FORMATS;
    }

    @Override
    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IllegalAccessException, InstantiationException, IOException {
        if (!FORMAT_JSON.equals(format)) {
            return super.newBundle(baseName, locale, format, loader, reload);
        }

        String bundleName = toBundleName(baseName, locale);
        String resourceName = toResourceName(bundleName, format);

        InputStream is = loader.getResourceAsStream(resourceName);

        if (is == null) {
            return null;
        }

        JsonObject jsonObject;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, CHARSET))) {
            jsonObject = new Gson().fromJson(br, JsonObject.class);
        }

        JSONResourceBundle rb = new JSONResourceBundle();

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            rb.put(entry.getKey(), entry.getValue().getAsString());
        }

        return rb;
    }

    private static class JSONResourceBundle extends ResourceBundle {
        private HashMap<String, Object> data = new HashMap<>();

        @Override
        public Enumeration<String> getKeys() {
            return Collections.enumeration(data.keySet());
        }

        @Override
        protected Object handleGetObject(String key) {
            return data.get(key);
        }

        public void put(String key, Object value) {
            data.put(key, value);
        }
    }
}