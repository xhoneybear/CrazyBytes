package javacards;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * Represents a player in the game.
 * A player has a hand of cards.
 */

public class Player implements CrazyEightsPlayer {
    /**
     * Player's name.
     */
    public final Text name;
    /**
     * Player's position on the table.
     */
    public final double[] position;
    /**
     * Is the player controlled by AI.
     */
    private final Boolean AI;
    /**
     * Player's points.
     */
    private int points = 0;
    /**
     * Player's hand.
     */
    private final CardPile hand = new CardPile(false);

    /**
     * Constructor for the Player class.
     *
     * @param name  The player's name.
     * @param pos   The player's position on the table.
     * @param count The number of players in a game.
     * @param AI    Is the player controlled by AI.
     */
    public Player(String name, int pos, int count, Boolean AI) {
        long x = Math.round(600 * Math.sin(Math.toRadians(-360/count * pos)));
        long y = Math.round(300 * Math.cos(Math.toRadians(360/count * pos)));
        System.out.println(x + " " + y + " " + 360/count * pos);
        this.position = new double[]{x, y, 360/count * pos};
        this.name = new Text(name);
        this.name.setFill(Color.WHITE);
        this.name.setTranslateX(this.position[0] + this.position[0]/600 * 150);
        this.name.setTranslateY(this.position[1] + this.position[1]/300 * 150);
        this.name.setRotate((this.position[2] + 90) % 360 <= 180 ? this.position[2] : this.position[2] - 180);
        this.AI = AI;
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
    @Override
    public void drawCard(Card card) {
        hand.add(card);
        card.setHandler(true);
        System.out.println("Added card: " + card);
        card.card.toFront();
        adjustHand();
    }

    private double shift(int mod, int i) {
        return Math.round(50 * mod * this.position[i]/((2 - i) * Math.sqrt(Math.pow(this.position[0], 2) + Math.pow(this.position[1], 2))));
    }

    private double ang(int mod, int i) {
        return Math.pow(Math.abs(mod), 2.4) * Math.signum(this.position[i]);
    }

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
    @Override
    public void playCard(Card card) {
        App.stack.add(hand.cards.remove(hand.cards.indexOf(card)));
        System.out.println("Removed card: " + card.toString());
        card.card.toFront();
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
    public void displayHand() {
        displayHand(true);
    }

    public boolean hasCards() {
        return !this.hand.cards.isEmpty();
    }

    public void takeControl() {
        if (this.AI) {
            Animation.nonBlockingSleep((int)(Math.random() * 1000), () -> {
                boolean found = false;
                for (Card card : this.hand.cards) {
                    if (found = card.play()) {
                        Animation.flip(card);
                        break;
                    }
                }
                if (!found) {
                    App.deck.cards.get(0).draw();
                }
            });
        } else {
            this.displayHand();
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
