package vv.intertrain.vidmot;

import javafx.application.Platform;
import javafx.event.ActionEvent;

/******************************************************************************
 *  Nafn    : Ebba Þóra Hvannberg
 *  T-póstur: ebba@hi.is
 *  Lýsing  : Controller fyrir Kveðja viðmótið. Lokar forritinu.
 *****************************************************************************/
public class KvedjaController {
    /**
     * Handler fyrir hætta
     * @param ignored ónotað
     */
    public void onQuit(ActionEvent ignored) {
        Platform.exit();
        System.exit(0);
    }
}

