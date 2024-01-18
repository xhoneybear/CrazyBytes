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
    /** Card face image file name. */
    public String img = Config.dir + "back.png";
    /** ImageView representing the card. */
    public final ImageView view;
    /** Card's graphical representation. */
    public final Group card;
    /** Card's orientation (true if face up) */
    public boolean visible = false;

    /**
     * Constructor for the Card class.
     * @param suit The suit of the card.
     * @param rank The rank of the card.
     */

    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
        Rectangle rec = new Rectangle(135, 210);
        rec.setFill(Paint.valueOf(Config.color(this.suit, true)));
        Image e = new Image(this.img, 135, 210, true, true);
        this.view = new ImageView(e);
        this.view.setFitWidth(135);
        this.view.setFitHeight(210);
        this.view.setScaleX(-1);
        this.card = new Group(rec, this.view);
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
