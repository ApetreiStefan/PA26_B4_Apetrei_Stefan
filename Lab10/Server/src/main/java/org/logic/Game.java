package org.logic;

import org.model.Player;
import org.model.Question;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Game {
    private List<Question> questions = new ArrayList<>();
    private Map<String, Player> players = new ConcurrentHashMap<>();
    private boolean started = false;
    private boolean aborted = false;
    private int currentQuestionIndex = 0;
    private static final int TIME_LIMIT_MS = 10000; // 10 seconds per question

    public Game() {
        loadQuestions();
    }

    private void loadQuestions() {
        try (InputStream is = getClass().getResourceAsStream("/questions.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Question q = Question.fromString(line);
                if (q != null) questions.add(q);
            }
        } catch (Exception e) {
            System.err.println("Error loading questions: " + e.getMessage());
            // Add some default questions if file not found
            questions.add(new Question("Default Q?", Arrays.asList("A", "B", "C", "D"), 0));
        }
    }

    public synchronized void addPlayer(String name, boolean isBot) {
        if (!started) {
            players.put(name, new Player(name, isBot));
        }
    }

    public void start() {
        this.started = true;
    }

    public boolean isStarted() {
        return started;
    }

    public Question getCurrentQuestion() {
        if (currentQuestionIndex < questions.size()) {
            return questions.get(currentQuestionIndex);
        }
        return null;
    }

    public synchronized void submitAnswer(String playerName, int answerIndex, long responseTime) {
        if (playerName == null) return;
        Player player = players.get(playerName);
        if (player != null && currentQuestionIndex < questions.size()) {
            Question q = questions.get(currentQuestionIndex);
            if (q.isCorrect(answerIndex) && responseTime <= TIME_LIMIT_MS) {
                player.addScore(1);
            }
            player.addResponseTime(responseTime);
        }
    }

    public synchronized void nextQuestion() {
        currentQuestionIndex++;
    }

    public boolean isFinished() {
        return aborted || currentQuestionIndex >= questions.size();
    }

    public void abort() {
        this.aborted = true;
    }

    public List<Player> getResults() {
        return players.values().stream()
                .sorted(Comparator.comparingInt(Player::getScore).reversed()
                        .thenComparingLong(Player::getTotalResponseTime))
                .collect(Collectors.toList());
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public int getTimeLimit() {
        return TIME_LIMIT_MS;
    }
}
