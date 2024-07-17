package dev.lisek.crazybytes.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    public Client(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("Connected to " + ip + ":" + port);
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
        try {
            out.println(message);
            System.out.println("Sent hello");
            System.out.println(in.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        try {
            Client client = new Client("localhost", 3888);
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
