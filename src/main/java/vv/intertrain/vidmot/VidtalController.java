package vv.intertrain.vidmot;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class VidtalController {
    @FXML private ChatboxController chatBoxController;
    @FXML private TextField inntak;

    @FXML
    public void initialize() {
        // Breyta þessu þegar AI klasi er tilbúinn
        chatBoxController.nyttSkilabod("Velkominn í viðtalið", false);
    }

    public void onSenda(){
        chatBoxController.nyttSkilabod(inntak.getText(), true);
        inntak.clear();
    }

    public void onEndurstilla() {
        chatBoxController.clear();
        // Breyta þessu þegar AI klasi er tilbúinn
        chatBoxController.nyttSkilabod("Velkominn", false);
    }

    public void onTilBaka() {
        chatBoxController.clear();
        ViewSwitcher.switchTo (View.VELKOMINN, true);
    }

    public void onKvedja() {
        ViewSwitcher.switchTo(View.KVEDJA, true);
    }
}