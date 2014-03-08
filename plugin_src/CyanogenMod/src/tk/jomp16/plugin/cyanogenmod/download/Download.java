/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.cyanogenmod.download;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.listener.event.CommandEvent;
import tk.jomp16.plugin.cyanogenmod.device.Device;
import tk.jomp16.plugin.cyanogenmod.device.DeviceInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Download extends Event {
    private final String CM_DOWNLOAD_API = "http://download.cyanogenmod.org/api";
    private Gson gson;

    public Download() {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void download(CommandEvent commandEvent) throws IOException {
        if (commandEvent.getArgs().size() >= 1) {
            System.out.println(commandEvent.getArgs());

            DeviceInfo deviceInfo = Device.getDevices().get(commandEvent.getArgs().get(0));

            if (deviceInfo != null) {
                HttpPost httpPost = new HttpPost(CM_DOWNLOAD_API);
                StringEntity json = new StringEntity(gson.toJson(new CMApiRequest(deviceInfo.getCodename())));
                json.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httpPost.setEntity(json);
                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpContext context = new BasicHttpContext();
                HttpResponse response = httpClient.execute(httpPost, context);

                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    return;
                }

                DownloadInfo downloadInfo;

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
                    downloadInfo = gson.fromJson(reader, DownloadInfo.class);
                }

                if (downloadInfo.result.size() > 0) {
                    DownloadInfo.Result latest = downloadInfo.result.get(0);
                    commandEvent.respond("Latest build (" + latest.channel + ") for " + deviceInfo.getCodename() + ": " + latest.url + " [md5sum: " + latest.md5sum + "]");
                } else {
                    commandEvent.respond("No builds for " + deviceInfo.getCodename());
                }
            }
        }
    }

    public class CMApiRequest {
        public String method = "get_all_builds";
        public Params params;

        public CMApiRequest(String device) {
            this.params = new Params(device);
        }

        public class Params {
            public String device;
            public String[] channels = new String[]{"nightly"};

            public Params(String device) {
                this.device = device;
            }
        }
    }
}
