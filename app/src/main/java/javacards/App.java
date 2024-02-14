package javacards;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

    public final static Deck deck = new Deck(true);
    public final static Deck stack = new Deck(false);
    public final static Player player = new Player("player", new int[]{140,140}, false);
    public final static int amountOfPlayers = 3;

    @Override
    public void start(Stage stage) {

        StackPane pane = new StackPane();

        Scene scene = new Scene(pane, 800, 640);
        scene.getStylesheets().add("style.css");
        for (int i = 51; i >= 0; i--) {
            pane.getChildren().add(deck.cards.get(i).card);
        }
        deck.cards.get(0).state = false;
        deck.cards.get(0).setHandler();
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
