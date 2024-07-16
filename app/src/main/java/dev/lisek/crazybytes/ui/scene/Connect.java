package dev.lisek.crazybytes.ui.scene;

import java.io.IOException;

import dev.lisek.crazybytes.App;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Connect extends Scene {

    private static StackPane layout;

    public Connect() {
        super(layout = new StackPane(), 1600, 1000);

        this.getStylesheets().add("style.css");

        Rectangle bg = new Rectangle(320, 320);
        bg.setFill(Color.GREEN);

        Text joinFail = new Text();
        joinFail.setStyle("-fx-fill: red;");

        Button local = new Button("Local game");
        local.setOnAction(eh -> App.stage.setScene(new LocalGame()));
        Button host = new Button("Host online");
        host.setOnAction(eh -> App.stage.setScene(new OnlineGame()));
        TextField address = new TextField();
        Button join = new Button("Join");
        join.setOnAction(eh -> {
            try {
                App.stage.setScene(new OnlineGame(address.getText()));
            } catch (IOException e) {
                joinFail.setText("Failed to join server");
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                joinFail.setText("Invalid address");
            }
        });
        HBox online = new HBox(address, join);
        online.setAlignment(Pos.CENTER);
        Button back = new Button("Back");
        back.setOnAction(eh -> App.stage.setScene(App.menu));
        VBox buttons = new VBox(local, host, online, joinFail, back);

        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(20);

        layout.getChildren().addAll(bg, buttons);
    }
}
