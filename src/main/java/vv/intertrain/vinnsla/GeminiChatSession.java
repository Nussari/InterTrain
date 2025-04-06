package vv.intertrain.vinnsla;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

public class GeminiChatSession {
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";
    private final String apiKey;
    private final List<Map<String, String>> conversationHistory;
    private final ExecutorService executor;

    public GeminiChatSession(String apiKey) {
        this.apiKey = apiKey;
        this.conversationHistory = new ArrayList<>();
        this.executor = Executors.newFixedThreadPool(1);
    }

    /**
     * Sends a user message and receives AI response while maintaining conversation history
     * @param userMessage The message to send to the AI
     * @return The AI's response
     * @throws Exception if there's an API communication error
     */
    public String sendAndReceiveMsg(String userMessage) throws Exception {
        // Validate input
        if (userMessage == null || userMessage.trim().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be empty");
        }

        // Add user message to history
        addMessage("user", userMessage.trim());

        // Get AI response
        String aiResponse = sendRequest(buildRequest());

        // Add AI response to history
        addMessage("model", aiResponse);

        return aiResponse;
    }

    private String buildRequest() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"contents\":[");

        for (Map<String, String> msg : conversationHistory) {
            sb.append(String.format(
                    "{\"role\":\"%s\",\"parts\":[{\"text\":\"%s\"}]},",
                    msg.get("role"),
                    escapeJson(msg.get("content"))
            ));
        }

        if (!conversationHistory.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }

        sb.append("]}");
        return sb.toString();
    }

    private String sendRequest(String requestBody) throws Exception {
        URL url = new URL(API_URL + "?key=" + apiKey);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        // Print request for debugging
        //System.err.println("Sending request: " + requestBody);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(requestBody.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            String error = readStream(conn.getErrorStream());
            throw new RuntimeException("API Error " + responseCode + ": " + error);
        }

        String response = readStream(conn.getInputStream());
        // System.err.println("Raw response: " + response); // Debug output

        return extractTextFromResponse(response);
    }

    private String readStream(InputStream is) throws IOException {
        if (is == null) return "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }

    private String extractTextFromResponse(String json) {
        try {
            int start = json.indexOf("\"text\": \"") + 9;
            int end = json.indexOf("\"", start);
            return json.substring(start, end).replace("\\n", "\n");
        } catch (Exception e) {
            System.err.println("Failed to parse response: " + json);
            return "Sorry, I couldn't process that response.";
        }
    }

    private void addMessage(String role, String content) {
        conversationHistory.add(Map.of(
                "role", role,
                "content", content
        ));
    }

    private String escapeJson(String input) {
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    public List<Map<String, String>> getConversationHistory() {
        return this.conversationHistory;
    }
}