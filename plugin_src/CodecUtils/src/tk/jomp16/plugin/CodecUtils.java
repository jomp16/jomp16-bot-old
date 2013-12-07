/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin;

import org.apache.commons.codec.binary.BinaryCodec;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import tk.jomp16.irc.event.Command;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.listener.CommandEvent;
import tk.jomp16.irc.event.listener.InitEvent;
import tk.jomp16.irc.plugin.help.HelpRegister;
import tk.jomp16.plugin.rot13.Rot13;

import java.util.Base64;

public class CodecUtils extends Event {
    @Command("encode")
    public void encode(CommandEvent commandEvent) throws Exception {
        if (commandEvent.getArgs().size() >= 2) {
            switch (commandEvent.getArgs().get(0).toLowerCase()) {
                case "base64":
                    commandEvent.respond(new String(Base64.getEncoder().encode(commandEvent.getArgs().get(1).getBytes())));
                    break;
                case "hex":
                    commandEvent.respond(String.valueOf(Hex.encodeHex(commandEvent.getArgs().get(1).getBytes())));
                    break;
                case "binary":
                    commandEvent.respond(getBinary(commandEvent.getArgs().get(1)));
                    break;
                default:
                    commandEvent.showUsage(this, "encode");
                    break;
            }
        } else {
            commandEvent.showUsage(this, "encode");
        }
    }

    @Command("decode")
    public void decode(CommandEvent commandEvent) throws Exception {
        if (commandEvent.getArgs().size() >= 2) {
            switch (commandEvent.getArgs().get(0).toLowerCase()) {
                case "base64":
                    commandEvent.respond(new String(Base64.getDecoder().decode(commandEvent.getArgs().get(1))));
                    break;
                case "hex":
                    commandEvent.respond(new String(Hex.decodeHex(commandEvent.getArgs().get(1).toCharArray())));
                    break;
                case "binary":
                    commandEvent.respond(new String(BinaryCodec.fromAscii(commandEvent.getArgs().get(1).replace(" ", "").getBytes())));
                    break;
                default:
                    commandEvent.showUsage(this, "decode");
                    break;
            }
        } else {
            commandEvent.showUsage(this, "decode");
        }
    }

    @Command("hash")
    public void hash(CommandEvent commandEvent) {
        if (commandEvent.getArgs().size() >= 2) {
            switch (commandEvent.getArgs().get(0).toLowerCase()) {
                case "md2":
                    commandEvent.respond(DigestUtils.md2Hex(commandEvent.getArgs().get(1)));
                    break;
                case "md5":
                    commandEvent.respond(DigestUtils.md5Hex(commandEvent.getArgs().get(1)));
                    break;
                case "sha1":
                    commandEvent.respond(DigestUtils.sha1Hex(commandEvent.getArgs().get(1)));
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
                default:
                    commandEvent.showUsage(this, "hash");
                    break;
            }
        } else {
            commandEvent.showUsage(this, "hash");
        }
    }

    @Command("rot13")
    public void rot13(CommandEvent commandEvent) {
        if (commandEvent.getArgs().size() >= 1) {
            commandEvent.respond(Rot13.rotate(commandEvent.getArgs().get(0)));
        } else {
            commandEvent.showUsage(this, "rot13");
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

    @Override
    public void onInit(InitEvent initEvent) throws Exception {
        initEvent.addHelp(this, new HelpRegister("hash", "Return a hash of the string given", "<md2, md5, sha1, sha256, sha384, sha512> 'string to return the hash'"));
        initEvent.addHelp(this, new HelpRegister("encode", "Encode a string", "<binary, hex, base64> 'string to encode'"));
        initEvent.addHelp(this, new HelpRegister("decode", "Decode a string", "<binary, hex, base64> 'string encoded to decode'"));
        initEvent.addHelp(this, new HelpRegister("rot13", "Rotate a string by 13 times", "'string to rotate'"));
    }
}
