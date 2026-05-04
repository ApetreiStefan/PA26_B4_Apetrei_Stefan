package org;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
    public static final int PORT = 8100;
    private boolean running = true;
    private ServerSocket serverSocket;

    public void run() throws IOException {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);

            while (running) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("Client connected: " + socket.getInetAddress());
                    new ClientThread(socket, this).start();
                } catch (IOException e) {
                    if (!running) {
                        System.out.println("Server stopped.");
                        break;
                    }
                    e.printStackTrace();
                }
            }
        } finally {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        }
    }

    public void stopServer() {
        this.running = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
