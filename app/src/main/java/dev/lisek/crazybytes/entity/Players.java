package dev.lisek.crazybytes.entity;

import dev.lisek.crazybytes.App;
import dev.lisek.crazybytes.ui.Animation;
import javafx.util.Pair;

/**
 * This class is a helper for management of players in a game.
 */

public class Players {
    public final Player[] list;
    public final int length;
    public int offset = 0;
    public int plays = 0;
    private int current;

    public Players(int offset, Pair<String, Boolean> ... players) {
        this.length = players.length;
        this.list = new Player[this.length];
        this.offset = offset;
        for (int i = 0; i < this.length; i++) {
            this.list[i] = new Player(players[i].getKey(), pos(i), this.length, players[i].getValue());
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
        return list[current];
    }

    public Player next() {
        current = ++current % list.length;
        return current();
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
        for (Player player : list) {
            player.flushHand();
        }
        return this;
    }
}
