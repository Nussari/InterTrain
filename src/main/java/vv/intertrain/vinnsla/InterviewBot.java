package vv.intertrain.vinnsla;

import java.util.Objects;
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

    // 0 is prep mode, 1 is interview
    private String chatType;

    // Interview status, true if ongoing, false if finished
    private Boolean isActive;
    // Total amount of questions asked so far
    private int questionCount;

    public InterviewBot(String name, String company, String jobTitle, int maxQuestionCount, String chatType) {
        this.name = name;
        this.company = company;
        this.jobTitle = jobTitle;
        this.chatSession = new GeminiChatSession("API_LYKILL"); // API lykill fer hingað
        this.initialPrompt = createInitialPrompt();
        this.isActive = true;
        this.questionCount = -1;
        this.chatType = chatType;

        if (maxQuestionCount <= 0) {
            this.MAX_QUESTIONS = DEFAULT_MAX_QUESTIONS;
        } else {
            this.MAX_QUESTIONS = maxQuestionCount;
        }
    }

    public InterviewBot(String name, String company, String jobTitle, String chatType) {
        this(name, company, jobTitle, DEFAULT_MAX_QUESTIONS, chatType);
    }

    public InterviewBot(String name, String jobTitle, int maxQuestionCount, String chatType) {
        this(name, "", jobTitle, maxQuestionCount, chatType);
    }

    public InterviewBot(String name, String jobTitle, String chatType) {
        this(name, "", jobTitle, DEFAULT_MAX_QUESTIONS, chatType);
    }

    private String createInitialPrompt() {
        System.out.println("Prep: " + this.chatType);
        if (Objects.equals(this.chatType, "prep")) {
            return String.format(
                    "Þú ert starfsráðgjafi sem hjálpar við undirbúning fyrir viðtal. Viðskiptavinurinn er %s sem er að sækja um starf sem %s hjá %s. " +
                            "1. Byrjaðu á stuttri kynningu á því hvað verður gert í þessu undirbúningsfundi\n" +
                            "2. Farðu í gegnum eftirfarandi þætti:\n" +
                            "   - Algengar viðtalsspurningar (hefðbundnar, hegðunar- og aðstæðuspurningar)\n" +
                            "   - Dæmi um góð svör\n" +
                            "   - Dæmi um slæm svör\n" +
                            "   - Leiðbeiningar um hvernig eigi að svara STAR (Situation, Task, Action, Result) aðferðinni\n" +
                            "3. Bjóddu upp á dæmisvör fyrir:\n" +
                            "   - 'Segðu mér frá þér'\n" +
                            "   - 'Hver er stærsti styrkur þinn?'\n" +
                            "   - 'Lýstu því hvernig þún leystir vanda'\n" +
                            "4. Í lokin:\n" +
                            "   - Dragðu saman lykilatriði\n" +
                            "   - Hvetdu viðskiptavininn til að æfa svör hávær\n" +
                            "   - Bíddu eftir spurningum\n\n" +
                            "Gakktu úr skugga um:\n" +
                            "- Að halda samtalinu náttúrulega og gagnvirku\n" +
                            "- Að láta notandann koma orði á milli á\n" +
                            "- Að nota einfalt og skýrt máli\n" +
                            "- Að setja áherslu á það sem er mikilvægt fyrir %s\n" +
                            "- Að tala á ÍSLENSKU",
                    name,
                    jobTitle,
                    company
            );
        }
        return String.format(
                "You are conducting a mock job interview, your name is Joe. The candidate is %s, applying for %s at %s. " +
                        "1. First conduct a natural conversation-style interview with different types of questions " +
                        "(behavioral, situational, technical). Keep it conversational and limit to %d questions.\n" +
                        "2. After exactly %d questions or when the candidate says 'end', provide feedback with:\n" +
                        "   - 3 specific strengths from their answers\n" +
                        "   - 3 concrete areas for improvement\n" +
                        "   - Reference their actual responses in your feedback. " +
                        "MAKE SURE TO LET THE CANDIDATE ANSWER, DO NOT ASK ALL QUESTIONS AT ONCE, ALSO DO NOT EXPLICITLY LET THE USER KNOW. SPEAK ICELANDIC!",
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

    public boolean getIsActiveStatus() {
        return this.isActive;
    }
}
