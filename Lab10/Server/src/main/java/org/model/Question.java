package org.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public class Question {
    private String text;
    private List<String> options;
    private int correctIndex;

    public boolean isCorrect(int index) {
        return index == correctIndex;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(text).append("\n");
        for (int i = 0; i < options.size(); i++) {
            sb.append(i).append(": ").append(options.get(i)).append("\n");
        }
        return sb.toString();
    }

    public static Question fromString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 3) return null;
        String text = parts[0];
        List<String> options = Arrays.asList(parts[1], parts[2], parts[3], parts[4]);
        int correctIndex = Integer.parseInt(parts[5]);
        return new Question(text, options, correctIndex);
    }
}
