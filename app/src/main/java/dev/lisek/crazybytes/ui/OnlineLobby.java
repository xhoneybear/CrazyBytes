package dev.lisek.crazybytes.ui;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.List;

import dev.lisek.crazybytes.App;
import dev.lisek.crazybytes.server.Client;
import dev.lisek.crazybytes.server.Server;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public class OnlineLobby extends Lobby {

    Server server;
    Client client;

    // Host constructor
    public OnlineLobby() {
        this.server = new Server(this);
        this.server.start();
        super.title.setText("Online | %s:%d".formatted(server.getAddress(), server.port));
        super.play.setText("Start");
        super.back.setOnAction(eh -> {
            server.close();
            App.stage.setScene(App.menu);
        });
        PlayerEntry host = App.profile.export.entry;
        host.ready.setFill(Color.YELLOW);
        super.players.content.getChildren().add(host);
    }

    // Client constructor
    public OnlineLobby(String address) throws IOException{
        String ip = address.split(":")[0];
        int port = Integer.parseInt(address.split(":")[1]);
        this.client = new Client(this, ip, port);
        super.title.setText("Online | " + address);
        super.play.setText("Ready");
        super.play.setOnAction(eh -> client.send("RDYRQ"));
        super.back.setOnAction(eh -> {
            client.send("LEAVE");
            client.close();
            App.stage.setScene(App.menu);
        });
    }

    public void addPlayer(PlayerEntry profile) {
        super.players.content.getChildren().add(profile);
    }

    public void removePlayer(PlayerEntry profile) {
        super.players.content.getChildren().remove(profile);
    }

    @Override
    void startGame(Constructor<?> constructor, List<Node> playerList) {
        // TODO: implement
    }
}