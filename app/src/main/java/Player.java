import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the game.
 * A player has a hand of cards.
 */

public class Player {
    /** The player's hand. */
    private List<Card> hand;

    // Constructor for the Player class.
    public Player() {
        hand = new ArrayList<>();
    }
    // Creates a new hand for the player.
    public void addCardToHand(Card card) {
        hand.add(card);
    }

    //displays the player's hand
    public void displayHand() {
        for(Card card: hand) {
            System.out.println(card);
        }
    }
}
