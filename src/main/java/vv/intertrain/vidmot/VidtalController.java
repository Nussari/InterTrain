package vv.intertrain.vidmot;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import vv.intertrain.vinnsla.ChatMode;
import vv.intertrain.vinnsla.InterviewBot;

import static vv.intertrain.vidmot.InterviewApplication.interview;

public class VidtalController {
    @FXML private ChatboxController chatBoxController;
    @FXML private TextField inntak;

    // TEMP FIX COMMENT
    // private InterviewBot interview;

    @FXML
    public void initialize() throws Exception {
        String nafn = ChatboxController.getNafn();
        String starf = ChatboxController.getStarf();
        String fyrirtaeki = ChatboxController.getFyrirtaeki();

        interview = new InterviewBot(nafn, fyrirtaeki, starf, 10, ChatMode.INTERVIEW);

        // Hefur viðtalið
        chatBoxController.nySkilabod(interview.start(), false);
        // TEMP FIX LÍNA
        //chatBoxController.nySkilabod("temp start", false);
    }

    public void onSenda() throws Exception {

        // Sækir skilaboð notanda og setur í textabúbblu
        String notandaSkilabod = inntak.getText();
        chatBoxController.nySkilabod(notandaSkilabod, true);

        // Hreinsar inntak
        inntak.clear();

        // Sendir skilaboðin á Gemini og setur svarið í búbblu
        String svar = interview.respond(notandaSkilabod);
        chatBoxController.nySkilabod(svar, false);
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