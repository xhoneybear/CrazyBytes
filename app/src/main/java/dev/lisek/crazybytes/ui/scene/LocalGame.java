package dev.lisek.crazybytes.ui.scene;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import dev.lisek.crazybytes.App;
import dev.lisek.crazybytes.config.BotNames;
import dev.lisek.crazybytes.config.Config;
import dev.lisek.crazybytes.entity.Players;
import dev.lisek.crazybytes.game.Game;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

class LocalPlayerEntry extends HBox {
    CheckBox human;
    TextField name;
    public LocalPlayerEntry(VBox parent) {
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
        remove.setOnMouseClicked(eh -> parent.getChildren().remove(this));
        this.getChildren().addAll(avatar, this.name, random, remove);
        this.setSpacing(8);
    }
}

public class LocalGame extends GamePrep {

    public LocalGame() {
        super.title.setText("Local Game");
        super.play.setText("Play");
        ImageView addPlayer = new ImageView(new Image(Config.DIR + "icons/user-plus.png", 48, 48, true, true));
        addPlayer.setTranslateX(148);
        addPlayer.setCursor(Cursor.HAND);
        addPlayer.setOnMouseClicked(eh -> {
            if (super.players.content.getChildren().size() < 10) {
                LocalPlayerEntry entry = new LocalPlayerEntry(super.players.content);
                super.players.content.getChildren().add(entry);
            }
        });
        super.players.header.getChildren().add(addPlayer);
        super.back.setOnAction(eh -> App.stage.setScene(App.menu));
    }

    @Override
    void startGame(Constructor<?> constructor, ObservableList playerList) {
        Pair<String, Boolean>[] pairs = new Pair[playerList.size()];
        String name;
        boolean human,
                hotseat = true;
        int idx = 0,
            humans = 0,
            offset = 0;
        LocalPlayerEntry entry;
        for (int i = 0; i < playerList.size(); i++) {
            entry = (LocalPlayerEntry) playerList.get(i);
            name = entry.name.getText();
            human = entry.human.isSelected();
            pairs[i] = new Pair(name, !human);
            if (human) {
                idx = i;
                humans += 1;
            }
        }
        if (humans == 1) {
            hotseat = false;
            offset = idx;
        }
        try {
            App.game = (Game) constructor.newInstance(new Players(offset, pairs), hotseat);
            App.game.start();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            System.err.println("ERROR: Invalid game constructor!");
        }
    }
}
