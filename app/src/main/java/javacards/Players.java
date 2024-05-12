//package javacards;
//
//public class Players extends Player {
//
//
//}

package javacards;

import javafx.util.Pair;

/**
 * This class is a helper for management of players in a game.
 */

public class Players {
    public final Player[] players;
    public final int length;
    private int current;

    public Players(Pair<String, Boolean> ... players) {
        this.length = players.length;
        this.players = new Player[players.length];
        for (int i = 0; i < players.length; i++) {
            this.players[i] = new Player(players[i].getKey(), i, length, players[i].getValue());
        }
        this.current = this.length - 1;
    }

    public Player current() {
        return players[current];
    }

    public Player next() {
        return players[current = ++current % players.length];
    }

    public void handControl() {
        System.out.print(this.current().name.getText());
        if (!this.current().hasCards()) {
            System.out.println(" wins!");
        } else {
            this.current().name.setStyle("-fx-font-weight: normal;");
            this.current().displayHand(false);
            Animation.nonBlockingSleep(500, () -> {
                this.next().takeControl();
                this.current().name.setStyle("-fx-font-weight: bold;");
                System.out.println(" hands control over to " + this.current().name.getText());
            });
        }
    }
}
