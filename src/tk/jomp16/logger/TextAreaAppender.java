/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.logger;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import javax.swing.*;
import java.io.Serializable;

@Plugin(name = "TextArea", category = "Core", elementType = "appender", printObject = true)
public class TextAreaAppender extends AbstractAppender {
    public static JTextArea jTextArea;
    public static JScrollPane jScrollPane;
    private JScrollBar jScrollBar;

    public TextAreaAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
    }

    @PluginFactory
    public static TextAreaAppender createAppender(
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginElement("Filters") final Filter filter,
            @PluginAttribute("name") final String name,
            @PluginAttribute("ignoreExceptions") final String ignore) {

        if (name == null) {
            LOGGER.error("No name provided for ConsoleAppender");
            return null;
        }

        if (layout == null) {
            layout = PatternLayout.createLayout(null, null, null, null, null, null);
        }

        boolean ignoreException = Boolean.parseBoolean(ignore);

        return new TextAreaAppender(name, filter, layout, ignoreException);
    }

    @Override
    public void append(LogEvent event) {
        if (jTextArea != null) {
            if (jScrollBar == null) {
                jScrollBar = jScrollPane.getVerticalScrollBar();
            }

            jTextArea.append((String) getLayout().toSerializable(event));
            jScrollBar.setValue(jScrollBar.getMaximum());
        }
    }
}
