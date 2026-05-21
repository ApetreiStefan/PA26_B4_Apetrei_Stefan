package org;

import org.logic.Game;
import org.model.Player;
import org.model.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread implements Runnable {
    private final Socket socket;
    private final GameServer server;
    private PrintWriter out;
    private String playerName;

    public ClientThread(Socket socket, GameServer server) {
        this.socket = socket;
        this.server = server;
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public void closeSocket() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing socket: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            
            this.out = out;
            String request;
            while (server.isRunning() && (request = in.readLine()) != null) {
                String[] parts = request.split(" ", 2);
                String command = parts[0].toLowerCase();
                String argument = parts.length > 1 ? parts[1] : "";

                switch (command) {
                    case "stop":
                        out.println("Server stopped");
                        server.stopServer();
                        return;
                    case "exit":
                        out.println("Goodbye!");
                        return;
                    case "join":
                        this.playerName = argument;
                        server.getGame().addPlayer(playerName, false);
                        out.println("Welcome, " + playerName + "! Type 'start' to begin the game.");
                        break;
                    case "start":
                        if (playerName == null) {
                            out.println("You must join first! (e.g., 'join Alice')");
                            break;
                        }
                        if (!server.getGame().isStarted()) {
                            server.getGame().start();
                            server.broadcast("Game started! Here is the first question:");
                            sendQuestion();
                        } else {
                            out.println("Game already started.");
                        }
                        break;
                    case "answer":
                        if (playerName == null) {
                            out.println("You must join first!");
                            break;
                        }
                        handleAnswer(argument);
                        break;
                    default:
                        if (server.getGame().isStarted() && playerName != null && argument.isEmpty() && command.matches("\\d+")) {
                            handleAnswer(command);
                        } else {
                            out.println("Unknown command: " + command);
                        }
                }
            }
        } catch (IOException e) {
            System.err.println("Error in ClientThread: " + e);
        } finally {
            server.removeClient(this);
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    private void sendQuestion() {
        Question q = server.getGame().getCurrentQuestion();
        if (q != null) {
            server.broadcast(q.toString());
            server.broadcast("You have " + (server.getGame().getTimeLimit() / 1000) + " seconds to answer (e.g., 'answer 0').");
        } else {
            finishGame();
        }
    }

    private void handleAnswer(String arg) {
        try {
            int index = Integer.parseInt(arg);
            long responseTime = 5000;
            server.getGame().submitAnswer(playerName, index, responseTime);
            out.println("Answer submitted.");
            
            // For now, advance to next question
            server.getGame().nextQuestion();
            sendQuestion();
        } catch (NumberFormatException e) {
            out.println("Invalid answer format. Use 'answer [index]'.");
        }
    }

    private void finishGame() {
        StringBuilder results = new StringBuilder("Game over! Results:\n");
        for (Player p : server.getGame().getResults()) {
            results.append(p.toString()).append("\n");
        }
        server.broadcast(results.toString());
    }
}
