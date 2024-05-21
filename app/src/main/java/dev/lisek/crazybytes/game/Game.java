package dev.lisek.crazybytes.game;

import dev.lisek.crazybytes.App;
import dev.lisek.crazybytes.entity.Card;
import dev.lisek.crazybytes.entity.CardPile;
import dev.lisek.crazybytes.entity.Player;
import dev.lisek.crazybytes.entity.Players;
import dev.lisek.crazybytes.ui.Animation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

class GameMenu extends StackPane {
    GameMenu(StackPane layout) {
        super();
        StackPane bg = new StackPane();
        bg.setBackground(Background.fill(Color.BLACK));
        bg.setOpacity(0.5);
        Button back = new Button("Back to game");
        back.setOnAction(eh -> layout.getChildren().remove(this));
        Button settings = new Button("Settings");
        Button exit = new Button("Exit");
        exit.setOnAction(eh -> App.stage.setScene(App.menu));
        VBox fg = new VBox(back, settings, exit);
        fg.setAlignment(Pos.CENTER);
        this.getChildren().addAll(bg, fg);
    }
}

public class Game extends Scene {

    public static StackPane layout;

    public final CardPile deck = new CardPile(true);
    public final CardPile stack = new CardPile(false);
    public final Players players;
    public final boolean local;

    public Game(Players players, boolean local) {
        super(layout = new StackPane(), 1600, 1000);
        this.getStylesheets().add("style.css");

        this.players = players;
        this.local = local;

        StackPane board = new StackPane();

        Button menu = new Button("=");
        menu.setTranslateX(8);
        menu.setTranslateY(8);
        menu.setOnAction(eh -> layout.getChildren().add(new GameMenu(layout)));

        layout.getChildren().addAll(board, menu);
        layout.setAlignment(menu, Pos.TOP_LEFT);

        for (int i = 51; i >= 0; i--) {
            board.getChildren().add(deck.cards.get(i).card);
        }
        for (Player player : players.players) {
            board.getChildren().add(player.label);
        }

        startGame();
    }
    public Game(Players players) {
        this(players, false);
    }

    private void deal(int pass) {
        if (pass == 5 * players.length) {
            begin();
            return;
        }
        players.next().drawCard(deck.dealCard());
        Animation.nonBlockingSleep(250, () -> deal(pass + 1));
    }

    private void begin() {
        stack.add(deck.dealCard());
        Animation.flip(stack.cards.get(0));
        for (Card card : deck.cards) {
            Animation.move(card, new double[]{-210, 0});
        }
        players.handControl();
        deck.activate();
    }

    private void startGame() {
        App.stage.setScene(this);
        deal(0);
    }
}
