package dev.lisek.crazybytes.ui;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.List;

import dev.lisek.crazybytes.App;
import dev.lisek.crazybytes.config.Config;
import dev.lisek.crazybytes.server.Client;
import dev.lisek.crazybytes.server.Server;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

class PlayerEntry extends HBox {
    Rectangle ready = new Rectangle(8, 48);
    public PlayerEntry(VBox parent, String pfp, String name) {
        this.setAlignment(Pos.TOP_CENTER);
        ImageView icon = new ImageView(new Image(pfp, 48, 48, true, true));
        icon.setMouseTransparent(true);
        HBox avatar = new HBox(this.ready, icon);
        this.ready.setFill(Color.RED);
        Text username = new Text(name);
        username.setStyle("-fx-font-size: 18px;");
        username.setWrappingWidth(240);
        ImageView remove = new ImageView(new Image(Config.DIR + "icons/user-minus.png", 48, 48, true, true));
        remove.setCursor(Cursor.HAND);
        remove.setOnMouseClicked(eh -> parent.getChildren().remove(this));
        this.getChildren().addAll(avatar, new StackPane(username), remove);
        this.setSpacing(16);
    }
}

public class OnlineGame extends GamePrep {

    Server server;
    Client client;

    // Host constructor
    public OnlineGame() {
        this.server = new Server();
        this.server.start();
        super.title.setText("Online | %s:%d".formatted(server.getAddress(), server.port));
        super.play.setText("Start");
        super.back.setOnAction(eh -> {
            server.close();
            App.stage.setScene(App.menu);
        });
        PlayerEntry host = new PlayerEntry(super.players.content, App.profile.avatar(), App.profile.name());
        host.ready.setFill(Color.YELLOW);
        super.players.content.getChildren().add(host);
    }

    // Client constructor
    public OnlineGame(String address) throws IOException{
        String ip = address.split(":")[0];
        int port = Integer.parseInt(address.split(":")[1]);
        this.client = new Client(ip, port);
        super.title.setText("Online | " + address);
        super.play.setText("Ready");
        super.back.setOnAction(eh -> client.close());
    }

    @Override
    void startGame(Constructor<?> constructor, List<Node> playerList) {
        // TODO: implement
    }
}