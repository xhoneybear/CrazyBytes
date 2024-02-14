package javacards;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

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
    /** The suit of the card (hearts, diamonds, clubs, spades). */
    private final String suit;
    /** The rank of the card (1 - 13). */
    private final String rank;
    /** The deck that the card belongs to. */
    private final Deck deck;
    /** Card face image file name. */
    public String img = Config.dir + "back.png";
    /** ImageView representing the card. */
    public final ImageView view;
    /** Card's graphical representation. */
    public final Group card;
    /** Card's orientation (true if face up) */
    public boolean visible = false;
    /** Whether the card is in the deck. */
    public Boolean state = null;

    /**
     * Constructor for the Card class.
     * @param suit The suit of the card.
     * @param rank The rank of the card.
     * @param deck The deck that the card belongs to.
     */

    public Card(String suit, String rank, Deck deck) {
        this.suit = suit;
        this.rank = rank;
        this.deck = deck;
        Rectangle rec = new Rectangle(135, 210);
        rec.setFill(Paint.valueOf(Config.color(this.suit, true)));
        Image e = new Image(this.img, 135, 210, true, true);
        this.view = new ImageView(e);
        this.view.setScaleX(-1);
        this.card = new Group(rec, this.view);
        this.card.setTranslateX(-210);
        setHandler();
    }

    /**
     * Sets the handler for the card.
     */
    public final void setHandler() {
        if (state == null) {
            card.setOnMouseClicked(null);
        } else if (state) {
            card.setOnMouseClicked(eh -> {
                System.out.println(App.stack.cards.size());
                Player player = App.player;
                player.playCard(this);
                setHandler();
            });
            state = null;
        } else {
            card.setOnMouseClicked(eh -> {
                Player player = App.player;
                deck.dealCard();
                player.drawCard(this);
                Animation.flip(this);
                card.toFront();
                setHandler();
                if (deck.cards.isEmpty()) {
                    Card temp = App.stack.dealCard();
                    App.stack.shuffle();
                    while (!App.stack.cards.isEmpty()) {
                        Card card = App.stack.dealCard();
                        card.state = false;
                        card.setHandler();
                        deck.add(card);
                        Animation.move(card, new int[]{-210, 0});
                        Animation.flip(card);
                        card.card.toFront();
                    }
                    App.stack.add(temp);
                }
                deck.cards.get(0).state = false;
                deck.cards.get(0).setHandler();
            });
            state = true;
        }
    }

    /**
     * Gets the suit of the card.
     * @return The suit of the card.
     */
    public String getSuit() {
        return suit;
    }

    /**
     * Gets the rank of the card.
     * @return The rank of the card.
     */
    public String getRank() {
        return rank;
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
