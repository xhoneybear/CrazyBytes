package javacards;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;

public class App extends Application {

    private Stage stage;
    private static BorderPane menuLayout;
    private static StackPane gameLayout;
    private static Scene menu;
    private static Scene game;

    private final static PerspectiveTransform pt = new PerspectiveTransform();
    private final static PerspectiveTransform rt = new PerspectiveTransform();

    public final static CardPile deck = new CardPile(true);
    public final static CardPile stack = new CardPile(false);
    public final static Players players = new Players(
        // new Player("Kamila", 0, 5, false),
        // new Player("Emily", 1, 5, true),
        // new Player("John", 2, 5, true),
        // new Player("Brett", 3, 5, true),
        // new Player("Patrick", 4, 5, true),
        new Pair<>("Kamila", false),
        new Pair<>("Emily", true),
        new Pair<>("John", true),
        new Pair<>("Brett", true),
        new Pair<>("Patrick", true)
    );

    private static void deal(int pass) {
        if (pass == 5 * players.length) {
            begin();
            return;
        }
        players.next().drawCard(deck.dealCard());
        Animation.nonBlockingSleep(250, () -> deal(pass + 1));
    }

    private static void begin() {
        stack.add(deck.dealCard());
        Animation.flip(stack.cards.get(0));
        for (Card card : deck.cards) {
            Animation.move(card, new double[]{-210, 0});
        }
        players.handControl();
        deck.activate();
    }

    private void startGame() {
        stage.setScene(game);
        deal(0);
    }

    private void initTransforms() {
        pt.setUlx(60);
        pt.setUly(60);
        pt.setUrx(546);
        pt.setUry(0);
        pt.setLlx(0);
        pt.setLly(291);
        pt.setLrx(591);
        pt.setLry(321);

        rt.setUlx(19);
        rt.setUly(0);
        rt.setUrx(281);
        rt.setUry(0);
        rt.setLlx(0);
        rt.setLly(100);
        rt.setLrx(257);
        rt.setLry(100);
    }

    private StackPane menuButton(int i, String text, String color) {
        Rectangle rec = new Rectangle(281, 100);
        rec.setFill(Paint.valueOf(color));
        rec.setEffect(rt);
        Text txt = new Text(text);
        txt.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        ImageView icon = new ImageView(new Image(Config.DIR + "../icons/%s.png".formatted(text.toLowerCase()), 48, 48, true, true));
        HBox hb = new HBox(txt, icon);
        hb.setAlignment(Pos.CENTER_RIGHT);
        hb.setSpacing(32);
        hb.setPadding(new Insets(0, 42, 0, 0));
        StackPane p = new StackPane(rec, hb);
        p.setTranslateX(147 - 19 * i);
        p.setTranslateY(30 + 107 * i);
        p.setCursor(Cursor.HAND);
        p.setOnMouseEntered(eh -> p.setTranslateX(297 - 19 * i));
        p.setOnMouseExited(eh -> p.setTranslateX(147 - 19 * i));
        return p;
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        initTransforms();

        ImageView c8 = new ImageView(new Image(Config.DIR + "../logo.png", 591, 321, true, true));
        Rectangle rec = new Rectangle(551, 281);
        rec.setFill(Paint.valueOf(Color.BLACK.toString()));
        rec.setX(20);
        rec.setY(20);
        Text subtitle = new Text("The pixel-perfect card shedding game!");
        subtitle.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        subtitle.setFill(Color.WHITE);
        subtitle.setX(100);
        subtitle.setY(361);
        Group logo = new Group(rec, c8, subtitle);
        logo.setEffect(pt);
        logo.setCache(true);

        menuLayout = new BorderPane();
        // HBox hbox = addHBox();
        BorderPane top = new BorderPane();
        top.setPadding(new Insets(32));
        menuLayout.setTop(top);
        top.setCenter(logo);
        Button settings = new Button("Settings");
        top.setLeft(settings);
        Button profile = new Button("Profile");
        top.setRight(profile);

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(32));
        vbox.setSpacing(16);
        // menuLayout.setLeft(vbox);
        // addStackPane(hbox);

        ImageView c1 = new ImageView(new Image(Config.DIR + "deck.png", 280, 440, true, true));
        Rectangle r1 = new Rectangle(280, 440);
        ImageView c2 = new ImageView(new Image(Config.DIR + "deck.png", 280, 440, true, true));
        Rectangle r2 = new Rectangle(280, 440);
        StackPane p = menuButton(0, "Play", "#80ff80");
        p.setOnMouseClicked(eh -> startGame());
        StackPane g = menuButton(1, "Profile", "8080ff");
        StackPane s = menuButton(2, "Settings", "ff80ff");
        StackPane e = menuButton(3, "Exit", "ff8080");
        e.setOnMouseClicked(eh -> System.exit(0));
        Group d1 = new Group(r1, c1);
        d1.setTranslateX(-80);
        d1.setTranslateY(50);
        d1.setRotate(-10);
        Group d2 = new Group(r2, c2);
        d2.setRotate(10);
        Rectangle buffer = new Rectangle(580, 0);
        Group center = new Group(buffer, p, g, s, e, d1, d2);
        menuLayout.setCenter(center);

        Text footer = new Text("Copyright © 2024. All rights reserved.");
        footer.setFill(Color.WHITE);
        // menuLayout.setMargin(footer, new Insets(32));
        menuLayout.setBottom(footer);
        menuLayout.setAlignment(footer, Pos.CENTER);

        // border.setCenter(addGridPane());
        // border.setRight(addFlowPane());
        menu = new Scene(menuLayout, 1600, 1000);
        menu.getStylesheets().add("style.css");

        Button play = new Button("Play");
        play.setOnAction(eh -> startGame());
        Button exit = new Button("Exit");
        exit.setOnAction(eh -> System.exit(0));
        vbox.getChildren().addAll(play, exit);

        gameLayout = new StackPane();
        game = new Scene(gameLayout, 1600, 1000);
        game.getStylesheets().add("style.css");
        for (int i = 51; i >= 0; i--) {
            gameLayout.getChildren().add(deck.cards.get(i).card);
        }
        for (Player player : players.players) {
            gameLayout.getChildren().add(player.name);
        }

        stage.setScene(menu);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
