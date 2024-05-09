//package javacards;
//
//public class Players extends Player {
//
//
//}

package javacards;

/**
 * This class is a helper for management of players in a game.
 */

public class Players {
    private final Player[] players;
    public final int length;
    private int i;

    public Players(Player[] players) {
        this.players = players;
        this.length = players.length;
        this.i = this.length - 1;
    }

    public Player current() {
        return players[i];
    }

    public Player next() {
        return players[i = ++i % players.length];
    }

    public void handControl() {
        System.out.print(this.current().name);
        if (!this.current().hasCards()) {
            System.out.println(" wins!");
        } else {
            this.current().displayHand(false);
            Animation.nonBlockingSleep(500, () -> {
                this.next().takeControl();
                System.out.println(" hands control over to " + this.current().name);
            });
        }
    }
}
