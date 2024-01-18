package javacards;

public class Config {
    public static final String dir = Card.class.getResource("/cards/").toExternalForm();

    public static final String color(String suit, boolean extended) {
        if (extended) {
            return switch (suit) {
                case "hearts" -> "#FF6E66";
                case "diamonds" -> "#BB66FF";
                case "spades" -> "#AAFF66";
                case "clubs" -> "#66F7FF";
                default -> "#FFFFFF";
            };
        } else {
            return switch (suit) {
                case "hearts", "diamonds" -> "#FF6E66";
                case "spades", "clubs" -> "#66F7FF";
                default -> "#FFFFFF";
            };
        }
    }
}
