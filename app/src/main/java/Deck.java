import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 Represents a deck of cards.
 A deck has an array of cards, and methods to shuffle the deck and deal a card.
 */

/** The deck of cards. */
public class Deck {
    /** List of cards */
    private List<Card> cards;

    /**
     * Constructor for the Deck class.
     */
    public Deck() {
        cards = new ArrayList<>();
        initializeDeck();
        shuffle();
    }

    /** Initializes the deck with all 52 cards. */

    private void initializeDeck() {
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"2","3","4","5","6","7","8","9","10","Jack", "Queen", "King", "Ace"};

        for(String suit: suits){
            for(String rank: ranks){
                cards.add(new Card(suit, rank));
            }
        }
    }
    /** Shuffles the deck */
    public void shuffle(){
        // Use the shuffle method in the Collections class to shuffle the deck.
        Collections.shuffle(cards);
    }
    /** Deals a card from the deck. */
    public Card dealCard(){
        /** If the deck is empty, return null. */
        if(cards.isEmpty()){
            return null;
        }
        /** Otherwise, remove the first card from the deck and return it. */
        return cards.remove(0);
    }
}