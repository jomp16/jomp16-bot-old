/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.plugin.cyanogenmod;

public enum Versions {
    CM_102("cm-10.2", "CyanogenMod 10.2", "Android 4.3"),
    CM_101("cm-10.1", "CyanogenMod 10.1", "Android 4.2.2"),
    CM_10("jellybean", "CyanogenMod 10", "Android 4.1.2"),
    CM_9("ics", "CyanogenMod 9", "Android 4.0.4"),
    CM_7("gingerbread", "CyanogenMod 7", "Android 2.3.7");
    public String branch;
    public String name;
    public String androidVersion;

    private Versions(String branch, String name, String androidVersion) {
        this.branch = branch;
        this.name = name;
        this.androidVersion = androidVersion;
    }

    public static Versions getVersionByBranch(String branch) {
        for (Versions version : Versions.values()) {
            if (version.branch.equals(branch)) {
                return version;
            }
        }

        return null;
    }

    public String getBranch() {
        return branch;
    }

    public String getName() {
        return name;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }
}
