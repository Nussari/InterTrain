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
     * Skiptir yfir í spurningaviðmótið
     */
    public void onAefa(ActionEvent ignored) {
        ViewSwitcher.switchTo(View.SPURNINGAR, true);
    }

    public void onUndirbua(ActionEvent ignored) {
        ViewSwitcher.switchTo(View.SPURNINGAR, true);
    }

    /**
     * Skiptir yfir í kveðju viðmótið
     */
    public void onKvedja(ActionEvent ignored) {
        ViewSwitcher.switchTo(View.KVEDJA, true);
    }


}