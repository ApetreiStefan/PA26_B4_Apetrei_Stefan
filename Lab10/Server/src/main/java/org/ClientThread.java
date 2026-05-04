package org;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    private final Socket socket;
    private final GameServer server;

    public ClientThread(Socket socket, GameServer server) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            
            String request;
            while ((request = in.readLine()) != null) {
                if ("stop".equalsIgnoreCase(request)) {
                    out.println("Server stopped");
                    out.flush();
                    server.stopServer();
                    break;
                } else {
                    out.println("Server received the request " + request);
                    out.flush();
                }
            }
        } catch (IOException e) {
            System.err.println("Error in ClientThread: " + e);
        } finally {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }
}
