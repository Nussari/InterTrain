package vv.intertrain.vidmot;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import vv.intertrain.vinnsla.ChatMode;
import vv.intertrain.vinnsla.InterviewBot;

import static vv.intertrain.vidmot.InterviewApplication.interview;

public class VidtalController {
    @FXML private ChatboxController chatBoxController;
    @FXML private TextField inntak;

    private InterviewBot interview;

    @FXML
    public void initialize() throws Exception {
        String nafn = ChatboxController.getNafn();
        String starf = ChatboxController.getStarf();
        String fyrirtaeki = ChatboxController.getFyrirtaeki();

        interview = new InterviewBot(nafn, fyrirtaeki, starf, 10, ChatMode.INTERVIEW);

        Task<String> initTask = new Task<>() {
            @Override
            protected String call() throws Exception {
                return interview.start();
            }
        };

        initTask.setOnSucceeded(e -> {
            chatBoxController.fyrstuSkilabod(initTask.getValue());

        });

        initTask.setOnFailed(e -> {
            chatBoxController.fyrstuSkilabod("Villa hjá gervigreind.");
        });

        new Thread(initTask).start();
    }

    public void onSenda() throws Exception {

        // Sækir skilaboð notanda og setur í textabúbblu
        String notandaSkilabod = inntak.getText();
        chatBoxController.nySkilabod(notandaSkilabod, true);

        // Hreinsar inntak
        inntak.clear();

        // API hlutir gerðir í bakgrunn til að forrit frjósi ekki
        Task<String> responseTask = new Task<>() {
            @Override
            protected String call() throws Exception {
                // Sæki svar frá API
                return interview.respond(notandaSkilabod);
            }
        };

        responseTask.setOnSucceeded(e -> {
            // Set svarið í búbblu
            chatBoxController.nySkilabod(responseTask.getValue(), false);
        });

        responseTask.setOnFailed(e -> {
            // Ef gervigreind klikkar
            chatBoxController.nySkilabod("Villa hjá gervigreind, reyndu aftur", false);
        });

        // Þræðir bjarga lífum
        new Thread(responseTask).start();
    }

    public void onEndurstilla() throws Exception {
        chatBoxController.clear();
        initialize();
    }

    public void onTilBaka() {
        chatBoxController.clear();
        ViewSwitcher.switchTo (View.VELKOMINN, true);
    }

    public void onKvedja() {
        ViewSwitcher.switchTo(View.KVEDJA, true);
    }
}