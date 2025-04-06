package vv.intertrain.vidmot;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import vv.intertrain.vinnsla.ChatMode;
import vv.intertrain.vinnsla.InterviewBot;

public class UndirbuaController {
    @FXML private ChatboxController chatBoxController;
    @FXML private TextField inntak;

    private InterviewBot prep;

    @FXML
    public void initialize() throws Exception {
        String nafn = ChatboxController.getNafn();
        String starf = ChatboxController.getStarf();
        String fyrirtaeki = ChatboxController.getFyrirtaeki();

        prep = new InterviewBot(nafn, fyrirtaeki, starf, 10, ChatMode.PREPARATION);

        // Hefur viðtalið
        chatBoxController.nySkilabod(prep.start(), false);
    }

    public void onSenda() throws Exception {
        // Sækir skilaboð notanda og setur í textabúbblu
        String notandaSkilabod = inntak.getText();
        chatBoxController.nySkilabod(notandaSkilabod, true);

        // Hreinsar inntak
        inntak.clear();

        // Sendir skilaboðin á Gemini og setur svarið í búbblu
        String svar = prep.respond(notandaSkilabod);
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
