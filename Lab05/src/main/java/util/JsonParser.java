package util;

import repo.Resource;
import java.io.*;

public class JsonParser implements AutoCloseable {
    private final BufferedReader reader;

    public JsonParser(String path) {
        try {
            this.reader = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + path, e);
        }
    }

    /**
     * Reads a single JSON object from the current line.
     */
    public Resource readResource() {
        StringBuilder jsonObjectBuilder = new StringBuilder();
        String line;
        boolean insideObject = false;

        try {
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.contains("{")) insideObject = true;
                if (insideObject) {
                    jsonObjectBuilder.append(line);
                }

                if (line.contains("}")) {
                    String fullJson = jsonObjectBuilder.toString();
                    return new Resource(
                            getField(fullJson, "id"),
                            getField(fullJson, "title"),
                            getField(fullJson, "location"),
                            getField(fullJson, "year"),
                            getField(fullJson, "author")
                    );
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON", e);
        }
        return null;
    }

    /**
     * Extracts value for a given key.
     * Handles "key":"value"
     */
    private String getField(String line, String key) {
        try {
            String search = "\"" + key + "\"";
            int keyIndex = line.indexOf(search);
            if (keyIndex == -1) return null;

            int colonIndex = line.indexOf(":", keyIndex + search.length());
            int startQuote = line.indexOf("\"", colonIndex) + 1;
            int endQuote = line.indexOf("\"", startQuote);

            return line.substring(startQuote, endQuote);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void close() {
        try {
            if (reader != null) reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}