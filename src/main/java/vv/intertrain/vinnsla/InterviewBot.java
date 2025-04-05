package vv.intertrain.vinnsla;

import java.util.Scanner;

public class InterviewBot {

    // Config
    private static final int DEFAULT_MAX_QUESTIONS = 10;
    private final int MAX_QUESTIONS;

    // Job information
    private String name;
    private String company;
    private String jobTitle;

    private GeminiChatSession chatSession;

    private String initialPrompt;
    private String feedbackTriggerPrompt;
    private String feedback;

    // Interview status, true if ongoing, false if finished
    private Boolean isActive;
    // Total amount of questions asked so far
    private int questionCount;

    public InterviewBot(String name, String company, String jobTitle, int maxQuestionCount) {
        this.name = name;
        this.company = company;
        this.jobTitle = jobTitle;
        this.chatSession = new GeminiChatSession();
        this.initialPrompt = createInitialPrompt();
        this.isActive = true;
        this.questionCount = -1;

        if (maxQuestionCount <= 0) {
            this.MAX_QUESTIONS = DEFAULT_MAX_QUESTIONS;
        } else {
            this.MAX_QUESTIONS = maxQuestionCount;
        }
    }

    public InterviewBot(String name, String company, String jobTitle) {
        this(name, company, jobTitle, DEFAULT_MAX_QUESTIONS);
    }

    public InterviewBot(String name, String jobTitle, int maxQuestionCount) {
        this(name, "", jobTitle, maxQuestionCount);
    }

    public InterviewBot(String name, String jobTitle) {
        this(name, "", jobTitle, DEFAULT_MAX_QUESTIONS);
    }

    private String createInitialPrompt() {
        return String.format(
                "You are conducting a mock job interview, your name is Joe. The candidate is %s, applying for %s at %s. " +
                        "1. First conduct a natural conversation-style interview with different types of questions " +
                        "(behavioral, situational, technical). Keep it conversational and limit to %d questions.\n" +
                        "2. After exactly %d questions or when the candidate says 'end', provide feedback with:\n" +
                        "   - 3 specific strengths from their answers\n" +
                        "   - 3 concrete areas for improvement\n" +
                        "   - Reference their actual responses in your feedback. " +
                        "MAKE SURE TO LET THE CANDIDATE ANSWER, DO NOT ASK ALL QUESTIONS AT ONCE, ALSO DO NOT EXPLICITLY LET THE USER KNOW. SPEAK ICELANDIC",
                name,
                jobTitle,
                company,
                MAX_QUESTIONS,
                MAX_QUESTIONS
        );
    }

    // Starts the interview (sends the preprompt to the ai and returns the response)
    public String start() throws Exception {
        return this.respond(this.initialPrompt);
    }

    // Respond to the interviewer and get the answer back
    public String respond(String message) throws Exception {
        // Increments the question count
        this.questionCount++;

        // If the interview has reached the maximum amount of questions
        if (this.questionCount >= MAX_QUESTIONS) {
            this.feedbackTriggerPrompt = "The interview is now complete. Please provide the feedback as specified in the initial instructions.";
            this.feedback = chatSession.sendAndReceiveMsg(feedbackTriggerPrompt);
            System.out.println(this.feedback);

            this.isActive = false;
            return this.feedback;
        }

        // Sends the user's message and returns the interviewers response/next question
        return chatSession.sendAndReceiveMsg(message);
    }

    public Boolean getIsActiveStatus() {
        return this.isActive;
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        String nafn = "Jón";
        String vinnuStadur = "EFLA";
        String starfsHeiti = "Byggingaverkfræðingur";

        InterviewBot interview = new InterviewBot(nafn, vinnuStadur, starfsHeiti);
        String greeting = interview.start();

        System.out.println(greeting);

        for (int i = 0; i <= 4; i++) {
            System.out.println("Your answer: ");

            String inntak = scanner.nextLine().trim();

            String question = interview.respond(inntak);
            System.out.println("Question: " + question);
        }
    }
}
