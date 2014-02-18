/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.properties;

import com.google.gson.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONProperties {
    private Gson gson;
    private Map<String, String> propertiesString;
    private Map<String, JsonPrimitive> propertiesPrimitiveMap;
    private Map<String, JsonArray> propertiesArrayMap;
    private Map<String, JsonElement> propertiesJsonElement;

    public JSONProperties() {
        this.gson = new Gson();
        this.propertiesString = new HashMap<>();
        this.propertiesPrimitiveMap = new HashMap<>();
        this.propertiesArrayMap = new HashMap<>();
        this.propertiesJsonElement = new HashMap<>();
    }

    public void load(InputStream inputStream) {
        this.load0(new InputStreamReader(inputStream));
    }

    public void load(Reader reader) {
        this.load0(reader);
    }

    private void load0(Reader reader) {
        JsonObject jsonObject = this.gson.fromJson(reader, JsonObject.class);

        jsonObject.entrySet().forEach(jsonElementEntry -> {
            propertiesJsonElement.put(jsonElementEntry.getKey(), jsonElementEntry.getValue());

            if (jsonElementEntry.getValue() instanceof JsonArray) {
                propertiesArrayMap.put(jsonElementEntry.getKey(), jsonElementEntry.getValue().getAsJsonArray());
            } else {
                propertiesString.put(jsonElementEntry.getKey(), jsonElementEntry.getValue().getAsString());
                propertiesPrimitiveMap.put(jsonElementEntry.getKey(), (JsonPrimitive) jsonElementEntry.getValue());
            }
        });
    }

    public JsonElement getJsonElement(String key) {
        return propertiesJsonElement.get(key);
    }

    public JsonPrimitive getJsonPrimitive(String key) {
        return propertiesPrimitiveMap.get(key);
    }

    public String getString(String key) {
        return propertiesString.get(key);
    }

    public List<JsonElement> getJsonArrayAsJsonElement(String key) {
        List<JsonElement> arrayList = new ArrayList<>();

        propertiesArrayMap.get(key).forEach(arrayList::add);

        return arrayList;
    }

    public List<String> getJsonArrayAsString(String key) {
        List<String> arrayList = new ArrayList<>();

        getJsonArrayAsJsonElement(key).forEach(element -> {
            JsonPrimitive jsonPrimitive = (JsonPrimitive) element;

            if (jsonPrimitive.isString()) {
                arrayList.add(jsonPrimitive.getAsString());
            }
        });

        return arrayList;
    }

    public <T> List<T> getJsonArrayAsClass(String key, Class<T> tClass) {
        List<T> arrayList = new ArrayList<>();

        getJsonArrayAsJsonElement(key).forEach(element -> {
            String tmp = gson.toJson(element);
            arrayList.add(gson.fromJson(tmp, tClass));
        });

        return arrayList;
    }
}
