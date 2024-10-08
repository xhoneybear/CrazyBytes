package dev.lisek.crazybytes.ui;

import dev.lisek.crazybytes.App;
import dev.lisek.crazybytes.config.Config;
import dev.lisek.crazybytes.entity.LocalProfile;
import dev.lisek.crazybytes.entity.ProfileCard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

class ButtonTransform extends PerspectiveTransform {
    ButtonTransform() {
        this.setUrx(281);
        this.setLly(100);
        this.setLrx(257);
        this.setLry(100);
    }
}

class MenuButton extends StackPane {
    static final ButtonTransform pt = new ButtonTransform();

    MenuButton(int i, String text, String color) {
        Rectangle rec = new Rectangle(281, 100);
        rec.setFill(Paint.valueOf(color));
        rec.setEffect(pt);
        Text txt = new Text(text);
        txt.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-fill: black;");
        ImageView icon = new ImageView(new Image(Config.DIR + "icons/%s.png".formatted(text.toLowerCase()), 48, 48, true, true));
        HBox hb = new HBox(txt, icon);
        hb.setAlignment(Pos.CENTER_RIGHT);
        hb.setSpacing(32);
        hb.setPadding(new Insets(0, 42, 0, 0));
        this.getChildren().addAll(rec, hb);
        this.setTranslateX((double) 147 - 19 * i);
        this.setTranslateY((double) 30 + 107 * i);
        this.setCursor(Cursor.HAND);
        this.setOnMouseEntered(eh -> this.setTranslateX((double) 297 - 19 * i));
        this.setOnMouseExited(eh -> this.setTranslateX((double) 147 - 19 * i));
    }
}

class Logo extends Group {
    private static final ImageView c8 = new ImageView(new Image(Config.DIR + "logo.png", 591, 321, true, true));
    private static final Rectangle rec = new Rectangle(551, 281);
    private static final Text subtitle = new Text("The pixel-perfect card shedding game!");
    private static final PerspectiveTransform pt = new PerspectiveTransform();

    public Logo() {
        super(rec, c8, subtitle);

        rec.setFill(Paint.valueOf(Color.BLACK.toString()));
        rec.setX(20);
        rec.setY(20);

        subtitle.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        subtitle.setX(100);
        subtitle.setY(361);

        pt.setUlx(60);
        pt.setUly(60);
        pt.setUrx(546);
        pt.setLly(291);
        pt.setLrx(591);
        pt.setLry(321);

        this.setEffect(pt);
        this.setCache(true);
    }
}


public class Menu extends Scene {

    private static final BorderPane layout = new BorderPane();

    public Menu(LocalProfile profile) {
        super(layout, 1600, 1000);

        Logo logo = new Logo();

        BorderPane top = new BorderPane();
        top.setPadding(new Insets(32));
        top.setCenter(logo);
        HBox buff = new LocalProfile().card;
        buff.setOpacity(0);
        top.setLeft(buff);
        top.setRight(new ProfileCard(profile, true));
        layout.setTop(top);

        ImageView c1 = new ImageView(new Image(Config.CARDS + "deck.png", 280, 440, true, true));
        Rectangle r1 = new Rectangle(280, 440);
        ImageView c2 = new ImageView(new Image(Config.CARDS + "deck.png", 280, 440, true, true));
        Rectangle r2 = new Rectangle(280, 440);
        Group d1 = new Group(r1, c1);
        d1.setTranslateX(-80);
        d1.setTranslateY(50);
        d1.setRotate(-10);
        StackPane d2 = new StackPane(r2, c2);
        d2.setRotate(10);
        Rectangle buffer = new Rectangle(580, 0);
        MenuButton p = new MenuButton(0, "Play", "#80ff80");
        p.setOnMouseClicked(eh -> App.stage.setScene(new Connect()));
        MenuButton g = new MenuButton(1, "Profile", "8080ff");
        MenuButton s = new MenuButton(2, "Settings", "ff80ff");
        MenuButton e = new MenuButton(3, "Exit", "ff8080");
        e.setOnMouseClicked(eh -> System.exit(0));
        Group center = new Group(buffer, p, g, s, e, d1, d2);
        center.setTranslateX(80);
        center.setTranslateY(-40);
        layout.setCenter(center);

        Text footer = new Text("Created with ♥️ by lisek.dev");
        footer.setStyle("-fx-font-weight: bold;");
        footer.setTranslateY(-32);
        layout.setBottom(footer);
        BorderPane.setAlignment(footer, Pos.CENTER);

        this.getStylesheets().add("style.css");
    }
}
