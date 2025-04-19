package vv.intertrain.vinnsla;
/*
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

    // Questions and groups
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
/*
        return """
                {
                  "job_title": "Java Developer",
                  "workplace": "Acme Corp",
                  "groups": [
                    {
                      "group": "Technical",
                      "questions": [
                        "Can you explain the difference between an interface and an abstract class in Java?",
                        "How do you handle exceptions in Java applications?",
                        "What are some best practices for multithreaded programming in Java?"
                      ]
                    },
                    {
                      "group": "Behavioral",
                      "questions": [
                        "Describe a time you worked as part of a team to solve a difficult problem.",
                        "How do you handle tight deadlines and pressure?",
                        "Tell us about a situation where you had to quickly learn a new technology."
                      ]
                    }
                  ]
                }
                """;
    }

    // Starts the interview (sends the preprompt to the ai and returns the response)
    public String requestQuestions() throws Exception {
        // Sends the question request prompt and returns the questions
        String questionsJson = chatSession.sendAndReceiveMsg(this.questionRequestPrompt);
        // this.questions = parseQuestionsFromJson(questionsJson);
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
*/