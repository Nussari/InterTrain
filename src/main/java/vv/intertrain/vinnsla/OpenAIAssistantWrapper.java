package vv.intertrain.vinnsla;
import vv.intertrain.vinnsla.com.logaritex.ai.api.AssistantApi;
import vv.intertrain.vinnsla.com.logaritex.ai.api.Data.Assistant;
import vv.intertrain.vinnsla.com.logaritex.ai.api.Data;
import vv.intertrain.vinnsla.com.logaritex.ai.api.Data.*;



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
    private static final String BASE_URL = "https://api.openai.com/v1/";

    private final AssistantApi assistantApi;
    private final Assistant assistant;
    private final Data.Thread thread;

    public OpenAIAssistantWrapper() {
        String apiKey = System.getenv("OPENAI_KEY");

        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("Missing OPENAI_KEY environment variable");
            System.exit(1);
        }

        this.assistantApi = new AssistantApi(apiKey);
        this.assistant = assistantApi.retrieveAssistant(ASSISTANT_ID);
        this.thread = assistantApi.createThread(new ThreadRequest());
    }

    public String requestQuestions(String jobTitle, String workplace, List<String> groups) {
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

        // Create and send the message
        assistantApi.createMessage(
                new Data.MessageRequest(
                        Data.Role.user,
                        messageContent
                ),
                thread.id()
        );

        // Display the Assistant's Response
        Data.DataList<Data.Message> messages = assistantApi.listMessages(
                new Data.ListRequest(), thread.id());

        // Filter out the assistant messages only.
        List<Data.Message> assistantMessages = messages.data().stream()
                .filter(msg -> msg.role() == Data.Role.assistant).toList();

        System.out.println(assistantMessages);

        return "";
    }

    public static void main(String[] args) {
        OpenAIAssistantWrapper ai = new OpenAIAssistantWrapper();

        List<String> groups = List.of("Technical", "Communication");

        ai.requestQuestions("Byggingaverkfræðingur", "EFLA", groups);
    }

    /*
    public static void main(String[] args) throws InterruptedException {
        // Connect to the assistant-api using your OpenAI api key.

        // Get the assistant.
        Assistant assistant = assistantApi.retrieveAssistant("asst_vLMQ1M855Jt8UPPi2S4CTroW");



        // Create an empty Thread.
        Data.Thread thread = assistantApi.createThread(new ThreadRequest());

        // System.out.println("Thread ID: " + thread.id());

        assistantApi.createMessage(new Data.MessageRequest(
            Data.Role.user, // user created message.
            "I need to solve the equation `3x + 11 = 14`. Can you help me?"), // user question.
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

        System.out.println(assistantMessages);
    }
    */
}
