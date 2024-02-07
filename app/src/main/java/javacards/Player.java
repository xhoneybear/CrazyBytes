package javacards;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents a player in the game.
 * A player has a hand of cards.
 */

public class Player implements CrazyEightsPlayer {
    /**
     * Player's name.
     */
    private final String name;
    /**
     * Player's position on the table.
     */
    public final int[] position;
    /**
     * Is the player controlled by AI.
     */
    private final Boolean AI;
    /**
     * Player's hand.
     */
    private static List<Card> hand;

    /**
     * Constructor for the Player class.
     *
     * @param name The player's name.
     * @param pos  The player's position on the table.
     * @param AI   Is the player controlled by AI.
     */
    public Player(String name, int[] pos, Boolean AI) {
        this.name = name;
        this.position = pos;
        this.AI = AI;
        this.hand = new ArrayList<>();
    }

    /**
     * Gets the player's hand.
     *
     * @return The player's hand.
     */
    public static List<Card> getHand() {
        return hand;
    }

    /**
     * Adds a new card to the player's hand.
     *
     * @param card The card to add.
     */
    @Override
    public void drawCard(Card card) {
        Animation.move(card, this.position);
        hand.add(card);
        System.out.println("Added card: " + card);
    }

    /**
     * Removes a card from the player's hand and adds it to the stack.
     *
     * @param card Index of the card to remove.
     */

    @Override
    public void playCard(int card) {
        Animation.move(hand.get(card), new int[]{0, 0});
        App.stack.add(hand.remove(card));
        System.out.println("Removed card: " + App.stack.cards.get(App.stack.cards.size() - 1));
    }

    /**
     * Displays the player's hand.
     */
    public void displayHand() {
        for (Card card : hand) {
            Animation.flip(card);
            System.out.println(card);
        }
    }

    @Override
    public void passTurn(int turn) {
        System.out.println("Player " + this.name + " passed turn " + turn);
    }

    @Override
    public void collectPayment() {
    }
}
