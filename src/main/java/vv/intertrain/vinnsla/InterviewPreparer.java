package vv.intertrain.vinnsla;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import com.google.gson.*;
import java.util.*;

public class InterviewPreparer {

    // Job information
    private String company;
    private String jobTitle;

    private GeminiChatSession chatSession;

    private String questionRequestPrompt;
    private String feedbackTriggerPrompt;
    private String feedback;

    private int questionsPerGroup;
    // Total amount of questions answered so far
    private int answerCount;

    // Questions and grousp
    private final Map<String, ObservableList<String>> questions = new HashMap<>();
    private final ObservableList<String> groups = FXCollections.observableArrayList();

    public InterviewPreparer(String company, String jobTitle, int questionsPerGroup) {

        String apiKey = System.getenv("GEMINI_KEY");

        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("Missing API_KEY environment variable");
            System.exit(1);
        }

        this.company = company;
        this.jobTitle = jobTitle;
        this.chatSession = new GeminiChatSession(apiKey); // API lykill fer hingað
        this.groups.add("Skill questions");
        this.groups.add("Technical questions");

        this.questionRequestPrompt = createPrompt();
        this.questionsPerGroup = questionsPerGroup;
        this.answerCount = 0;
    }

    private String createPrompt() {

        // Build the group array for the JSON
        StringBuilder groupsJson = new StringBuilder();
        groupsJson.append("[");
        for (int i = 0; i < groups.size(); i++) {
            groupsJson.append("\"").append(groups.get(i)).append("\"");
            if (i < groups.size() - 1) {
                groupsJson.append(", ");
            }
        }
        groupsJson.append("]");

        /*
        return String.format(
                "You are an expert interview assistant.\n" +
                        "Here below I have provided you with the following data:\n" +
                        "\n" +
                        "A list of question groups (e.g., technical, behavioral, leadership, etc.)\n" +
                        "The job title\n" +
                        "Optionally, the workplace name\n" +
                        "{\n" +
                        "  \"job_title\": \"%s\",\n" +
                        "  \"workplace\": \"%s\",\n" +
                        "  \"groups\": %s\n" +
                        "}\n" +
                        "Your tasks:\n" +
                        "\n" +
                        "1. For each question group, generate exactly %s high-quality, relevant interview questions.\n" +
                        "2. Ensure the questions are tailored to the job title (and workplace, if provided).\n" +
                        "3. Respond to this message in the following strict JSON format only, and DO NOT include any text outside the JSON:\n" +
                        "{\n" +
                        "      \"group\": \"<group name>\",\n" +
                        "      \"questions\": [\n" +
                        "        \"<question 1>\",\n" +
                        "        \"<question 2>\",\n" +
                        "        \"... up to %s questions ...\"\n" +
                        "      ]\n" +
                        "    }\n" +
                        "    // ...repeat for each group...\n" +
                        "  ]\n" +
                        "}\n",
                jobTitle,
                company,
                groupsJson.toString(),
                questionsPerGroup,
                questionsPerGroup
        );
        */
        return "{\n" +
                "  \"job_title\": \"Java Developer\",\n" +
                "  \"workplace\": \"Acme Corp\",\n" +
                "  \"groups\": [\n" +
                "    {\n" +
                "      \"group\": \"Technical\",\n" +
                "      \"questions\": [\n" +
                "        \"Can you explain the difference between an interface and an abstract class in Java?\",\n" +
                "        \"How do you handle exceptions in Java applications?\",\n" +
                "        \"What are some best practices for multithreaded programming in Java?\"\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"group\": \"Behavioral\",\n" +
                "      \"questions\": [\n" +
                "        \"Describe a time you worked as part of a team to solve a difficult problem.\",\n" +
                "        \"How do you handle tight deadlines and pressure?\",\n" +
                "        \"Tell us about a situation where you had to quickly learn a new technology.\"\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";
    }

    // Starts the interview (sends the preprompt to the ai and returns the response)
    public String requestQuestions() throws Exception {
        // Sends the question request prompt and returns the questions
        String questionsJson = chatSession.sendAndReceiveMsg(this.questionRequestPrompt);
        this.questions = parseQuestionsFromJson(questionsJson);
    }
    private final Map<String, ObservableList<String>> questions = new HashMap<>();

    public Map<String, ObservableList<String>> parseQuestionsFromJson(String json) {
        Map<String, ObservableList<String>> result = new HashMap<>();

        Gson gson = new Gson();
        JsonObject root = JsonParser.parseString(json).getAsJsonObject();
        JsonArray groupsArray = root.getAsJsonArray("groups");

        for (JsonElement groupElem : groupsArray) {
            JsonObject groupObj = groupElem.getAsJsonObject();
            String groupName = groupObj.get("group").getAsString();
            JsonArray questionsArray = groupObj.getAsJsonArray("questions");

            // Parse questions into a List<String>
            Type listType = new TypeToken<List<String>>() {}.getType();
            List<String> questions = gson.fromJson(questionsArray, listType);

            // Add to result as ObservableList
            result.put(groupName, FXCollections.observableArrayList(questions));
        }

        return result;
    }


    public Map<String, ObservableList<String>> getQuestions() {
        return this.questions;
    }

    public ObservableList<String> getGroups() {
        return this.groups;
    }

    public String resendLast() throws Exception {
        return chatSession.resendLastMessage();
    }
}
