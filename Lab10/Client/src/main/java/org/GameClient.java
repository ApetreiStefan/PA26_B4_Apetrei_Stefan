package org;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

class GameClient {

    public void run() throws IOException {
        try (
                Socket socket = new Socket("localhost", 8100);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                Scanner scanner = new Scanner(System.in)) {
            
            System.out.println("Client connected to server");
            
            while (true) {
                String command = scanner.nextLine();
                if ("exit".equalsIgnoreCase(command)) {
                    break;
                }
                
                out.println(command);
                out.flush();
                
                String response = in.readLine();
                if (response == null) {
                    System.out.println("Connection closed by server.");
                    break;
                }
                System.out.println(response);
                
                if ("stop".equalsIgnoreCase(command)) {
                    break;
                }
            }
        } catch (UnknownHostException e) {
            System.out.println("Server not found: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}