// Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
// This work is free. You can redistribute it and/or modify it under the
// terms of the Do What The Fuck You Want To Public License, Version 2,
// as published by Sam Hocevar. See the COPYING file for more details.

package tk.jomp16.plugin.utils.programmer;

import tk.jomp16.irc.event.Command;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.listener.CommandEvent;
import tk.jomp16.irc.event.listener.InitEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Jokes extends Event {
    private ArrayList<String> jokes = new ArrayList<>();
    private Random random = new Random();

    @Command("programmerJokes")
    public void programmerJokes(CommandEvent commandEvent) {
        Collections.shuffle(jokes);
        commandEvent.respond(jokes.get(random.nextInt(jokes.size())), false);
    }

    @Override
    public void onInit(InitEvent initEvent) throws Exception {
        // http://stackoverflow.com/questions/234075/what-is-your-best-programmer-joke
        jokes.add("http://crossthebreeze.files.wordpress.com/2007/08/feature.jpg");
        jokes.add("A SQL query goes into a bar, walks up to two tables and asks, \"Can I join you?\"");
        jokes.add("Saying that Java is nice because it works on every OS is like saying that anal sex is nice because it works on every gender.");
        jokes.add("Q: How many programmers does it take to change a light bulb? || A: None, that's a hardware problem");
        jokes.add("http://i.stack.imgur.com/YryhF.jpg");
        jokes.add("When your hammer is C++, everything begins to look like a thumb.");
        jokes.add("Q: Whats the object-oriented way to become wealthy? || A: Inheritance");
        jokes.add("[\"hip\", \"hip\"] || (hip hip array!)");
        jokes.add("http://imgs.xkcd.com/comics/random_number.png");
        jokes.add("Programming is like sex: one mistake and you have to support it for the rest of your life.");
        jokes.add("Software is like sex: It's better when it's free. (Linus Torvalds)");
        jokes.add("Q: How many prolog programmers does it take to change a lightbulb? || A: Yes.");
        jokes.add("To understand what recursion is, you must first understand recursion.");
        jokes.add("There are 10 types of people in the world. Those who understand binary and those who have regular sex.");
        jokes.add("There are 10 types of people. Those who understand binary and those who don't.");
        jokes.add("There are two types of people in this world: those who understand recursion and those who don't understand that there are two types of people in this world:");
        jokes.add("Unix is user friendly. It's just very particular about who its friends are.");
        jokes.add("A foo walks into a bar, takes a look around and says \"Hello World!\" and meet up his friend Baz and they takes a look around and says \"Hello World!\" and then meets up with his friends, Alice, Bob, and Carol.");
        jokes.add("If your mom was a collection class, her insert method would be public.");
        jokes.add("http://img437.imageshack.us/img437/7439/bscap0001gc.jpg");
        jokes.add("How long does it take to copy a file in Vista? Yeah, I don't know either, I'm still waiting to find out.");
        jokes.add("Q: What is the difference between a programmer and a non-programmer? || A: The non-programmer thinks a kilobyte is 1000 bytes while a programmer is convinced that a kilometer is 1024 meters");
        jokes.add("Old C programmers don't die, they're just cast into void.");
        jokes.add("If you listen to a UNIX shell, can you hear the C?");
        jokes.add("Vi, vi, vi - the editor of the beast.");
        jokes.add("Eight bytes walk into a bar. The bartender asks, \"Can I get you anything?\" || \"Yeah,\" reply the bytes. \"Make us a double.\"");
        jokes.add("C++ is a modern language where your parent can't touch your privates but your friends can!");
        jokes.add("http://techportal.co.za/files/image001.png");
        // http://www.sobbayi.com/blog/software-development/run-computer-programmer-jokes/
        jokes.add("Two bytes meet. The first byte asks, \"Are you ill?\" || The second byte replies, \"No, just feeling a bit off.\"");
        jokes.add("Q: Why do programmers always mix up Halloween and Christmas? || A: Because Oct 31 == Dec 25!");
        jokes.add("Q: how many Microsoft programmers does it take to change a light bulb? || A: None, they just make darkness a standard and tell everyone \"this behavior is by design\"");
        jokes.add("\"Keyboard not found... press F1 to continue\"");
        jokes.add("Your mouse has moved. The system must reboot to effect the change!");
        jokes.add("My CD-Rom driver became corrupted and windows could no longer recognize/find my CD-Rom drive. So the error message I got was \"please insert Windows CD\"");
        jokes.add("Two threads walk into a bar. The barkeeper looks up and yells, \"Hey, I want don't any conditions race like time last!\"");
        jokes.add("A programmer puts two glasses on his bedside table before going to sleep. A full one, in case he gets thirsty, and an empty one, in case he doesn't.");
        jokes.add("Software salesmen and used-car salesmen differ in that the latter know when they are lying.");
        jokes.add("The programmer's national anthem is ‘AAAAAAAAHHHHHHHH'.");
        jokes.add("Computers let you make more mistakes than any other invention in history. With the possible exception of handguns and tequila.");
        // http://www.scandit.com/2013/02/14/top-10-geek-jokes-for-developers/
        jokes.add("Q. How did the programmer die in the shower? || A. He read the shampoo bottle instructions: Lather. Rinse. Repeat.");
        jokes.add("3 Database Admins walked into a NoSQL bar. A little while later they walked out because they couldn't find a table.");
        jokes.add("Q: Do you know why Facebook went public? || A: They couldn't figure out the privacy settings!");
        jokes.add("Q: How do you explain the movie Inception to a programmer? || A: Basically, when you run a VM inside another VM, inside another VM, inside another VM…, everything runs real slow!");
        jokes.add("An int, a char and a string walk into a bar and order some drinks. A short while later, the int and char start hitting on the waitress who gets very uncomfortable and walks away. The string walks up to the waitress and says \"You'll have to forgive them, they're primitive types.\"");
    }
}
