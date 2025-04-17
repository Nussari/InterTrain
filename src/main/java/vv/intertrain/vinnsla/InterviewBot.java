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
    private final ChatMode mode;

    // Interview status, true if ongoing, false if finished
    private Boolean isActive;
    // Total amount of questions asked so far
    private int questionCount;

    public InterviewBot(String name, String company, String jobTitle, int maxQuestionCount, ChatMode mode) {

        String apiKey = System.getenv("GEMINI_KEY");

        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("Missing GEMINI_KEY environment variable");
            System.exit(1);
        }

        this.name = name;
        this.company = company;
        this.jobTitle = jobTitle;
        this.mode = mode;
        this.chatSession = new GeminiChatSession(apiKey); // API lykill fer hingað
        this.initialPrompt = createInitialPrompt();
        this.isActive = true;
        this.questionCount = -1;

        if (maxQuestionCount <= 0) {
            this.MAX_QUESTIONS = DEFAULT_MAX_QUESTIONS;
        } else {
            this.MAX_QUESTIONS = maxQuestionCount;
        }
    }

    public InterviewBot(String name, String company, String jobTitle, ChatMode mode) {
        this(name, company, jobTitle, DEFAULT_MAX_QUESTIONS, mode);
    }

    public InterviewBot(String name, String jobTitle, int maxQuestionCount, ChatMode mode) {
        this(name, "", jobTitle, maxQuestionCount, mode);
    }

    public InterviewBot(String name, String jobTitle, ChatMode mode) {
        this(name, "", jobTitle, DEFAULT_MAX_QUESTIONS, mode);
    }

    private String createInitialPrompt() {
        if (this.mode == ChatMode.PREPARATION) {
            System.out.println("Preparation mode");
            return String.format(
                    "Þú ert starfsráðgjafi sem heitir Jón sem hjálpar við undirbúning fyrir viðtal. Viðskiptavinurinn er %s sem er að sækja um starf sem %s hjá %s. " +
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
                            "   - Hvattu viðskiptavininn til að æfa svör upphátt\n" +
                            "   - Bíddu eftir spurningum\n\n" +
                            "Gakktu úr skugga um:\n" +
                            "- Að halda samtalinu náttúrulegu og gagnvirku\n" +
                            "- Að setja áherslu á það sem er mikilvægt fyrir þetta starf\n" +
                            "- Að tala á ÍSLENSKU\n",
                            "- Að segja ekki of margt í einu",
                    name,
                    jobTitle,
                    company
            );
        }
        System.out.println("Interview mode");
        return String.format(
                "Þú ert að taka atvinnuviðtal, þú heitir Jón. Sá/sú sem þú tekur viðtal við heitir %s og er að sækja um starf sem %s hjá %s. " +
                        "1. Byrjaðu á náttúrulegu viðtali með mismunandi spurningum." +
                        "(hegðunar, tæknilegum, osfrv.). Haltu flæði í samræðunum og í mesta lagi %d spurningar eru leyfðar.\n" +
                        "2. Eftir nákvæmlega %d spurningar eða þegar viðmælandinn segir 'hætta', gefðu endurgjöf á eftirfarandi máta:\n" +
                        "   - 3 styrkleikar útfrá svörunum hans.\n" +
                        "   - 3 hlutir sem hann mætti bæta útfrá svörunum hans.\n" +
                        "   - Vitnaðu stuttlega í svörin hans í endurgjöfinni. " +
                        "   - Ekki fara í alltof mikil smáatriði í endurgjöfinni, reyndu að halda þessu 'short and sweet'.\n" +
                        "Mundu svo að leyfa viðtakandanum að svara, ekki spyrja margar spurningar í einu.\n" +
                        "Ekki gleyma að viðtalið á að vera náttúrulegt, þannig ekki taka sérstaklega fram að notandi eigi að svara eða " +
                        "annað sem kann að vera mjög augljóst.\n" +
                        "TALAÐU ÍSLENSKU!",
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

    public String resendLast() throws Exception {
        return chatSession.resendLastMessage();
    }

    public boolean getIsActive() {
        return this.isActive;
    }
}
