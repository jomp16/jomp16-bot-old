/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.cyanogenmod;

public class DeviceInfo {
    private String codename;
    private String deviceName;
    private Versions cmVersion;
    private String cmWiki;

    public DeviceInfo(String codename, String deviceName, Versions cmVersion, String cmWiki) {
        this.codename = codename;
        this.deviceName = deviceName;
        this.cmVersion = cmVersion;
        this.cmWiki = cmWiki;
    }

    public String getCodename() {
        return codename;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public Versions getCmVersion() {
        return cmVersion;
    }

    public String getCmWiki() {
        return cmWiki;
    }
}
