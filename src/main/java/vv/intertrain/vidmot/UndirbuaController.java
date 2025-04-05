package vv.intertrain.vidmot;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class UndirbuaController {
    @FXML private ChatboxController chatBoxController;
    @FXML private TextField inntak;

    @FXML
    public void initialize() {
        // Breyta þessu þegar AI klasi er tilbúinn
        chatBoxController.nySkilabod("Undirbúningur fyrir viðtal:", false);
    }

    public void onSenda(){
        chatBoxController.nySkilabod(inntak.getText(), true);
        inntak.clear();
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
