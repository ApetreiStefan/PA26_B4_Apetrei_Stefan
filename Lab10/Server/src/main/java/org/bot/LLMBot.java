package org.bot;

import org.logic.Game;
import org.model.Question;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Collectors;

/**
 * LLM-based AI Bot.
 * In a real scenario, this would call OpenAI, Anthropic, or Google Gemini API.
 * For this lab, it uses a mock response or a simple logic to simulate
 * difficulty levels.
 */
public class LLMBot implements BotPlayer, Runnable {
    private final String name;
    private final Game game;
    private final String difficulty; // "Easy", "Medium", "Hard"
    private static final String API_KEY = "AIzaSyBhaLmJ1vVpkmoljQMb6VEOZg-3xQLxE68";
    private static final String MODEL = "gemini-2.5-flash";

    public LLMBot(String name, Game game, String difficulty) {
        this.name = name;
        this.game = game;
        this.difficulty = difficulty;
        game.addPlayer(name, true);
    }

    @Override
    public void play(Game game) {
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (!game.isFinished()) {
            if (game.isStarted()) {
                Question q = game.getCurrentQuestion();
                if (q != null) {
                    try {
                        // Simulate API latency
                        long latency = 2000 + (long) (Math.random() * 2000);
                        Thread.sleep(latency);

                        // Call Gemma API
                        int answer = getAIAnswer(q);

                        game.submitAnswer(name, answer, latency);
                        System.out.println(name + " (Gemini-" + difficulty + ") answered: " + answer);

                        while (q == game.getCurrentQuestion() && !game.isFinished()) {
                            Thread.sleep(500);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    private int getAIAnswer(Question q) {
        if (API_KEY.equals("YOUR_GEMMA_API_KEY_HERE")) {
            // Fallback if no API key
            return (int) (Math.random() * 4);
        }

        try {
            String prompt = String.format(
                    "You are a quiz contestant. Answer this question by providing ONLY the number of the correct option (0, 1, 2, or 3).\n\n"
                            +
                            "Question: %s\n" +
                            "Options:\n%s\n\n" +
                            "Answer index:",
                    q.getText(),
                    java.util.stream.IntStream.range(0, q.getOptions().size())
                            .mapToObj(i -> i + ": " + q.getOptions().get(i))
                            .collect(Collectors.joining("\n")));

            JSONObject requestBody = new JSONObject()
                    .put("contents", new JSONArray()
                            .put(new JSONObject()
                                    .put("parts", new JSONArray()
                                            .put(new JSONObject().put("text", prompt)))));

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models/" + MODEL
                            + ":generateContent?key=" + API_KEY))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONObject jsonResponse = new JSONObject(response.body());
                String text = jsonResponse.getJSONArray("candidates")
                        .getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text")
                        .trim();

                // Extract the first number found in the response
                String numericOnly = text.replaceAll("[^0-3]", "");
                if (!numericOnly.isEmpty()) {
                    return Integer.parseInt(numericOnly.substring(0, 1));
                }
            } else {
                System.err.println("Gemma API Error: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            System.err.println("Error calling Gemma API: " + e.getMessage());
        }
        return (int) (Math.random() * 4);
    }
}
