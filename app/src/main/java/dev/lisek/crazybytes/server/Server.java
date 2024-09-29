package dev.lisek.crazybytes.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import dev.lisek.crazybytes.App;
import dev.lisek.crazybytes.config.Config;
import dev.lisek.crazybytes.entity.ExportProfile;
import dev.lisek.crazybytes.ui.OnlineLobby;
import dev.lisek.crazybytes.ui.PlayerEntry;
import javafx.application.Platform;
import javafx.scene.paint.Color;


/**
 * This class handles a single client connected to the server.
 * It contains information about a player as well as a communication
 * interface to exchange client-server information.
 */
class ClientHandler extends Thread {

    private final Socket client;
    private final List<ClientHandler> clients;
    private final BufferedReader in;
    private final PrintWriter out;

    private final OnlineLobby lobby;
    private ExportProfile profile;

    public ClientHandler(Socket client, List<ClientHandler> clients, OnlineLobby lobby) throws IOException {
        this.client = client;
        this.clients = clients;
        this.lobby = lobby;
        this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.out = new PrintWriter(client.getOutputStream(), true);
    }

    public void init(ExportProfile profile) {
        this.profile = profile;
    }

    public ExportProfile getProfile() {
        return this.profile;
    }

    public PlayerEntry getEntry() {
        return this.profile.entry;
    }

    @Override
    public void run() {
        // Notify everyone that a new player has joined
        notifyAll("JOIN" + JsonHandler.to(getProfile()));
        try {
            String message;
            String content;
            // Main call loop
            while ((message = in.readLine()) != null) {
                content = "";
                // Client leaves
                if (message.equals("LEAVE")) {
                    System.out.println("Bye!");
                    out.println("Bye!");
                    ClientHandler self = this;
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            lobby.removePlayer(self.getEntry());
                        }
                    });
                    clients.remove(this);
                    break;
                // Client requests readiness status change
                } else if (message.equals("RDYRQ")) {
                    if (getEntry().ready.getFill() == Color.RED) {
                        message = "READY";
                        getEntry().ready.setFill(Color.GREEN);
                    } else {
                        message = "UNRDY";
                        getEntry().ready.setFill(Color.RED);
                    }
                // Client sends a chat message
                } else if (message.startsWith("CHAT ")) {
                    content = message.substring(5, message.length());
                // Client plays a card
                } else if (message.startsWith("PLAY ")) {
                    content = message.substring(5, message.length());
                // Client draws a card
                } else if (message.startsWith("DRAW ")) {

                // Client passes turn
                } else if (message.equals("PASS ")) {

                // Client sends an out-of-spec signal
                } else {
                    System.out.println("Unknown message: " + message);
                }
                // Propagate to all clients
                notifyAll(message.substring(5) + getProfile().uuid() + content);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            close();
        }
    }

    public void close() {
        try {
            in.close();
            out.close();
            client.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String read() {
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(String message) {
        out.println(message);
    }

    public void notifyAll(String message) {
        for (ClientHandler c : clients) {
            c.send(message);
        }
    }
}

/**
 * Main server class.
 * Receives connection requests and client data and assigns
 * a ClientHandler to each client after a verification check.
 */
public class Server extends Thread {

    private final List<ClientHandler> clients = new ArrayList<>();
    private final ServerSocket socket;
    public final int port;

    private OnlineLobby lobby;

    public Server(OnlineLobby lobby) {
        this.port = 1024 + Config.random.nextInt((int) Math.pow(2, 16) - 1024);
        this.lobby = lobby;
        System.out.println("Starting server on port " + port);
        try {
            socket = new ServerSocket(port);
            System.out.println("Server successfully started");
            App.stage.setOnCloseRequest(eh -> this.close());
        } catch (IOException e) {
            System.out.println("Failed to start server");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            startServer();
        } catch (IOException e) {
            System.out.println("Server closed");
        }
    }

    public final void startServer() throws IOException {
        Socket client;
        while (!socket.isClosed()) {
            client = socket.accept();
            final ClientHandler handler = new ClientHandler(client, clients, lobby);
            handler.send("REQ_DATA");
            if (handler.read().equals(Config.VERSION)) {
                System.out.println("Client connected: " + client.getInetAddress().getHostAddress());
                handler.init(JsonHandler.from(handler.read()));
                handler.send("JOIN" + JsonHandler.to(App.profile.export));
                for (ClientHandler c : clients) {
                    handler.send("JOIN" + JsonHandler.to(c.getProfile()));
                }
                clients.add(handler);
                handler.start();
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        lobby.addPlayer(handler.getEntry());
                    }
                });
            } else {
                System.out.println("Client rejected: " + client.getInetAddress().getHostAddress() + " (version mismatch)");
                client.close();
            }
        }
    }

    public String getAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        for (ClientHandler c : clients) {
            c.close();
        }
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
