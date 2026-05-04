package org;

import java.io.IOException;
import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) {
        try {
            GameClient client = new GameClient();
            client.run();
        } catch (UnknownHostException e) {
            System.out.println("Server not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
