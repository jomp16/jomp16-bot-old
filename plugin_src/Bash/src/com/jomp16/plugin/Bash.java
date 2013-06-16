/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.plugin;

import com.jomp16.irc.event.CommandFilter;
import com.jomp16.irc.event.Event;
import com.jomp16.irc.event.Level;
import com.jomp16.irc.event.listener.CommandEvent;
import com.jomp16.irc.event.listener.InitEvent;
import com.jomp16.irc.plugin.help.HelpRegister;
import com.jomp16.language.LanguageManager;
import com.jomp16.language.LanguageNotFoundException;
import com.jomp16.language.LanguageStringNotFoundException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;

public class Bash extends Event {
    private LanguageManager languageManager;

    @CommandFilter(value = "exec", level = Level.OWNER)
    public void exec(CommandEvent commandEvent) throws LanguageStringNotFoundException {
        Process process = null;
        StringBuilder builder = new StringBuilder();

        if (commandEvent.getMessage().length() >= 6) {
            String tmp = commandEvent.getMessage().substring(6);
            try {
                process = Runtime.getRuntime().exec(tmp);
            } catch (Exception e) {
                commandEvent.respond(languageManager.getString("Error"));
            }

            assert process != null;
            try (BufferedReader commandReader = new BufferedReader(new InputStreamReader(process.getInputStream())); BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String tmp1;
                while ((tmp1 = commandReader.readLine()) != null) {
                    builder.append(tmp1)
                            .append(" ");
                }

                while ((tmp1 = errorReader.readLine()) != null) {
                    builder.append(tmp1)
                            .append(" ");
                }
            } catch (Exception e) {
                commandEvent.respond(languageManager.getString("Error"));
            }
            commandEvent.respond(builder, false);
        }
    }

    @CommandFilter(value = "bash", level = Level.ADMIN)
    public void bash(CommandEvent commandEvent) throws LanguageStringNotFoundException {
        Process process = null;
        StringBuilder builder = new StringBuilder();

        if (commandEvent.getMessage().length() >= 6) {
            String tmp = commandEvent.getMessage().substring(6);
            try {
                process = Runtime.getRuntime().exec("bash");
                process.getOutputStream().write((tmp + "\nexit\n").getBytes());
                process.getOutputStream().flush();
            } catch (Exception e) {
                commandEvent.respond(languageManager.getString("Error"));
            }

            assert process != null;
            try (BufferedReader commandReader = new BufferedReader(new InputStreamReader(process.getInputStream())); BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String tmp1;
                while ((tmp1 = commandReader.readLine()) != null) {
                    builder.append(tmp1)
                            .append(" ");
                }

                while ((tmp1 = errorReader.readLine()) != null) {
                    builder.append(tmp1)
                            .append(" ");
                }
            } catch (Exception e) {
                commandEvent.respond(languageManager.getString("Error"));
            }
            commandEvent.respond(builder, false);
        }
    }

    @Override
    public void onInit(InitEvent initEvent) throws Exception {
        String languageName = String.format("/lang/%s_%s.lang", System.getProperty("user.language"), System.getProperty("user.country"));
        String jarPath = URLDecoder.decode(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");

        try {
            languageManager = new LanguageManager(new URL("jar:file:" + jarPath + "!" + languageName).openStream());
        } catch (LanguageNotFoundException e) {
            try {
                languageName = String.format("/lang/%s_%s.lang", "en", "US");
                languageManager = new LanguageManager(new URL("jar:file:" + jarPath + "!" + languageName).openStream());
            } catch (LanguageNotFoundException e1) {
                // Ignore
            }
        }

        initEvent.addHelp(this, new HelpRegister("exec", languageManager.getString("HelpExec"), languageManager.getString("UsageExec"), Level.OWNER));
        initEvent.addHelp(this, new HelpRegister("bash", languageManager.getString("HelpBash"), languageManager.getString("UsageBash"), Level.OWNER));
        super.onInit(initEvent);
    }
}
