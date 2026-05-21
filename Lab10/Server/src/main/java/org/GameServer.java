package org;

import org.logic.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GameServer {
    public static final int PORT = 8100;
    private volatile boolean running = true;
    private ServerSocket serverSocket;
    private final ExecutorService pool;
    private final Game game;
    private final Set<ClientThread> clients = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public GameServer() {
        // Use Virtual Threads for scalability (Advanced requirement)
        this.pool = Executors.newVirtualThreadPerTaskExecutor();
        this.game = new Game();
    }

    public void run() throws IOException {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);

            while (running) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("Client connected: " + socket.getInetAddress());
                    ClientThread clientThread = new ClientThread(socket, this);
                    clients.add(clientThread);
                    pool.execute(clientThread);
                } catch (IOException e) {
                    if (!running) {
                        System.out.println("Server stopping...");
                        break;
                    }
                    e.printStackTrace();
                }
            }
        } finally {
            stopServer();
        }
    }
    public void stopServer() {
        this.running = false;
        System.out.println("Stopping server...");
        game.abort();
        
        broadcast("Server is shutting down. Goodbye!");

        // Close all client sockets
        for (ClientThread client : clients) {
            client.closeSocket();
        }

        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            pool.shutdown();
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
            System.out.println("Server stopped gracefully.");
        } catch (IOException | InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public boolean isRunning() {
        return running;
    }

    public Game getGame() {
        return game;
    }

    public void broadcast(String message) {
        for (ClientThread client : clients) {
            client.sendMessage(message);
        }
    }

    public void removeClient(ClientThread client) {
        clients.remove(client);
    }
}

