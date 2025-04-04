package vv.intertrain.vinnsla;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AIInterviewer {
    private final List<String> conversationHistory = new ArrayList<>();
    private final InterviewQuestionsStorage storage = new InterviewQuestionsStorage();
    private String name;
    private String company;
    private String jobTitle;
    private AIChatbot ai;

    private final int questionsPerType = 5;

    private String prompt;

    public AIInterviewer(String name, String company, String jobTitle) {
        this.name = name;
        this.company = company;
        this.jobTitle = jobTitle;
        this.ai = new AIChatbot();
        buildPrompt();
    }

    public AIInterviewer(String name, String jobTitle) {
        this.name = name;
        this.jobTitle = jobTitle;
        this.ai = new AIChatbot();
        buildPrompt();
    }

    public void buildPrompt() {
        this.prompt = "Generate n=" + questionsPerType + " different types of job interview questions (5 per type) for the following job role: " + this.getJobTitle() + " at company: " + this.getCompany() + ". \n" +
                "The response should be structured in JSON format like this:\n" +
                "\n" +
                "{\n" +
                "  \"Behavioral\": [\n" +
                "    \"Question 1\",\n" +
                "    ...\n" +
                "    \"Question n\"\n" +
                "  ],\n" +
                "  \"Technical\": [\n" +
                "    \"Question 1\",\n" +
                "    ...\n" +
                "    \"Question n\"\n" +
                "  ],\n" +
                "  \"Situational\": [\n" +
                "    \"Question 1\",\n" +
                "    ...\n" +
                "    \"Question n\"\n" +
                "  ],\n" +
                "  \"Cultural Fit\": [\n" +
                "    \"Question 1\",\n" +
                "    ...\n" +
                "    \"Question n\"\n" +
                "  ],\n" +
                "  \"General Knowledge\": [\n" +
                "    \"Question 1\",\n" +
                "    ...\n" +
                "    \"Question n\"\n" +
                "  ]\n" +
                "}\n" +
                "\n" +
                "Ensure the output is **pure JSON** without extra text or explanation, ignore company if empty.";
    }

    public String getName() {
        return this.name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public String getCompany() {
        return this.company;
    }

    public void setCompany(String newCompany) {
        this.company = newCompany;
        buildPrompt();
    }

    public String getJobTitle() {
        return this.jobTitle;
    }

    public void setJobTitle(String newJobTitle) {
        this.jobTitle = newJobTitle;
        buildPrompt();
    }

    public InterviewQuestionsStorage getStorage() {
        return this.storage;
    }

    public String requestQuestions() {
        String jsonResponse = ai.sendMessage(prompt);
        storage.storeAllQuestions(jsonResponse);
        return jsonResponse;
    }

    public static void main(String[] args) throws IOException {
        AIInterviewer interviewer = new AIInterviewer("John Doe", "Hreyfill", "Taxi Driver");

        String response = interviewer.requestQuestions();
        System.out.println("Response: " + response);

        InterviewQuestionsStorage storage = interviewer.getStorage();

        for (Map.Entry<String, List<String>> entry : storage.getQuestions().entrySet()) {
            String type = entry.getKey();
            List<String> questions = entry.getValue();

            System.out.println("Question Type: " + type);
            for (String question : questions) {
                System.out.println("- " + question);
            }
            System.out.println(); // For spacing between types
        }

    }
}
