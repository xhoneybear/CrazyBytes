package dev.lisek.crazybytes.ui;

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

        Text fail = new Text();
        fail.setStyle("-fx-fill: red;");

        Button local = new Button("Local game");
        local.setOnAction(eh -> App.stage.setScene(new LocalLobby()));
        Button host = new Button("Host online");
        host.setOnAction(eh -> App.stage.setScene(new OnlineLobby()));
        TextField address = new TextField();
        Button join = new Button("Join");
        join.setOnAction(eh -> join(address.getText(), fail));
        HBox online = new HBox(address, join);
        online.setAlignment(Pos.CENTER);
        Button back = new Button("Back");
        back.setOnAction(eh -> App.stage.setScene(App.menu));
        VBox buttons = new VBox(local, host, online, fail, back);

        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(20);

        layout.getChildren().addAll(bg, buttons);
    }

    private void join(String address, Text fail) {
        try {
            App.stage.setScene(new OnlineLobby(address));
        } catch (IOException e) {
            fail.setText("Failed to join server");
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            fail.setText("Invalid address");
        }
    }
}
