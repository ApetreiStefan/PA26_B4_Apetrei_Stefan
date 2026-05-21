package org;

import org.entity.PlayerEntity;
import org.repository.PlayerRepository;
import java.util.List;

public class CachePerformanceTest {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   SECOND-LEVEL CACHE & QUERY CACHE DEMONSTRATION ");
        System.out.println("==================================================");

        PlayerRepository playerRepo = new PlayerRepository();

        // 1. Create a set of players for testing
        System.out.println("\n[1/3] Seeding test database with players...");
        for (int i = 0; i < 5; i++) {
            String name = "CachePlayer_" + i + "_" + System.currentTimeMillis();
            playerRepo.create(new PlayerEntity(name, i % 2 == 0));
        }

        // 2. Measure JPQL Read performance (L2 & Query Cache)
        System.out.println("\n[2/3] Executing JPQL Queries to measure Cache Performance...");
        
        // Query 1: First invocation (Cache Miss - hits the relational database)
        long start1 = System.nanoTime();
        List<PlayerEntity> botsRun1 = playerRepo.findBots();
        long end1 = System.nanoTime();
        long duration1Us = (end1 - start1) / 1000;
        
        System.out.println(">>> RUN 1 (Cache Miss - Hits DB): " + duration1Us + " us. Found bots: " + botsRun1.size());

        // Query 2: Second invocation (Cache Hit - resolved entirely from Hibernate cache)
        long start2 = System.nanoTime();
        List<PlayerEntity> botsRun2 = playerRepo.findBots();
        long end2 = System.nanoTime();
        long duration2Us = (end2 - start2) / 1000;

        System.out.println(">>> RUN 2 (Cache Hit - L2 Query Cache): " + duration2Us + " us. Found bots: " + botsRun2.size());

        // Query 3: Third invocation (Cache Hit - resolved entirely from Hibernate cache)
        long start3 = System.nanoTime();
        List<PlayerEntity> botsRun3 = playerRepo.findBots();
        long end3 = System.nanoTime();
        long duration3Us = (end3 - start3) / 1000;

        System.out.println(">>> RUN 3 (Cache Hit - L2 Query Cache): " + duration3Us + " us. Found bots: " + botsRun3.size());

        // Calculate improvement factor
        double improvement = (double) duration1Us / duration2Us;
        System.out.println(String.format("\n[3/3] Cache read performance improvement: %.2fx faster!", improvement));
        System.out.println("==================================================");
    }
}
