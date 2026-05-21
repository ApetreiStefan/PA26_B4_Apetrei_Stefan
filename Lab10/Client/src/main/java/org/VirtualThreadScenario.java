package org;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Scenario to demonstrate the benefit of Virtual Threads.
 * This class attempts to create 10,000 concurrent client connections.
 * With Platform Threads, this would likely crash or be extremely slow.
 * With Virtual Threads on the server, it should handle them easily.
 */
public class VirtualThreadScenario {
    private static final int CLIENT_COUNT = 1000; // Adjust based on OS limits
    private static final String HOST = "localhost";
    private static final int PORT = 8100;

    public static void main(String[] args) {
        System.out.println("Starting Virtual Thread Scenario: Connecting " + CLIENT_COUNT + " clients...");
        
        AtomicInteger successCount = new AtomicInteger(0);
        long startTime = System.currentTimeMillis();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < CLIENT_COUNT; i++) {
                final int id = i;
                executor.submit(() -> {
                    try (Socket socket = new Socket(HOST, PORT);
                         PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                        
                        out.println("join BotClient-" + id);
                        successCount.incrementAndGet();
                        
                        // Keep connection alive for a bit
                        Thread.sleep(2000);
                        out.println("exit");
                    } catch (Exception e) {
                        // System.err.println("Client " + id + " failed: " + e.getMessage());
                    }
                });
            }
            executor.shutdown();
            executor.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Scenario completed.");
        System.out.println("Successfully connected: " + successCount.get() + " / " + CLIENT_COUNT);
        System.out.println("Total time: " + (endTime - startTime) + "ms");
    }
}
