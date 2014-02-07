/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.sqlite.configurator;

import tk.jomp16.sqlite.SQLiteManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SQLite_Configurator {
    // TODO: terminate and improve this shittz!!

    private SQLiteManager sqLiteManager;
    private Scanner scanner = new Scanner(System.in);
    private String botNick;
    private String botRealName;
    private String botIdentify;
    private String botPrefix;
    private String ircHost;
    private List<String> owners = new ArrayList<>();
    private List<String> admins = new ArrayList<>();
    private List<String> mods = new ArrayList<>();

    public SQLite_Configurator() throws Exception {
        sqLiteManager = new SQLiteManager("database");

        step1();
        step2();
        step3();
        step4();
        step5();
        step6();

        insertData();
    }

    private void step1() {
        while (true) {
            System.out.println("What is the your bot name?");
            System.out.print("--> ");
            String tmp = scanner.nextLine();

            if (tmp.length() != 0) {
                botNick = tmp;
                break;
            } else {
                System.out.println("Derp...");
            }
        }
    }

    private void step2() {
        while (true) {
            System.out.println("What is the your bot real name?");
            System.out.print("--> ");
            String tmp = scanner.nextLine();

            if (tmp.length() != 0) {
                botRealName = tmp;
                break;
            } else {
                System.out.println("Derp...");
            }
        }
    }

    private void step3() {
        while (true) {
            System.out.println("What is the your bot identify? (~'identify'@hostName)");
            System.out.print("--> ");
            String tmp = scanner.nextLine();

            if (tmp.length() != 0) {
                botIdentify = tmp;
                break;
            } else {
                System.out.println("Derp...");
            }
        }
    }

    private void step4() {
        while (true) {
            System.out.println("What is the your bot prefix? (Example: !)");
            System.out.print("--> ");
            String tmp = scanner.nextLine();

            if (tmp.length() != 0) {
                botPrefix = tmp;
                break;
            } else {
                System.out.println("Derp...");
            }
        }
    }

    private void step5() {
        while (true) {
            System.out.println("What is the IRC host to connect? (Example: irc.freenode.org)");
            System.out.print("--> ");
            String tmp = scanner.nextLine();

            if (tmp.length() != 0) {
                ircHost = tmp;
                break;
            } else {
                System.out.println("Derp...");
            }
        }
    }

    private void step6() {
        while (true) {
            // TODO: ADD IF YOU WANT TO ADD THIS

            System.out.println("What is the hostMask of the owner of the bot? (Example: *!*@unaffiliated/nickname)");
            System.out.print("--> ");
            String tmp = scanner.nextLine();

            if (tmp.length() != 0) {
                owners.add(tmp);
            } else {
                System.out.println("Derp...");
            }

            System.out.println("You want to add more owner of your bot?");
            System.out.print("--> ");
            String tmp1 = scanner.nextLine();

            if (tmp1.endsWith("n") || tmp.length() == 0) {
                break;
            }
        }
    }

    // TODO: ADD STEPS FOR ADMIN, MOD AND CHANNELS

    public void insertData() throws SQLException {
        sqLiteManager.executeFastUpdateQuery("CREATE TABLE IF NOT EXISTS bot_config (" +
                "nick string NOT NULL, " +
                "realName string NOT NULL, " +
                "identify string NOT NULL, " +
                "prefix string NOT NULL, " +
                "server string NOT NULL" +
                ")");

        sqLiteManager.executeFastUpdateQuery("CREATE TABLE IF NOT EXISTS owners (mask string NOT NULL)");
        sqLiteManager.executeFastUpdateQuery("CREATE TABLE IF NOT EXISTS admins (mask string NOT NULL)");
        sqLiteManager.executeFastUpdateQuery("CREATE TABLE IF NOT EXISTS mods (mask string NOT NULL)");

        sqLiteManager.executeFastUpdateQuery("CREATE TABLE IF NOT EXISTS joinChannel (channel string NOT NULL)");

        sqLiteManager.insertData("bot_config", botNick, botRealName, botIdentify, botPrefix, ircHost);

        for (String owner : owners) {
            sqLiteManager.insertData("owners", owner);
        }
    }
}
