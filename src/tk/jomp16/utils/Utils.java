/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.utils;

import com.google.common.base.CharMatcher;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    // I took it from PircBotX =P
    public static List<String> tokenizeIRCLine(String input) {
        List<String> stringParts = new ArrayList<>();
        if (input == null || input.length() == 0) {
            return stringParts;
        }

        // Heavily optimized version string split by space with all characters after :
        // as a single entry. Under benchmarks, its faster than StringTokenizer,
        // String.split, toCharArray, and charAt
        String trimmedInput = CharMatcher.WHITESPACE.trimFrom(input);
        int pos = 0, end;
        while ((end = trimmedInput.indexOf(' ', pos)) >= 0) {
            stringParts.add(trimmedInput.substring(pos, end));
            pos = end + 1;
            if (trimmedInput.charAt(pos) == ':') {
                stringParts.add(trimmedInput.substring(pos + 1));
                return stringParts;
            }
        }
        // No more spaces, add last part of line
        stringParts.add(trimmedInput.substring(pos));
        return stringParts;
    }

    public static String getRamUsage() {
        return humanReadableByteCount(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(), true);
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static void receiveDCCFile(Socket socket, File f, long startPosition) throws Exception {
        try (BufferedInputStream socketInput = new BufferedInputStream(socket.getInputStream());
             OutputStream socketOutput = socket.getOutputStream();
             RandomAccessFile fileOutput = new RandomAccessFile(f.getCanonicalPath(), "rw")) {

            fileOutput.seek(startPosition);

            long bytesTransfered = 0;
            byte[] inBuffer = new byte[1024];
            byte[] outBuffer = new byte[4];
            int bytesRead;
            while ((bytesRead = socketInput.read(inBuffer, 0, inBuffer.length)) != -1) {
                fileOutput.write(inBuffer, 0, bytesRead);
                bytesTransfered += bytesRead;
                // Send back an acknowledgement of how many bytes we have got so far.
                // Convert bytesTransfered to an "unsigned, 4 byte integer in network byte order", per DCC specification
                outBuffer[0] = (byte) ((bytesTransfered >> 24) & 0xff);
                outBuffer[1] = (byte) ((bytesTransfered >> 16) & 0xff);
                outBuffer[2] = (byte) ((bytesTransfered >> 8) & 0xff);
                outBuffer[3] = (byte) (bytesTransfered & 0xff);
                socketOutput.write(outBuffer);
            }
        }
    }

    public static void sendDCCFile(Socket socket, File f, long startPosition) throws Exception {
        try (BufferedOutputStream socketOutput = new BufferedOutputStream(socket.getOutputStream());
             BufferedInputStream socketInput = new BufferedInputStream(socket.getInputStream());
             BufferedInputStream fileInput = new BufferedInputStream(new FileInputStream(f))) {

            // Check for resuming.
            if (startPosition > 0) {
                long bytesSkipped = 0;
                while (bytesSkipped < startPosition)
                    bytesSkipped += fileInput.skip(startPosition - bytesSkipped);
            }

            byte[] outBuffer = new byte[1024];
            byte[] inBuffer = new byte[4];
            int bytesRead;
            while ((bytesRead = fileInput.read(outBuffer, 0, outBuffer.length)) != -1) {
                socketOutput.write(outBuffer, 0, bytesRead);
                socketOutput.flush();
                socketInput.read(inBuffer, 0, inBuffer.length);
            }
        }

        socket.close();
    }
}
