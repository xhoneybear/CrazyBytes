package dev.lisek.crazybytes.game;

import java.util.ArrayList;

import dev.lisek.crazybytes.App;
import dev.lisek.crazybytes.entity.Card;
import dev.lisek.crazybytes.entity.Player;
import dev.lisek.crazybytes.entity.Players;
import dev.lisek.crazybytes.ui.Animation;
import dev.lisek.crazybytes.ui.element.PostGame;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

class Modifiers extends ArrayList {

    ToggleGroup mode = new ToggleGroup();

    RadioButton classic = new RadioButton("Classic");
    RadioButton single = new RadioButton("First to shed");
    RadioButton countdown = new RadioButton("Countdown");

    CheckBox quickdraw = new CheckBox("Quickdraw");
    CheckBox queenSkip = new CheckBox("Queen skip");
    CheckBox aceReverse = new CheckBox("Ace reverse");
    CheckBox exchange7 = new CheckBox("7 exchange");
    CheckBox draw2 = new CheckBox("Draw 2");
    CheckBox draw4 = new CheckBox("Draw 4");

    public Modifiers() {
        classic.setToggleGroup(mode);
        single.setToggleGroup(mode);
        countdown.setToggleGroup(mode);
        classic.setSelected(true);

        // this.addAll(classic, single, countdown, quickdraw, queenSkip, aceReverse, exchange7, draw2, draw4);
        this.add(classic);
        this.add(single);
        this.add(countdown);

        this.add(quickdraw);
        this.add(queenSkip);
        this.add(aceReverse);
        this.add(exchange7);
        this.add(draw2);
        this.add(draw4);
    }
}

public class CrazyEights extends Game {

    class Ruleset {
        final static boolean checkValid(Card deck, Card player) {
            int special = 8;
            if (modifiers.countdown.isSelected()) {
                special = App.game.players.current().points;
            }
            return (
                (player.getRank() == special) ||
                (player.getRank() == deck.getRank()) ||
                player.getSuit().equals(deck.getSuit())
            );
        }
    }

    public static Modifiers modifiers = new Modifiers();

    public CrazyEights(Players players, boolean hotseat) {
        super(players, hotseat);
        super.rounds = 0;
        super.cards = 5;
        
        if (modifiers.countdown.isSelected()) {
            for (Player player : players.players) {
                if (player.points == 0)
                    player.addPoints(8);
            }
        }
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
            if (modifiers.countdown.isSelected()) {
                System.out.println(" wins! Special card: " + this.players.current().addPoints(-1));
            } else {
                System.out.println(" wins %d points!".formatted(this.players.current().collectPayment()));
            }
            if (modifiers.single.isSelected() || (modifiers.countdown.isSelected() && this.players.current().getPoints() == 0) || this.players.current().getPoints() >= 50 * this.players.length) {
                System.out.println("Game over!");
                App.game.layout.getChildren().add(new PostGame(this.players.current().profile));
            } else {
                Animation.nonBlockingSleep(5000, () -> PostGame.replay());
            }
        }
        return status;
    }
}
