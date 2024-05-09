package javacards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 Represents a pile of cards.
 A pile holds an array of cards and methods to shuffle itself and deal a card.
 */

/** A pile for cards. */
public class CardPile {
    /** List of cards */
    public final List<Card> cards;

    /**
     * Constructor for the CardPile class.
     * @param init If true, initializes the deck with all 52 cards.
     */
    public CardPile(boolean init) {
        cards = new ArrayList<>();
        if (init) {
            initializeDeck();
        }
    }

    /** Initializes the deck with all 52 cards. */
    public final void initializeDeck() {
        String[] suits = {"hearts", "diamonds", "clubs", "spades"};

        for(String suit: suits){
            for(int rank = 1; rank <= 13; rank++){
                cards.add(new Card(suit, rank, this));
            }
        }
        shuffle();
    }

    public final void activate() {
        Card card = cards.get(0);
        card.setHandler(false);
    }

    /**
     * Adds a card to the deck.
     * @param card The card to add.
     */
    public void add(Card card){
        cards.add(0, card);
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
        /** 
         * If the deck is empty, return null. 
         * (I don't think it should ever happen though)
         */
        if(cards.isEmpty()){
            System.err.println("Deck is empty!");
            return null;
        }
        /** Otherwise, remove the first card from the deck and return it. */
        return cards.remove(0);
    }
}
