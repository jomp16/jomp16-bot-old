/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.plugin.cyanogenmod;

public enum Versions {
    CM_102("cm-10.2"),
    CM_101("cm-10.1"),
    CM_9("ics"),
    JELLYBEAN("jellybean");
    public String value;

    private Versions(String s) {
        this.value = s;
    }

    public static Versions getVersionByBranch(String branch) {
        for (Versions version : Versions.values()) {
            if (version.value.equals(branch)) {
                return version;
            }
        }

        return null;
    }
}
