package vv.intertrain.vinnsla;
import vv.intertrain.vinnsla.com.logaritex.ai.api.AssistantApi;
import vv.intertrain.vinnsla.com.logaritex.ai.api.Data.Assistant;
import vv.intertrain.vinnsla.com.logaritex.ai.api.Data;
import vv.intertrain.vinnsla.com.logaritex.ai.api.Data.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



import okhttp3.*;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;
import java.util.*;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



public class OpenAIAssistantWrapper {

    private static final String ASSISTANT_ID = "asst_vLMQ1M855Jt8UPPi2S4CTroW";
    private static final String FEEDBACK_ASSISTANT_ID = "asst_k79wj6VICKkqeYsNt32i9LoU";
    private static final String BASE_URL = "https://api.openai.com/v1/";

    private final AssistantApi assistantApi;
    private final Assistant assistant;

    private final Assistant feedbackAssistant;
    private final Data.Thread thread;
    private final Data.Thread feedbackThread;

    public OpenAIAssistantWrapper() {

        String apiKey = System.getenv("OPENAI_KEY");

        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("Missing OPENAI_KEY environment variable");
            System.exit(1);
        }

        this.assistantApi = new AssistantApi(apiKey);
        this.assistant = assistantApi.retrieveAssistant(ASSISTANT_ID);
        this.feedbackAssistant = assistantApi.retrieveAssistant(FEEDBACK_ASSISTANT_ID);
        this.thread = assistantApi.createThread(new ThreadRequest());
        this.feedbackThread = assistantApi.createThread(new ThreadRequest());
    }

    public String requestQuestions(String jobTitle, String workplace, List<String> groups) throws InterruptedException {
        // Convert the list of groups to a JSON array string
        String groupsJson = groups.stream()
                .map(g -> "\"" + g + "\"")
                .collect(Collectors.joining(", "));

        // Build the JSON string dynamically
        String messageContent = String.format("""
                {
                    "job_title": "%s",
                    "workplace": "%s",
                    "groups": [%s]
                }
                """, jobTitle, workplace, groupsJson);

        assistantApi.createMessage(new Data.MessageRequest(
                        Data.Role.user, // user created message.
                        messageContent), // user question.
                thread.id()); // thread to add the message to.

        Data.Run run = assistantApi.createRun(
                thread.id(), // run this thread,
                new Data.RunRequest(assistant.id())); // with this assistant.

        while (assistantApi.retrieveRun(thread.id(), run.id()).status() != Data.Run.Status.completed) {
            java.lang.Thread.sleep(500);
        }

        // Display the Assistant's Response
        Data.DataList<Data.Message> messages = assistantApi.listMessages(
                new Data.ListRequest(), thread.id());

        // Filter out the assistant messages only.
        List<Data.Message> assistantMessages = messages.data().stream()
                .filter(msg -> msg.role() == Data.Role.assistant).toList();

        Data.DataList<Data.Message> newMessages = assistantApi.listMessages(new Data.ListRequest(), thread.id());

        return extractGroupsFromMessages(newMessages);
    }

    public String extractGroupsFromMessages(Data.DataList<Data.Message> messages) {
        try {
            // Filter assistant messages
            List<Data.Message> assistantMessages = messages.data().stream()
                    .filter(msg -> msg.role() == Data.Role.assistant)
                    .toList();

            if (assistantMessages.isEmpty()) {
                return "No assistant messages found.";
            }

            // Get the first assistant message's content (assuming it's text-based)
            String contentJson = assistantMessages.get(0)
                    .content()
                    .get(0)
                    .text()
                    .value(); // Adjust this if the structure is different

            // Parse the JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(contentJson);

            // Extract the "groups" field
            JsonNode groupsNode = rootNode.get("groups");

            if (groupsNode == null) {
                return "No 'groups' field found in assistant message.";
            }

            // Return it as a pretty JSON string
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(groupsNode);

        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing assistant message.";
        }
    }


    public String requestFeedback(String question, String answer) throws InterruptedException {
        // Build the JSON string dynamically
        String messageContent = String.format("""
                {
                    "question": "%s",
                    "answer": "%s"
                }
                """, question, answer);

        assistantApi.createMessage(new Data.MessageRequest(
                        Data.Role.user, // user created message.
                        messageContent), // user question.
                feedbackThread.id()); // thread to add the message to.

        Data.Run run = assistantApi.createRun(
                feedbackThread.id(), // run this thread,
                new Data.RunRequest(feedbackAssistant.id())); // with this assistant.

        while (assistantApi.retrieveRun(feedbackThread.id(), run.id()).status() != Data.Run.Status.completed) {
            java.lang.Thread.sleep(500);
        }

        // Display the Assistant's Response
        Data.DataList<Data.Message> messages = assistantApi.listMessages(
                new Data.ListRequest(), feedbackThread.id());

        // Filter out the assistant messages only.
        List<Data.Message> assistantMessages = messages.data().stream()
                .filter(msg -> msg.role() == Data.Role.assistant).toList();

        return assistantMessages.toString();
    }


    public static void main(String[] args) throws InterruptedException {
        OpenAIAssistantWrapper ai = new OpenAIAssistantWrapper();

        List<String> groups = List.of("Technical", "Communication");

        System.out.println(ai.requestQuestions("Byggingaverkfræðingur", "Trade Info", groups));

        System.out.println(ai.requestFeedback("How good are you at Excel", "Pretty good"));
    }
}
