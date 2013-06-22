/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.plugin;

import com.jomp16.irc.event.CommandFilter;
import com.jomp16.irc.event.Event;
import com.jomp16.irc.event.listener.CommandEvent;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.apache.commons.codec.binary.BinaryCodec;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Arrays;

public class CodecUtils extends Event {
    @CommandFilter("encode")
    public void encode(CommandEvent commandEvent) throws Exception {
        if (commandEvent.getArgs().size() >= 2) {
            switch (commandEvent.getArgs().get(0).toLowerCase()) {
                case "base64":
                    commandEvent.respond(Base64.encode(commandEvent.getArgs().get(1).getBytes()));
                    break;
                case "hex":
                    commandEvent.respond(String.valueOf(Hex.encodeHex(commandEvent.getArgs().get(1).getBytes())));
                    break;
                case "binary":
                    commandEvent.respond(getBinary(commandEvent.getArgs().get(1)));
                    break;
                case "bytes":
                    commandEvent.respond(Arrays.toString(commandEvent.getArgs().get(1).getBytes()));
                    break;
            }
        }
    }

    @CommandFilter("decode")
    public void decode(CommandEvent commandEvent) throws Exception {
        if (commandEvent.getArgs().size() >= 2) {
            switch (commandEvent.getArgs().get(0).toLowerCase()) {
                case "base64":
                    commandEvent.respond(Base64.decode(commandEvent.getArgs().get(1).getBytes()));
                    break;
                case "hex":
                    commandEvent.respond(new String(Hex.decodeHex(commandEvent.getArgs().get(1).toCharArray())));
                    break;
                case "binary":
                    commandEvent.respond(new String(BinaryCodec.fromAscii(commandEvent.getArgs().get(1).replace(" ", "").getBytes())));
                    break;
            }
        }
    }

    @CommandFilter("hash")
    public void hash(CommandEvent commandEvent) {
        if (commandEvent.getArgs().size() >= 2) {
            switch (commandEvent.getArgs().get(0).toLowerCase()) {
                case "md5":
                    commandEvent.respond(DigestUtils.md5Hex(commandEvent.getArgs().get(1)));
                    break;
                case "sha1":
                    commandEvent.respond(DigestUtils.shaHex(commandEvent.getArgs().get(1)));
                    break;
                case "sha256":
                    commandEvent.respond(DigestUtils.sha256Hex(commandEvent.getArgs().get(1)));
                    break;
                case "sha384":
                    commandEvent.respond(DigestUtils.sha384Hex(commandEvent.getArgs().get(1)));
                    break;
                case "sha512":
                    commandEvent.respond(DigestUtils.sha512Hex(commandEvent.getArgs().get(1)));
                    break;
            }
        }
    }

    private String getBinary(String text) {
        StringBuilder builder = new StringBuilder();
        String tmp = new String(BinaryCodec.toAsciiChars(text.getBytes()));

        int i1 = 0;
        for (int i = 0; i < tmp.length(); i++) {
            if (i1 == 8) {
                builder.append(' ');
                i1 = 0;
            }
            builder.append(tmp.charAt(i));
            i1++;
        }

        return builder.toString();
    }
}
