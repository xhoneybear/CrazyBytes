package javacards;

import javafx.animation.ScaleTransition;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

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
    private final int rank;
    /** The deck that the card belongs to. */
    private final CardPile deck;
    /** Card face image. */
    public final Image img;
    /** ImageView representing the card. */
    public final ImageView view;
    /** Card's graphical representation. */
    public final Group card;
    /** Card's orientation (true if face up) */
    public boolean visible = false;
    /** Card's transition for use with flip animation. */
    public final ScaleTransition scale;

    /**
     * Constructor for the Card class.
     * @param suit The suit of the card.
     * @param rank The rank of the card.
     * @param deck The deck that the card belongs to.
     */

    public Card(String suit, int rank, CardPile deck) {
        this.suit = suit;
        this.rank = rank;
        this.deck = deck;
        Rectangle rec = new Rectangle(125, 200);
        rec.setFill(Paint.valueOf(Config.color(this.suit, true)));
        rec.setX(5);
        rec.setY(5);
        String e = Config.DIR + suit.charAt(0) + rank + ".png";
        this.img = new Image(e, 135, 210, true, true);
        this.view = new ImageView(Config.BACK);
        this.view.setScaleX(-1);
        this.card = new Group(rec, this.view);
        this.card.setRotate(Config.tilt());
        this.scale = new ScaleTransition(Duration.seconds(0.4), this.card);
        this.scale.setByX(2);
    }

    public boolean play() {
        boolean isValid = Ruleset.checkValid(App.stack.cards.get(0), this);
        if (isValid) {
            App.players.current().playCard(this);
            App.players.handControl();
            setHandler(null);
        }
        return isValid;
    }

    public void draw() {
        setHandler(true);

        deck.dealCard();
        App.players.current().drawCard(this);
        if (deck.cards.isEmpty()) {
            Card top = App.stack.dealCard();
            App.stack.shuffle();
            while (!App.stack.cards.isEmpty()) {
                Card temp = App.stack.dealCard();
                deck.add(temp);
                Animation.move(temp, new double[]{-210, 0});
                Animation.flip(temp);
                temp.card.toFront();
            }
            deck.activate();
            App.stack.add(top);
        }
        deck.cards.get(0).setHandler(false);
        App.players.handControl();
    }

    /**
     * Sets the handler for the card.
     * @param state The new handler state.
     */
    public final void setHandler(Boolean state) {
        if (state == null) {    // disable the card
            card.setOnMouseClicked(null);
        } else if (state) {     // make playable by player
            card.setOnMouseClicked(eh -> play());
        } else {                // make drawable from deck
            card.setOnMouseClicked(eh -> draw());
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
    public int getRank() {
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
