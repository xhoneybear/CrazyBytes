package dev.lisek.crazybytes.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import dev.lisek.crazybytes.App;
import dev.lisek.crazybytes.config.Config;

class ClientHandler extends Thread {

    private final Socket client;
    private final List<ClientHandler> clients;
    private final BufferedReader in;
    private final PrintWriter out;

    public ClientHandler(Socket client, List<ClientHandler> clients) throws IOException {
        this.client = client;
        this.clients = clients;
        this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.out = new PrintWriter(client.getOutputStream(), true);
    }

    @Override
    public void run() {
        System.out.println(clients);
        try {
            String message;
            while ((message = in.readLine()) != null) {
                if (message.equals("quit")) {
                    System.out.println("Bye!");
                    out.println("Bye!");
                    clients.remove(this);
                    break;
                }
                notifyAll(message);
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

    public void notifyAll(String message) {
        for (ClientHandler c : clients) {
            c.out.println(message + "(echo)");
        }
    }
}

public class Server extends Thread {

    private final List<ClientHandler> clients = new ArrayList<>();
    private final ServerSocket socket;
    public final int port;

    public Server() {
        this.port = (int) (Config.random.nextInt((int) Math.pow(2, 16) - 1024)) + 1024;
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
        ClientHandler handler;
        while (!socket.isClosed()) {
            client = socket.accept();
            handler = new ClientHandler(client, clients);
            clients.add(handler);
            handler.start();
            System.out.println("Client connected: " + client.getInetAddress().getHostAddress());
        }
    }

    public String getAddress() {
        try (BufferedReader ip = new BufferedReader(
            new InputStreamReader(
                new URL("http://checkip.amazonaws.com/")
                .openStream()))) {
            return ip.readLine();
        } catch (IOException e) {
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
