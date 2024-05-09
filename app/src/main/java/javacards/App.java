package javacards;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

    public final static CardPile deck = new CardPile(true);
    public final static CardPile stack = new CardPile(false);
    public final static Players players = new Players(new Player[] {
        new Player("player1", new int[]{0,250,0}, false),
        new Player("player2", new int[]{-500,0,90}, false),
        new Player("player3", new int[]{0,-250,180}, false),
        new Player("player4", new int[]{500,0,-90}, false)
    });

    private static void deal(int pass) {
        if (pass == 5 * players.length) {
            startGame();
            return;
        }
        players.next().drawCard(deck.dealCard());
        Animation.nonBlockingSleep(250).thenRun(() -> deal(pass + 1));
    }

    private static void startGame() {
        stack.add(deck.dealCard());
        Animation.flip(stack.cards.get(0));
        for (Card card : deck.cards) {
            Animation.move(card, new double[]{-210, 0});
        }
        players.next().displayHand();
        deck.activate();
    }

    @Override
    public void start(Stage stage) {
        StackPane pane = new StackPane();

        Scene scene = new Scene(pane, 1280, 800);
        scene.getStylesheets().add("style.css");
        for (int i = 51; i >= 0; i--) {
            pane.getChildren().add(deck.cards.get(i).card);
        }
        stage.setScene(scene);
        stage.show();

        deal(0);
    }

    public static void main(String[] args) {
        launch();
    }
}
