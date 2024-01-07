/**
 * The Card class represents a playing card.
 * It contains the suit and rank of the card.
 * It also contains methods to get and set the suit and rank of the card.
 * It also contains a method to return a string representation of the card.
 *
 * @version 1.0
 * @since 2024-01-01
 */



public class Card {
    /** The suit of the card (Hearts, Diamonds, Clubs, Spades). */
    private String suit;
    /** The rank of the card (2, 3, 4, 5, 6, 7, 8, 9, 10, Jack, Queen, King, Ace). */
    private String rank;

    /**
     * Constructor for the Card class.
     * @param suit The suit of the card.
     * @param rank The rank of the card.
     */

    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
    }

    /**
     * Gets the suit of the card.
     * @return The suit of the card.
     */
    public String getSuit() {
        return suit;
    }

    /**
     * Sets the suit of the card.
     * @param suit The suit of the card.
     */
    public void setSuit(String suit) {
        this.suit = suit;
    }

    /**
     * Gets the rank of the card.
     * @return The rank of the card.
     */
    public String getRank() {
        return rank;
    }

    /**
     * Sets the rank of the card.
     * @param rank The rank of the card.
     */
    public void setRank(String rank) {
        this.rank = rank;
    }

    /**
     * Returns a string representation of the card.
     * @return A string representation of the card.
     */
    @Override
    public String toString(){
        return rank + " of " + suit;
    }
}
