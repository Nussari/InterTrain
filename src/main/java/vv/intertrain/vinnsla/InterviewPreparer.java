package vv.intertrain.vinnsla;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import vv.intertrain.vinnsla.com.logaritex.ai.api.AssistantApi;
import vv.intertrain.vinnsla.com.logaritex.ai.api.Data.Assistant;
import vv.intertrain.vinnsla.com.logaritex.ai.api.Data;
import vv.intertrain.vinnsla.com.logaritex.ai.api.Data.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InterviewPreparer {

    private static final String ASSISTANT_ID = "asst_vLMQ1M855Jt8UPPi2S4CTroW";
    private static final String FEEDBACK_ASSISTANT_ID = "asst_k79wj6VICKkqeYsNt32i9LoU";
    private static final String BASE_URL = "https://api.openai.com/v1/";

    private final AssistantApi assistantApi;
    private final Assistant assistant;

    private final Assistant feedbackAssistant;

    public InterviewPreparer() {

        String apiKey = System.getenv("OPENAI_KEY");

        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("Missing OPENAI_KEY environment variable");
            System.exit(1);
        }

        // Retrieves a question assistant and a feedback assistant
        this.assistantApi = new AssistantApi(apiKey);

        // Assistant for getting interview questions
        this.assistant = assistantApi.retrieveAssistant(ASSISTANT_ID);

        // Assistant for feedback for each question
        this.feedbackAssistant = assistantApi.retrieveAssistant(FEEDBACK_ASSISTANT_ID);
    }

    public Map<String, ObservableList<String>> requestQuestions(String jobTitle, String workplace, List<String> groups) throws InterruptedException {
        // Convert the list of groups to a JSON array string
        String groupsJson = groups.stream()
                .map(g -> "\"" + g + "\"")
                .collect(Collectors.joining(", "));

        Data.Thread thread = assistantApi.createThread(new ThreadRequest());

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

        return convertMessagesToMap(assistantMessages);
    }

    public Map<String, ObservableList<String>> convertMessagesToMap(List<Data.Message> assistantMessages) {
        Map<String, ObservableList<String>> resultMap = new HashMap<>();

        try {
            for (Data.Message message : assistantMessages) {
                // Assuming the message content is a JSON structure or similar key-value format
                String contentJson = message.content().get(0).text().value(); // Adjust based on actual structure

                // Parse the content (assuming it's in JSON format) - You'll likely use Jackson or Gson for this
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(contentJson);

                // Extract key-value pairs from the content
                JsonNode groupsNode = rootNode.get("groups");

                if (groupsNode != null && groupsNode.isObject()) {
                    groupsNode.fields().forEachRemaining(entry -> {
                        String group = entry.getKey();
                        List<String> questions = new ArrayList<>();
                        entry.getValue().forEach(questionNode -> questions.add(questionNode.asText()));

                        // Convert the List<String> to ObservableList<String>
                        ObservableList<String> observableQuestions = FXCollections.observableArrayList(questions);

                        // Put the result in the map
                        resultMap.put(group, observableQuestions);
                    });
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;
    }

    // Takes in the question and the user's answer
    // Returns the AI's feedback for the user's answer to the question as a String
    public String requestFeedback(String question, String answer) throws InterruptedException {
        // Build the JSON string dynamically
        String messageContent = String.format("""
                {
                    "question": "%s",
                    "answer": "%s"
                }
                """, question, answer);

        Data.Thread feedbackThread = assistantApi.createThread(new ThreadRequest());

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

        return assistantMessages.getFirst().content().getFirst().text().value();
    }

    public static void main(String[] args) throws InterruptedException {
        InterviewPreparer ai = new InterviewPreparer();

        List<String> groups = List.of("Technical", "Communication", "Teamwork", "Problem solving");

        System.out.println(ai.requestQuestions("Byggingaverkfræðingur", "EFLA", groups));
    }
}
