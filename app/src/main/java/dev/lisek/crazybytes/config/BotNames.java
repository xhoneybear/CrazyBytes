package dev.lisek.crazybytes.config;

import java.util.ArrayList;
import java.util.List;

public class BotNames {
    private BotNames() {}

    private static final ArrayList<String> names = new ArrayList<>(List.of(
        "Abby", "Abigail", "Adam", "Alice", "Amber", "Andrew", "Ash", "Ashley", "Audrey",
        "Ben", "Beth", "Bianca", "Bill", "Blanca", "Bob", "Boris", "Brian", "Bruce",
        "Casey", "Celine", "Chloe", "Chris", "Cole",
        "David", "Dean", "Donald", "Dwayne",
        "Ellie", "Elliot", "Emily", "Emma", "Eric",
        "Fae", "Felix", "Flora", "Flynn", "Frank", "Fred",
        "Garret", "Gary", "George", "Grace", "Greg", "Gus", "Guy", "Gwen",
        "Hailey", "Haley", "Hannah", "Harry", "Henry", "Hope",
        "Ian", "Inez", "Ingrid", "Iris", "Ivy",
        "James", "Jay", "Jen", "Jenny", "Jim", "Joe", "John", "Jonathan", "Julie", "Justin",
        "Kate", "Katie", "Keith", "Kelly", "Kim", "Kirsten", "Kyle",
        "Laureen", "Laura", "Leah", "Leigh", "Lily", "Liz", "Lou", "Louis", "Lucas", "Lucy", "Luis", "Luke",
        "Mae", "Max", "Melanie", "Monica", "Morgan",
        "Nancy", "Naomi", "Nate", "Natalie", "Nathan", "Neil", "Nina", "Nile", "Nox",
        "Olivia", "Oliver", "Omar", "Oscar", "Owen",
        "Patrick", "Paul", "Paula", "Peter", "Preston",
        "Quentin", "Quincy", "Quinn",
        "Ralph", "Rae", "Ray", "Raymond", "Rebecca", "Reese", "Reid", "Rhett", "Rick", "Riley", "Robert", "Robin", "Rose",
        "Sam", "Samson", "Samuel", "Sarah", "Sean", "Shane", "Shelby", "Simon", "Stella", "Steve",
        "Terry", "Thomas", "Tim", "Timothy", "Tyler",
        "Ulysses", "Umberto", "Ursula",
        "Vicky", "Victoria",
        "Wade", "Walter", "Wendy", "William", "Willow", "Winston", "Wolfgang", "Wynne",
        "Xander", "Xavier", "Xena",
        "Yasmine", "Yvonne",
        "Zach", "Zoe"
    ));

    public static String getName(int i) {
        return names.get(i);
    }

    public static String random() {
        return getName(Config.random.nextInt(names.size()));
    }
}
