package vv.intertrain.vidmot;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import vv.intertrain.vinnsla.InterviewBot;

public class VidtalController {
    @FXML private ChatboxController chatBoxController;
    @FXML private TextField inntak;

    private InterviewBot interview;

    private static String nafn;
    private static String starf;
    private static String fyrirtaeki;

    @FXML
    public void initialize() throws Exception {
        interview = new InterviewBot(nafn, fyrirtaeki, starf, 10);

        // Hefur viðtalið
        chatBoxController.nySkilabod(interview.start(), false);
    }

    public static void setNafn(String nyttNafn) {
        nafn = nyttNafn;
    }

    public static void setFyrirtaeki(String nyttFyrirtaeki) {
        fyrirtaeki = nyttFyrirtaeki;
    }

    public static void setStarf(String nyttStarf) {
        starf = nyttStarf;
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

    public void onEndurstilla() {
        chatBoxController.clear();
        // Breyta þessu þegar AI klasi er tilbúinn
        chatBoxController.nySkilabod("Velkominn", false);
    }

    public void onTilBaka() {
        chatBoxController.clear();
        ViewSwitcher.switchTo (View.VELKOMINN, true);
    }

    public void onKvedja() {
        ViewSwitcher.switchTo(View.KVEDJA, true);
    }
}