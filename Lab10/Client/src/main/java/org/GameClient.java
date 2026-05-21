package org;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class GameClient {
    private static final String HOST = "localhost";
    private static final int PORT = 8100;
    private boolean running = true;

    public void run() {
        try (Socket socket = new Socket(HOST, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to the Game Server!");
            System.out.println("Available commands: join [name], start, answer [index], exit, stop");

            // Thread for listening to server messages
            Thread listenerThread = new Thread(() -> {
                try {
                    String response;
                    while (running && (response = in.readLine()) != null) {
                        System.out.println("\n[Server]: " + response);
                        System.out.print("> ");
                    }
                } catch (IOException e) {
                    if (running) {
                        System.err.println("Connection lost: " + e.getMessage());
                    }
                }
            });
            listenerThread.start();

            // Main thread for sending commands
            while (running) {
                System.out.print("> ");
                if (scanner.hasNextLine()) {
                    String command = scanner.nextLine();
                    if ("exit".equalsIgnoreCase(command)) {
                        running = false;
                        out.println(command);
                        break;
                    }
                    out.println(command);
                    if ("stop".equalsIgnoreCase(command)) {
                        running = false;
                        break;
                    }
                }
            }

            listenerThread.join(1000);
        } catch (UnknownHostException e) {
            System.err.println("Server not found: " + e.getMessage());
        } catch (IOException | InterruptedException e) {
            System.err.println("I/O Error: " + e.getMessage());
        }
    }
}