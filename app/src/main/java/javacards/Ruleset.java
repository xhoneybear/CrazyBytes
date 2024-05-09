package javacards;

public class Ruleset {
    public final static boolean checkValid(Card deck, Card player) {
        return (
            (player.getRank() == 8) ||
            (player.getRank() == deck.getRank()) ||
            player.getSuit().equals(deck.getSuit())
        );
    }
}
