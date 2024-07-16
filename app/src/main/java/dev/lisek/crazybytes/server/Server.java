package dev.lisek.crazybytes.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

import dev.lisek.crazybytes.App;

class ClientHandler extends Thread {

    private final Socket client;
    private final ArrayList<ClientHandler> clients;
    private final BufferedReader in;
    private final PrintWriter out;

    public ClientHandler(Socket client, ArrayList<ClientHandler> clients) throws IOException {
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
                out.println("I got you!");
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

    private final ArrayList<ClientHandler> clients = new ArrayList();
    private final ServerSocket server;
    public final int port;

    public Server() {
        this.port = (int) (Math.random() * (Math.pow(2, 16) - 1024)) + 1024;
        System.out.println("Starting server on port " + port);
        try {
            server = new ServerSocket(port);
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
            start(port);
        } catch (IOException e) {
            System.out.println("Server closed");
        }
    }

    public final void start(int port) throws IOException {
        Socket client;
        ClientHandler handler;
        while (!server.isClosed()) {
            client = server.accept();
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
            server.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
