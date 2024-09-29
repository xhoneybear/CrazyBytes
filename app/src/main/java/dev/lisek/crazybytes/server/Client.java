package dev.lisek.crazybytes.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import dev.lisek.crazybytes.App;
import dev.lisek.crazybytes.config.Config;
import dev.lisek.crazybytes.entity.ExportProfile;
import dev.lisek.crazybytes.ui.OnlineLobby;
import javafx.application.Platform;
import javafx.scene.paint.Color;

public class Client {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    public Client(OnlineLobby lobby, String ip, int port) throws IOException {
        System.out.println("Connecting to " + ip + ":" + port);
        socket = new Socket(ip, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        if (in.readLine().equals("REQ_DATA")) {
            send(Config.VERSION);
            System.out.println("Connection established");
            App.stage.setOnCloseRequest(eh -> {
                send("LEAVE");
                this.close();
            });
            send(JsonHandler.to(App.profile.export));

            new Thread() {
                @Override
                public void run() {
                    ArrayList<ExportProfile> players = new ArrayList<>();
                    String message;
                    while ((message = read()) != null) {
                        if (message.startsWith("JOIN")) {
                            ExportProfile profile = JsonHandler.from(message.substring(4));
                            players.add(profile);
                            Platform.runLater(new Runnable() {
                                @Override public void run() {
                                    lobby.addPlayer(profile.entry);
                                }
                            });
                        } else if (message.startsWith("LEAVE")) {
                            for (ExportProfile profile : players) {
                                if (profile.uuid().equals(message.substring(5))) {
                                    players.remove(profile);
                                    Platform.runLater(new Runnable() {
                                        @Override public void run() {
                                            lobby.removePlayer(profile.entry);
                                        }
                                    });
                                    break;
                                }
                            }
                        } else if (message.startsWith("READY")) {
                            String uuid;
                            for (ExportProfile profile : players) {
                                if ((uuid = message.substring(5)).equals(profile.uuid())) {
                                    profile.entry.ready.setFill(Color.GREEN);
                                    if (uuid.equals(App.profile.export.uuid())) {
                                        Platform.runLater(new Runnable() {
                                            @Override public void run() {
                                                lobby.play.setText("Unready");
                                            }
                                        });
                                    }
                                    break;
                                }
                            }
                        } else if (message.startsWith("UNRDY")) {
                            String uuid;
                            for (ExportProfile profile : players) {
                                if ((uuid = message.substring(5)).equals(profile.uuid())) {
                                    profile.entry.ready.setFill(Color.RED);
                                    if (uuid.equals(App.profile.export.uuid())) {
                                        Platform.runLater(new Runnable() {
                                            @Override public void run() {
                                                lobby.play.setText("Ready");
                                            }
                                        });
                                    }
                                    break;
                                }
                            }
                        } else if (message.startsWith("CHAT")) {
                            // TODO: add chat, chat entry scaffold class (JSON) and handle communication
                        } else {
                            System.out.println("Received unhandled message: %s".formatted(message));
                        }
                    }
                }
            }.start();
        }
    }

    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
            System.out.println("Connection closed");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(String message) {
        out.println(message);
    }

    public String read() {
        try {
            return in.readLine();
        } catch (IOException e) {
            System.out.println("Connection closed");
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            Client client = new Client(null, "localhost", 3888);
            client.send("Hello!");
            String signal;
            while ((signal = client.in.readLine()) != null) {
                System.out.println(signal);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
