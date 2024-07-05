package dev.lisek.crazybytes.game;

import dev.lisek.crazybytes.App;
import dev.lisek.crazybytes.entity.Card;
import dev.lisek.crazybytes.entity.Player;
import dev.lisek.crazybytes.entity.Players;
import dev.lisek.crazybytes.ui.Animation;
import dev.lisek.crazybytes.ui.element.PostGame;
import javafx.scene.control.CheckBox;

public class CrazyEights extends Game {

    class Ruleset {
        final static boolean checkValid(Card deck, Card player) {
            return (
                (player.getRank() == 8) ||
                (player.getRank() == deck.getRank()) ||
                player.getSuit().equals(deck.getSuit())
            );
        }
    }

    public static CheckBox[] modifiers = new CheckBox[] {
        new CheckBox("No points"),
        new CheckBox("Countdown"),
        new CheckBox("Quickdraw"),
        new CheckBox("Queen skip"),
        new CheckBox("Ace reverse"),
        new CheckBox("7 exchange"),
        new CheckBox("Draw 2"),
        new CheckBox("Draw 4"),
    };

    public CrazyEights(Players players, boolean local) {
        super(players, local);
        super.rounds = 0;
        super.cards = 5;
    }

    /**
     * Checks the validity of the move and plays the card if it is valid.
     * @param card The card to play. <br>
     * @return True if the move is valid, false otherwise. <br><ul>
     * @see <li>Card#setHandler(boolean)
     * @see <li>Player#takeControl()
     * </ul>
     */
    @Override
    public boolean play(Card card) {
        boolean isValid = Ruleset.checkValid(App.game.stack.cards.get(0), card);
        if (isValid) {
            App.game.players.current().playCard(card);
            App.game.players.handControl();
            card.setHandler(null);
        }
        return isValid;
    }

    /**
     * Draws and animates a card.
     * @param card The card to draw. <br><ul>
     * @see <li>Card#setHandler(boolean)
     * @see <li>Player#takeControl()
     * </ul>
     */
    @Override
    public void draw(Card card) {
        card.setHandler(true);

        deck.dealCard();
        App.game.players.current().drawCard(card);
        if (deck.cards.isEmpty()) {
            Card top = App.game.stack.dealCard();
            App.game.stack.shuffle();
            while (!App.game.stack.cards.isEmpty()) {
                Card temp = App.game.stack.dealCard();
                deck.add(temp);
                Animation.move(temp, new double[]{-210, 0});
                Animation.flip(temp);
                temp.toFront();
            }
            deck.activate();
            App.game.stack.add(top);
        }
        deck.cards.get(0).setHandler(false);
        App.game.players.handControl();
    }

    @Override
    public void computerMove(Player player) {
        Animation.nonBlockingSleep((int)(Math.random() * 1000), () -> {
            boolean found = false;
            for (Card card : player.getHand().cards) {
                if (found = play(card)) {
                    Animation.flip(card);
                    break;
                }
            }
            if (!found) {
                draw(App.game.deck.cards.get(0));
            }
        });
    }

    @Override
    public boolean checkWin() {
        boolean status = !this.players.current().hasCards();
        if (status) {
            for (int i = 0; i < this.players.length; i++) {
                this.players.next().displayHand();
            }
            System.out.println(" wins %d points!".formatted(this.players.current().collectPayment()));
            if (this.players.current().getPoints() >= 50 * this.players.length) {
                System.out.println("Game over!");
                App.game.layout.getChildren().add(new PostGame(this.players.current().profile));
            } else {
                Animation.nonBlockingSleep(5000, () -> PostGame.replay());
            }
        }
        return status;
    }
}
