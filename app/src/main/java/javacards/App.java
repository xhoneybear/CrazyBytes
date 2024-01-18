package javacards;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

    public final static Deck deck = new Deck(true);
    public final static Deck stack = new Deck(false);
    private final static Player player = new Player("player", new int[]{140,140}, false);

    @Override
    public void start(Stage stage) {

        StackPane pane = new StackPane();

        Scene scene = new Scene(pane, 800, 640);
        scene.getStylesheets().add("style.css");
        Button deal = new Button("Deal!");
        deal.setTranslateY(140);
        deal.setOnAction(eh -> {
            Card card = deck.dealCard();
            player.drawCard(card);
            Animation.flip(card);
            card.card.toFront();
        });
        pane.getChildren().addAll(
            deck.cards.get(51).card, deck.cards.get(50).card, deck.cards.get(49).card, deck.cards.get(48).card,
            deck.cards.get(47).card, deck.cards.get(46).card, deck.cards.get(45).card, deck.cards.get(44).card,
            deck.cards.get(43).card, deck.cards.get(42).card, deck.cards.get(41).card, deck.cards.get(40).card,
            deck.cards.get(39).card, deck.cards.get(38).card, deck.cards.get(37).card, deck.cards.get(36).card,
            deck.cards.get(35).card, deck.cards.get(34).card, deck.cards.get(33).card, deck.cards.get(32).card,
            deck.cards.get(31).card, deck.cards.get(30).card, deck.cards.get(29).card, deck.cards.get(28).card,
            deck.cards.get(27).card, deck.cards.get(26).card, deck.cards.get(25).card, deck.cards.get(24).card,
            deck.cards.get(23).card, deck.cards.get(22).card, deck.cards.get(21).card, deck.cards.get(20).card,
            deck.cards.get(19).card, deck.cards.get(18).card, deck.cards.get(17).card, deck.cards.get(16).card,
            deck.cards.get(15).card, deck.cards.get(14).card, deck.cards.get(13).card, deck.cards.get(12).card,
            deck.cards.get(11).card, deck.cards.get(10).card, deck.cards.get( 9).card, deck.cards.get( 8).card,
            deck.cards.get( 7).card, deck.cards.get( 6).card, deck.cards.get( 5).card, deck.cards.get( 4).card,
            deck.cards.get( 3).card, deck.cards.get( 2).card, deck.cards.get( 1).card, deck.cards.get( 0).card,
            deal
        );
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
