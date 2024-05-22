package dev.lisek.crazybytes.game;

import dev.lisek.crazybytes.App;
import dev.lisek.crazybytes.entity.Card;
import dev.lisek.crazybytes.entity.CardPile;
import dev.lisek.crazybytes.entity.Player;
import dev.lisek.crazybytes.entity.Players;
import dev.lisek.crazybytes.ui.Animation;

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
    public int getScore(CardPile hand) {
        return 0;
    }
}
