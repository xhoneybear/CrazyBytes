package dev.lisek.crazybytes.ui;

import java.lang.reflect.InvocationTargetException;

import dev.lisek.crazybytes.App;
import dev.lisek.crazybytes.entity.ExportProfile;
import dev.lisek.crazybytes.game.Game;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class PostGame extends StackPane {
    public PostGame(ExportProfile winner) {
        StackPane bg = new StackPane();
        bg.setBackground(Background.fill(Color.BLACK));
        bg.setOpacity(0.5);
        Text text = new Text("WINNER!");
        text.setStyle("-fx-font-weight: bold; -fx-font-size: 24px");
        Button replay = new Button("Play again");
        replay.setOnAction(eh -> replay());
        Button exit = new Button("Exit");
        exit.setOnAction(eh -> App.stage.setScene(App.menu));
        VBox vb = new VBox(text, winner.card, replay, exit);
        vb.setAlignment(Pos.CENTER);
        this.getChildren().addAll(bg, vb);
    }

    public static void replay() {
        try {
            App.game = (Game) App.game
                .getClass()
                .getConstructors()[0]
                .newInstance(App.game.players.retain(), App.game.hotseat);
            App.stage.setScene(App.game);
            App.game.start();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
