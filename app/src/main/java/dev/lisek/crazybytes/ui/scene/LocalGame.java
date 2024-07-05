package dev.lisek.crazybytes.ui.scene;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import dev.lisek.crazybytes.App;
import dev.lisek.crazybytes.config.BotNames;
import dev.lisek.crazybytes.config.Config;
import dev.lisek.crazybytes.entity.Players;
import dev.lisek.crazybytes.game.Game;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Pair;

class PlayerEntry extends HBox {
    CheckBox human;
    TextField name;
    public PlayerEntry(VBox players) {
        this.setAlignment(Pos.TOP_CENTER);
        ImageView icon = new ImageView(Config.BOT);
        icon.setMouseTransparent(true);
        this.human = new CheckBox();
        this.human.setPrefWidth(48);
        this.human.setPrefHeight(48);
        this.human.setOpacity(0);
        this.human.setOnAction(eh -> {
            if (this.human.isSelected()) {
                icon.setImage(Config.HUMAN);
            } else {
                icon.setImage(Config.BOT);
            }
        });
        StackPane avatar = new StackPane(this.human, icon);
        this.name = new TextField(BotNames.random());
        this.name.setPrefWidth(240);
        this.name.setPrefHeight(48);
        ImageView random = new ImageView(new Image(Config.DIR + "icons/random.png", 48, 48, true, true));
        random.setCursor(Cursor.HAND);
        random.setOnMouseClicked(eh -> this.name.setText(BotNames.random()));
        ImageView remove = new ImageView(new Image(Config.DIR + "icons/user-minus.png", 48, 48, true, true));
        remove.setCursor(Cursor.HAND);
        remove.setOnMouseClicked(eh -> players.getChildren().remove(this));
        this.getChildren().addAll(avatar, this.name, random, remove);
        this.setSpacing(8);
    }
}

class MenuSection extends StackPane {
    public MenuSection(int sections, VBox fg) {
        Rectangle[] rec = new Rectangle[sections + 2];
        int len;

        for (int i = 0; i < rec.length; i ++) {
            len = i % 2 == 0 ? 80 : (640/sections - 10 * (sections - 1));
            rec[i] = new Rectangle(540, len);
            rec[i].setArcWidth(40);
            rec[i].setArcHeight(40);
            rec[i].setFill(Color.GREEN);
        }
        VBox bg = new VBox(rec);
        bg.setAlignment(Pos.CENTER);
        bg.setSpacing(20);

        this.getChildren().addAll(bg, fg);
    }
}

public class LocalGame extends Scene {

    static StackPane layout;

    public LocalGame() {
        super(layout = new StackPane(), 1600, 1000);
        this.getStylesheets().add("style.css");

        Rectangle bg = new Rectangle(1200, 880);
        bg.setArcWidth(40);
        bg.setArcHeight(40);
        bg.setFill(Color.DARKGREEN);

        VBox playerList = new VBox();
        playerList.setSpacing(16);
        Group middle = new Group(new Rectangle(0, 640), playerList);
        ImageView add = new ImageView(new Image(Config.DIR + "icons/user-plus.png", 48, 48, true, true));
        add.setCursor(Cursor.HAND);
        add.setOnMouseClicked(eh -> {
            if (playerList.getChildren().size() < 10)
                playerList.getChildren().add(new PlayerEntry(playerList));
        });
        Button play = new Button("Play");
        Button exit = new Button("Exit");
        exit.setOnAction(eh -> App.stage.setScene(App.menu));
        HBox hb = new HBox(play, exit);
        hb.setAlignment(Pos.CENTER);

        VBox fgRight = new VBox(add, middle, hb);
        fgRight.setAlignment(Pos.CENTER);
        fgRight.setSpacing(20);

        MenuSection right = new MenuSection(1, fgRight);

        Text label1 = new Text("Mode");
        Text label2 = new Text("Modifiers");

        ToggleGroup mode = new ToggleGroup();
        RadioButton[] toggles;
        VBox modifiers = new VBox();

        try (ScanResult scanResult = new ClassGraph()
            .enableAllInfo()
            .acceptPackages("dev.lisek.crazybytes.game")
            .scan()) {
            ClassInfoList widgetClasses = scanResult.getSubclasses("dev.lisek.crazybytes.game.Game");
            List<Class<?>> widgetClassNames = widgetClasses.loadClasses();
            toggles = new RadioButton[widgetClassNames.size()];
            int idx = 0;
            for (Class<?> c : widgetClassNames) {
                toggles[idx] = new RadioButton(c.getSimpleName());
                toggles[idx].setToggleGroup(mode);
                toggles[idx].setOnAction(
                    eh1 -> {
                        try {
                            modifiers.getChildren().clear();
                            modifiers.getChildren().addAll((CheckBox[]) c.getField("modifiers").get(null));
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        play.setOnAction(
                            eh2 -> startGame(c.getConstructors()[0], playerList.getChildren())
                        );
                    }
                );
                idx++;
            }
        }

        VBox modes = new VBox(toggles);
        modes.setPadding(new Insets(20));
        modes.setSpacing(16);

        StackPane modesWrapper = new StackPane(new Rectangle(0, 310), modes);
        StackPane modifiersWrapper = new StackPane(new Rectangle(0, 310), modifiers);

        VBox fgLeft = new VBox(label1, modesWrapper, label2, modifiersWrapper);
        fgLeft.setAlignment(Pos.CENTER);
        fgLeft.setSpacing(20);

        MenuSection left = new MenuSection(2, fgLeft);

        HBox fg = new HBox(left, right);
        fg.setAlignment(Pos.CENTER);
        fg.setSpacing(40);

        layout.getChildren().addAll(bg, fg);
    }

    private void startGame(Constructor<?> constructor, ObservableList playerList) {
        Pair<String, Boolean>[] players = new Pair[playerList.size()];
        String name;
        boolean human,
                local = true;
        int idx = 0,
            humans = 0,
            offset = 0;
        for (int i = 0; i < playerList.size(); i++) {
            name = ((PlayerEntry) playerList.get(i)).name.getText();
            human = ((PlayerEntry) playerList.get(i)).human.isSelected();
            players[i] = new Pair(name, !human);
            if (human) {
                idx = i;
                humans += 1;
            }
        }
        if (humans == 1) {
            local = false;
            offset = idx;
        }
        try {
            App.game = (Game) constructor.newInstance(new Players(offset, players), local);
            App.game.start();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
