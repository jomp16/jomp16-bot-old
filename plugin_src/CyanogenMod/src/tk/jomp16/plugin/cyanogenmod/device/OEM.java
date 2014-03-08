/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.cyanogenmod.device;

public enum OEM {
    ACER("Acer"),
    ADVENT("Advent"),
    AMAZON("Amazon"),
    ASUS("Asus"),
    BARNES_NOBLE("Barnes & Noble"),
    COMMTIVA("Commtiva"),
    GEEKSPHONE("Geeksphone"),
    GOOGLE("Google"),
    HTC("HTC"),
    HARDKERNEL("Hardkernel"),
    HP("Hewlett Packard"),
    HUAWEI("Huawei"),
    LG("LG"),
    MOTOROLA("Motorola"),
    OPPO("Oppo"),
    SAMSUNG("Samsung"),
    SONY("Sony"),
    SONY_ERICSSON("Sony Ericsson"),
    VIEWSONIC("Viewsonic"),
    ZTE("ZTE");

    private String oem;

    private OEM(String oem) {
        this.oem = oem;
    }

    public String getOem() {
        return oem;
    }

    public static OEM getOemByName(String oem) {
        for (OEM oem1 : values()) {
            if (oem.contains(oem1.oem)) {
                return oem1;
            }
        }

        return null;
    }
}
