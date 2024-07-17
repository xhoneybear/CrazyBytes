package dev.lisek.crazybytes.config;

import java.net.URISyntaxException;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.scene.image.Image;

public class Config {
    public static final String CARDS = Config.class.getResource("/cards/").toExternalForm();
    public static final String DIR = CARDS.substring(0, CARDS.length() - 6);
    public static final String ACCOUNT = DIR + "account.json";
    public static final Image BACK = new Image(CARDS + "back.png", 135, 210, true, true);
    public static final Image BOT = new Image(DIR + "bot.png", 48, 48, true, true);
    public static final Image HUMAN = new Image(DIR + "human.png", 48, 48, true, true);

    private static final String HEARTS = "#FF6E66";
    private static final String DIAMONDS = "#BB66FF";
    private static final String SPADES = "#AAFF66";
    private static final String CLUBS = "#66F7FF";

    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static final java.util.Random random = new java.util.Random();

    private Config() {}

    public static final String color(String suit, boolean extended) {
        if (extended) {
            return switch (suit) {
                case "hearts" -> HEARTS;
                case "diamonds" -> DIAMONDS;
                case "spades" -> SPADES;
                case "clubs" -> CLUBS;
                default -> "#FFFFFF";
            };
        } else {
            return switch (suit) {
                case "hearts", "diamonds" -> HEARTS;
                case "spades", "clubs" -> SPADES;
                default -> "#FFFFFF";
            };
        }
    }

    public static final double tilt() {
        return Math.random() * 10 - 5;
    }

    public static final Object getResource(String resource, Boolean mode) {
        try {
            URL ret = Thread.currentThread().getContextClassLoader().getResource(resource);
            if (mode == null) {
                return ret;
            } else if (mode) {
                return ret.toURI();
            } else {
                return ret.toExternalForm();
            }
        } catch (URISyntaxException e) {
            return null;
        }
    }
}
