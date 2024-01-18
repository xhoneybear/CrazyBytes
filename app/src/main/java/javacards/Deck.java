package javacards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 Represents a deck of cards.
 A deck has an array of cards, and methods to shuffle the deck and deal a card.
 */

/** The deck of cards. */
public class Deck {
    /** List of cards */
    public List<Card> cards;

    /**
     * Constructor for the Deck class.
     * @param init If true, initializes the deck with all 52 cards.
     */
    public Deck(boolean init) {
        cards = new ArrayList<>();
        if (init) {
            initializeDeck();
        }
    }

    /** Initializes the deck with all 52 cards. */
    public void initializeDeck() {
        String[] suits = {"hearts", "diamonds", "clubs", "spades"};

        for(String suit: suits){
            for(int rank = 1; rank <= 13; rank++){
                cards.add(new Card(suit, Integer.toString(rank)));
            }
        }
        shuffle();
    }

    /**
     * Adds a card to the deck.
     * @param card The card to add.
     */
    public void add(Card card){
        cards.add(card);
    }

    /** Shuffles the deck */
    public void shuffle(){
        // Use the shuffle method in the Collections class to shuffle the deck.
        Collections.shuffle(cards);
    }

    /**
     * Deals a card from the deck.
     * @return The card that was dealt, or null if the deck is empty.
     */
    public Card dealCard(){
        /** If the deck is empty, return null. */
        if(cards.isEmpty()){
            return null;
        }
        /** Otherwise, remove the first card from the deck and return it. */
        return cards.remove(0);
    }
}
