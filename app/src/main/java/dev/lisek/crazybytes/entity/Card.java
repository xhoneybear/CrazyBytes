package dev.lisek.crazybytes.entity;

import dev.lisek.crazybytes.App;
import dev.lisek.crazybytes.config.Config;
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
 */
public class Card extends Group {
    /** The suit of the card (hearts, diamonds, clubs, spades). */
    private final String suit;
    /** The rank of the card (1 - 13). */
    private final int rank;
    /** Card face image. */
    public final Image img;
    /** ImageView representing the card. */
    public final ImageView view;
    /** Card's transition for use with flip animation. */
    public final ScaleTransition scale;
    /** Card's orientation (true if face up) */
    public boolean visible = false;

    /**
     * Constructor for the Card class.
     * @param suit The suit of the card.
     * @param rank The rank of the card.
     */
    public Card(String suit, int rank) {
        this.suit = suit;
        this.rank = rank;
        Rectangle rec = new Rectangle(125, 200);
        rec.setFill(Paint.valueOf(Config.color(this.suit, true)));
        rec.setX(5);
        rec.setY(5);
        String e = Config.CARDS + suit.charAt(0) + rank + ".png";
        this.img = new Image(e, 135, 210, true, true);
        this.view = new ImageView(Config.BACK);
        this.view.setScaleX(-1);
        this.getChildren().addAll(rec, this.view);
        this.setRotate(Config.tilt());
        this.scale = new ScaleTransition(Duration.seconds(0.4), this);
        this.scale.setByX(2);
    }

    /**
     * Sets the action handler for the card.
     * @param state The new handler state.
     * @see play()
     * @see draw()
     */
    public final void setHandler(Boolean state) {
        if (state == null) {    // disable the card
            this.setOnMouseClicked(null);
        } else if (state) {     // make playable by player
            this.setOnMouseClicked(eh -> App.game.play(this));
        } else {                // make drawable from deck
            this.setOnMouseClicked(eh -> App.game.draw(this));
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
