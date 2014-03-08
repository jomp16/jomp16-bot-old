/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.cyanogenmod.device;

public enum Versions {
    CM_11("cm-11.0", "CyanogenMod 11", "Android 4.4.2"),
    CM_102("cm-10.2", "CyanogenMod 10.2", "Android 4.3.1"),
    CM_101("cm-10.1", "CyanogenMod 10.1", "Android 4.2.2"),
    CM_10("jellybean", "CyanogenMod 10", "Android 4.1.2"),
    CM_9("ics", "CyanogenMod 9", "Android 4.0.4"),
    CM_7("gingerbread", "CyanogenMod 7", "Android 2.3.7");
    private String branch;
    private String name;
    private String androidVersion;

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
