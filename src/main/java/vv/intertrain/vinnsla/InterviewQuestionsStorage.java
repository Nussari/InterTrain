package vv.intertrain.vinnsla;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

public class InterviewQuestionsStorage {
    private final Map<String, List<String>> questionsByType = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Store entire JSON-formatted string (map of types to questions)
    public void storeAllQuestions(String json) {
        try {
            Map<String, List<String>> parsed = objectMapper.readValue(json, Map.class);
            questionsByType.putAll(parsed);
        } catch (JsonProcessingException e) {
            System.err.println("Failed to parse questions JSON: " + e.getMessage());
        }
    }

    // Store a single type of questions
    public void storeQuestions(String type, List<String> questions) {
        questionsByType.put(type, questions);
    }

    // Retrieve questions by type
    public List<String> getQuestionsByType(String type) {
        return questionsByType.getOrDefault(type, new ArrayList<>());
    }

    // Get all questions
    public Map<String, List<String>> getQuestions() {
        return questionsByType;
    }

    // Optional: Print all stored questions
    public void printAll() {
        questionsByType.forEach((type, questions) -> {
            System.out.println("Type: " + type);
            questions.forEach(q -> System.out.println(" - " + q));
        });
    }
}
