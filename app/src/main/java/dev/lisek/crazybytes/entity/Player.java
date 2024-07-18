package dev.lisek.crazybytes.entity;

import dev.lisek.crazybytes.App;
import dev.lisek.crazybytes.config.Config;
import dev.lisek.crazybytes.ui.Animation;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 * Represents a player in the game.
 * A player has a hand of cards.
 */
public class Player {
    /**
     * Player's name.
     */
    public final Text name;
    /** 
     * Player's avatar.
     */
    public final ImageView avatar;
    /**
     * Player's profile.
     */
    public final Profile profile;
    /**
     * Player's label.
     */
    public final HBox label;
    /**
     * Player's position on the table.
     */
    public final double[] position;
    /**
     * Is the player controlled by a computer.
     */
    public final Boolean bot;
    /**
     * Player's points.
     */
    public int points = 0;

    private final Text point = new Text("(%d)".formatted(this.points));
    /**
     * Player's hand.
     */
    private CardPile hand = new CardPile(false);

    /**
     * Constructor for the Player class.
     *
     * @param name  The player's name.
     * @param pos   The player's position on the table.
     * @param count The number of players in a game.
     * @param bot   Is the player controlled by a computer.
     */
    public Player(String name, int pos, int count, Boolean bot) {
        long x = Math.round(600 * Math.sin(Math.toRadians(-360./count * pos)));
        long y = Math.round(300 * Math.cos(Math.toRadians(360./count * pos)));
        System.out.println(x + " " + y + " " + 360/count * pos);
        this.position = new double[]{x, y, 360/count * pos};
        this.name = new Text(name);
        this.bot = bot;
        if (this.bot) {
            this.avatar = new ImageView(Config.BOT);
        } else {
            this.avatar = new ImageView(Config.HUMAN);
        }
        this.avatar.setFitHeight(24);
        this.avatar.setFitWidth(24);
        this.profile = new Profile();
        this.label = new HBox(this.avatar, this.name, this.point);
        this.label.setMouseTransparent(true);
        this.label.setAlignment(Pos.CENTER);
        this.label.setSpacing(8);
        this.label.setTranslateX(1.25 * this.position[0]);
        this.label.setTranslateY(1.5 * this.position[1]);
        this.label.setRotate((this.position[2] + 90) % 360 <= 180 ? this.position[2] : this.position[2] - 180);
    }

    /**
     * Gets the player's hand.
     *
     * @return The player's hand.
     */
    public CardPile getHand() {
        return hand;
    }

    /**
     * Gets the player's points.
     *
     * @return The player's points.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Adds a new card to the player's hand.
     *
     * @param card The card to add.
     */
    public void drawCard(Card card) {
        hand.add(card);
        if (!App.game.hotseat && !App.game.players.current().bot)
            Animation.flip(card);
        card.setHandler(true);
        System.out.println("Added card: " + card);
        card.toFront();
        adjustHand();
    }

    /**
     * Shifts a card horizontally in the player's hand.
     * Causes the cards to be spread.
     * @param mod   The shift amount.
     * @param i     The index of the card.
     */
    private double shift(int mod, int i) {
        return 50 * mod * this.position[i]/((2 - i) * Math.sqrt(Math.pow(this.position[0], 2) + Math.pow(this.position[1], 2)));
    }

    /**
     * Shifts a card vertically in the player's hand.
     * Causes the cards to make an arch.
     * @param mod   The shift amount.
     * @param i     The index of the card.
     */
    private double ang(int mod, int i) {
        //! It's binary, and it shouldn't be. Only works well for 2, 4 and 8.
        //! Will try to change it to a polar coordinate function.
        return Math.pow(Math.abs(mod), 2.4) * Math.signum(this.position[i]);
    }

    /**
     * Adjusts the position of the cards in the player's hand.
     * Causes the cards to be fan-shaped.
     * @see shift(int, int)
     * @see ang(int, int)
     */
    private void adjustHand() {
        int mod;
        for (int i = 0; i < hand.cards.size(); i++) {
            mod = 2 * i - (hand.cards.size() - 1);
            double[] pos = new double[]{
                this.position[0] + shift(mod, 1) + ang(mod, 0),
                this.position[1] - shift(mod, 0) + ang(mod, 1),
                this.position[2] + 5 * mod
            };
            Animation.move(hand.cards.get(hand.cards.size() - i - 1), pos);
        }
    }

    /**
     * Removes a card from the player's hand and adds it to the stack.
     *
     * @param card Index of the card to remove.
     */
    public void playCard(Card card) {
        System.out.println(card);
        System.out.println(hand.cards);
        App.game.stack.add(hand.cards.remove(hand.cards.indexOf(card)));
        System.out.println("Removed card: " + card.toString());
        card.toFront();
        Animation.move(card, new double[]{0, 0, Config.tilt()});
        adjustHand();
    }

    /**
     * Displays the player's hand.
     * @param show If false, hides the player's cards instead.
     */
    public void displayHand(boolean show) {
        for (Card card : hand.cards) {
            if (card.scale.getByX() == (show ? 2 : -2)) {
                Animation.flip(card);
                System.out.println(card);
            }
        }
    }
    /**
     * Shorthand for displayHand(true).
     * @see displayHand(boolean)
     */
    public void displayHand() {
        displayHand(true);
    }

    public void flushHand() {
        this.hand = new CardPile(false);
    }

    public int countHand() {
        int pts = 0;
        for (Card card : hand.cards) {
            pts += card.getRank() > 10 ? 10 : card.getRank() == 8 ? 50 : card.getRank();    //! Update for new modes
        }
        return pts;
    }

    /**
     * Checks if the player has cards in their hand.
     * @return True if the player has cards in their hand.
     */
    public boolean hasCards() {
        return !this.hand.cards.isEmpty();
    }

    /**
     * Interface to receive control in game.
     * If the player is CPU, it sheds the first valid card or draws one.
     * If the player is human, it displays the player's hand.
     */
    public void takeControl() {
        if (this.bot) {
            App.game.computerMove(this);
        } else {
            this.displayHand();
        }
    }

    public void passTurn(int turn) {
        System.out.println("Player " + this.name + " passed turn " + turn);
    }

    public int collectPayment() {
        int payment = 0;
        for (int i = 0; i < App.game.players.length; i++) {
            payment += App.game.players.next().countHand();
        }
        this.points += payment;
        this.point.setText("(%d)".formatted(this.points));
        return payment;
    }
    public int addPoints(int points) {
        this.points += points;
        this.point.setText("(%d)".formatted(this.points));
        return this.points;
    }
}
