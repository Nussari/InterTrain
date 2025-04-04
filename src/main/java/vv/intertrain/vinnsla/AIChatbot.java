package vv.intertrain.vinnsla;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public class AIChatbot {
    //private static final String API_KEY = System.getenv("API_KEY");
    private static final String API_KEY = "";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;
    private final ObjectMapper objectMapper;

    public AIChatbot() {
        this.objectMapper = new ObjectMapper();
    }

    public String sendMessage(String userMessage) {
        HttpURLConnection connection = null;
        try {
            // Create URL and open connection
            URL url = new URL(API_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Create JSON request body
            ObjectNode requestJson = objectMapper.createObjectNode();
            ArrayNode contents = requestJson.putArray("contents");
            ObjectNode contentItem = contents.addObject();
            ArrayNode parts = contentItem.putArray("parts");
            ObjectNode textNode = parts.addObject();
            textNode.put("text", userMessage);

            String jsonRequest = objectMapper.writeValueAsString(requestJson);

            // Send JSON data to API
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonRequest.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Read response
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line.trim());
                    }
                    return extractAIResponse(response.toString());
                }
            } else {
                return "Error: HTTP " + responseCode;
            }
        } catch (IOException e) {
            return "Error communicating with AI: " + e.getMessage();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private String extractAIResponse(String jsonResponse) throws IOException {
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        JsonNode contentNode = rootNode.path("candidates").get(0).path("content").path("parts").get(0).path("text");

        String rawText = contentNode.asText();

        // Remove markdown-style code block markers (```json ... ```)
        return rawText.replaceAll("(?s)^```json\\s*", "").replaceAll("(?s)```\\s*$", "").trim();
    }
}
