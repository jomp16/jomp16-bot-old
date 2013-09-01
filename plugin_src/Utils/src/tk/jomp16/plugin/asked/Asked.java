/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.asked;

import tk.jomp16.irc.event.Event;

@Deprecated // useless
public class Asked extends Event {
    /*private HashMap<String, Integer> askedMap = new HashMap<>();

    @Override
    public void onPrivMsg(CommandEvent commandEvent) {
        if (commandEvent.getMessage().contains("?")) {
            if (askedMap.containsKey(commandEvent.getChannel().getTargetName())) {
                int asked = askedMap.get(commandEvent.getChannel().getTargetName());
                asked++;

                if (asked == 7) {
                    asked = 0;

                    commandEvent.respond("Derp...", false);
                }

                askedMap.replace(commandEvent.getChannel().getTargetName(), asked);
            } else {
                askedMap.put(commandEvent.getChannel().getTargetName(), 1);
            }
        }
    }*/
}
