/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.codecutils.ui;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.BinaryCodec;
import org.apache.commons.codec.binary.Hex;
import tk.jomp16.plugin.codecutils.CodecUtils;

import javax.swing.*;
import java.util.Base64;

public class EncodeDecodePanel {
    public JPanel encodeDecodePanel;
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JButton base64Button;
    private JButton binaryButton;
    private JButton hexadecimalButton;

    public EncodeDecodePanel(boolean encode) {
        base64Button.addActionListener(e -> {
            if (encode) {
                if (inputTextArea.getText().length() != 0)
                    outputTextArea.setText(new String(Base64.getEncoder().encode(inputTextArea.getText().getBytes())));
            } else {
                if (inputTextArea.getText().length() != 0)
                    outputTextArea.setText(new String(Base64.getDecoder().decode(inputTextArea.getText())));
            }
        });

        hexadecimalButton.addActionListener(e -> {
            if (encode) {
                if (inputTextArea.getText().length() != 0)
                    outputTextArea.setText(String.valueOf(Hex.encodeHex(inputTextArea.getText().getBytes())));
            } else {
                if (inputTextArea.getText().length() != 0) {
                    try {
                        outputTextArea.setText(new String(Hex.decodeHex(inputTextArea.getText().toCharArray())));
                    } catch (DecoderException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        binaryButton.addActionListener(e -> {
            if (encode) {
                if (inputTextArea.getText().length() != 0)
                    outputTextArea.setText(CodecUtils.getBinary(inputTextArea.getText()));
            } else {
                if (inputTextArea.getText().length() != 0)
                    outputTextArea.setText(new String(BinaryCodec.fromAscii(inputTextArea.getText().replace(" ", "").getBytes())));
            }
        });
    }
}
