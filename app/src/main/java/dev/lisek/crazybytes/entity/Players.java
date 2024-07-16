package dev.lisek.crazybytes.entity;

import dev.lisek.crazybytes.App;
import dev.lisek.crazybytes.ui.Animation;
import javafx.util.Pair;

/**
 * This class is a helper for management of players in a game.
 */

public class Players {
    public final Player[] players;
    public final int length;
    public int offset = 0;
    public int plays = 0;
    private int current;

    public Players(int offset, Pair<String, Boolean> ... players) {
        this.length = players.length;
        this.players = new Player[this.length];
        this.offset = offset;
        for (int i = 0; i < this.length; i++) {
            this.players[i] = new Player(players[i].getKey(), pos(i), this.length, players[i].getValue());
        }
        this.current = this.length - 1;
    }
    public Players(Pair<String, Boolean> ... players) {
        this(0, players);
    }

    private int pos(int i) {
        return (this.length + i - this.offset) % this.length;
    }

    public Player current() {
        return players[current];
    }

    public Player next() {
        return players[current = ++current % players.length];
    }

    public void handControl() {
        this.plays++;
        System.out.print(this.current().name.getText());
        if (!App.game.checkWin()) {
            this.current().name.setStyle("-fx-font-weight: normal;");
            if (App.game.hotseat)
                this.current().displayHand(false);
            Animation.nonBlockingSleep(500, () -> {
                this.next().takeControl();
                this.current().name.setStyle("-fx-font-weight: bold;");
                System.out.println(" hands control over to " + this.current().name.getText());
                System.out.println(this.current().getHand().cards);
            });
        }
    }

    public Players retain() {
        this.plays = 0;
        for (Player player : players) {
            player.flushHand();
        }
        return this;
    }
}
