package dev.lisek.crazybytes.game;

import dev.lisek.crazybytes.App;
import dev.lisek.crazybytes.entity.Card;
import dev.lisek.crazybytes.entity.Player;
import dev.lisek.crazybytes.entity.Players;
import dev.lisek.crazybytes.ui.Animation;

public class CrazyEights extends Game {

    public CrazyEights(Players players, boolean local) {
        super(players, local);
        super.cards = 5;
    }

    @Override
    public void computerMove(Player player) {
        Animation.nonBlockingSleep((int)(Math.random() * 1000), () -> {
            boolean found = false;
            for (Card card : player.getHand().cards) {
                if (found = card.play()) {
                    Animation.flip(card);
                    break;
                }
            }
            if (!found) {
                App.game.deck.cards.get(0).draw();
            }
        });
    }
}
