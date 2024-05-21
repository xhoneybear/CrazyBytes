package dev.lisek.crazybytes.config;

import java.util.ArrayList;
import java.util.List;

public class BotNames {
    private final static ArrayList<String> names = new ArrayList(List.of(
        "Andrew",
        "Boris",
        "Celine",
        "David",
        "Elliot",
        "Felix",
        "George",
        "Henry",
        "Inez",
        "James",
        "Kyle",
        "Laureen",
        "Monica",
        "Nile",
        "Oliver",
        "Paul",
        "Quinn",
        "Ralph",
        "Steve",
        "Thomas",
        "Ulysses",
        "Victoria",
        "Wendy",
        "Xavier",
        "Yvonne",
        "Zach"
    ));

    // public BotNames() {
    //     Collections.shuffle(this.names);
    // }

    public static String getName(int i) {
        return names.get(i);
    }

    public static String random() {
        return getName((int) (Math.random() * names.size()));
    }
}
