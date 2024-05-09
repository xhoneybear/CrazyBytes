package javacards;

import javafx.scene.image.Image;
public class Config {
    public static final String DIR = Card.class.getResource("/cards/").toExternalForm();
    public static final Image BACK = new Image(DIR + "back.png", 135, 210, true, true);

    private static final String HEARTS = "#FF6E66";
    private static final String DIAMONDS = "#BB66FF";
    private static final String SPADES = "#AAFF66";
    private static final String CLUBS = "#66F7FF";



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
}
