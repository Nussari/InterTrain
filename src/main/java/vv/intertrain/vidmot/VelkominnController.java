package vv.intertrain.vidmot;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/******************************************************************************
 *  Nafn    : Viktor Andri Hermannsson
 *  T-póstur: vah32@hi.is
 *  Lýsing  : Controller fyrir Velkominn viðmótið. Setur inn upplýsingar um
 *  starf og getur farið í undirbúning, æfingarviðtal, eða hætt.
 *****************************************************************************/

public class VelkominnController {
    @FXML private TextField nafn;
    @FXML private TextField starf;
    @FXML private TextField fyrirtaeki;
    @FXML private Button undirbua;
    @FXML private Button aefa;

    @FXML
    public void initialize() {
        undirbua.disableProperty().bind(
                Bindings.createBooleanBinding(() ->
                                nafn.getText().trim().isEmpty() ||
                                        starf.getText().trim().isEmpty(),
                        nafn.textProperty(),
                        starf.textProperty()
                )
        );

        aefa.disableProperty().bind(
                Bindings.createBooleanBinding(() ->
                                nafn.getText().trim().isEmpty() ||
                                        starf.getText().trim().isEmpty(),
                        nafn.textProperty(),
                        starf.textProperty()
                )
        );
    }

    /**
     * Skiptir yfir í viðtalsviðmótið
     */
    public void onAefa(ActionEvent ignored) {
        ChatboxController.setNafn(nafn.getText());
        ChatboxController.setStarf(starf.getText());
        ChatboxController.setFyrirtaeki(fyrirtaeki.getText());

        ViewSwitcher.switchTo(View.VIDTAL, false);
    }

    /**
     * Skiptir yfir í undirbúningsviðmótið (æfingaspurningar)
     */
    public void onUndirbua(ActionEvent ignored) {
        ChatboxController.setNafn(nafn.getText());
        ChatboxController.setStarf(starf.getText());
        ChatboxController.setFyrirtaeki(fyrirtaeki.getText());

        ViewSwitcher.switchTo(View.UNDIRBUA, false);
    }

    /**
     * Skiptir yfir í kveðju viðmótið
     */
    public void onKvedja(ActionEvent ignored) {
        ViewSwitcher.switchTo(View.KVEDJA, true);
    }
}