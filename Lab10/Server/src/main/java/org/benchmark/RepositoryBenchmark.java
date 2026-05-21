package org.benchmark;

import org.entity.GameEntity;
import org.entity.PlayerEntity;
import org.entity.ResultEntity;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.repository.GameRepository;
import org.repository.PlayerRepository;
import org.repository.ResultRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 2, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(0)
public class RepositoryBenchmark {

    private PlayerRepository playerRepository;
    private ResultRepository resultRepository;
    private String searchName;

    @Setup
    public void setup() {
        playerRepository = new PlayerRepository();
        resultRepository = new ResultRepository();
        GameRepository gameRepository = new GameRepository();

        // Seed the DB with benchmark test data
        searchName = "BenchPlayer_" + System.currentTimeMillis();
        PlayerEntity player = new PlayerEntity(searchName, true);
        playerRepository.create(player);

        GameEntity game = new GameEntity();
        ResultEntity result = new ResultEntity(player, game, 95, 2000);
        game.addResult(result);
        gameRepository.create(game);
    }

    @Benchmark
    public PlayerEntity testFindPlayerByName() {
        return playerRepository.findByName(searchName);
    }

    @Benchmark
    public List<ResultEntity> testDynamicSearchCriteria() {
        return resultRepository.findResultsWithFilters(
                "BenchPlayer",     // prefix
                80,               // min score
                true,             // is bot
                LocalDateTime.now().minusDays(1) // after yesterday
        );
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(RepositoryBenchmark.class.getSimpleName())
                .forks(0)
                .warmupIterations(2)
                .measurementIterations(3)
                .build();
        new Runner(opt).run();
    }
}
