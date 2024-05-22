package dev.lisek.crazybytes.game;

import dev.lisek.crazybytes.entity.Player;
import dev.lisek.crazybytes.entity.Players;

public class Blackjack extends Game {

    public Blackjack(Players players, boolean local) {
        super(players, local);
        super.cards = 2;
    }

    @Override
    public void computerMove(Player player) {}
}
