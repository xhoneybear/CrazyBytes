package javacards;

public class Config {
    public static final String dir = Card.class.getResource("/cards/").toExternalForm();
    public static final String hearts = "#FF6E66";
    public static final String diamonds = "#BB66FF";
    public static final String spades = "#AAFF66";
    public static final String clubs = "#66F7FF";



    public static final String color(String suit, boolean extended) {
        if (extended) {
            return switch (suit) {
                case "hearts" -> hearts;
                case "diamonds" -> diamonds;
                case "spades" -> spades;
                case "clubs" -> clubs;
                default -> "#FFFFFF";
            };
        } else {
            return switch (suit) {
                case "hearts", "diamonds" -> hearts;
                case "spades", "clubs" -> spades;
                default -> "#FFFFFF";
            };
        }
    }
}
