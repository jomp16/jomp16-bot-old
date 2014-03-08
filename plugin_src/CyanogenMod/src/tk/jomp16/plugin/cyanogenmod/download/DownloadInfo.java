/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.cyanogenmod.download;

import java.util.List;

public class DownloadInfo {
    public Integer id;
    public List<Result> result;

    public class Result {
        public String url;
        public int timestamp;
        public String md5sum;
        public String filename;
        public String channel;
        public String changes;
        public int api_level;
    }
}
