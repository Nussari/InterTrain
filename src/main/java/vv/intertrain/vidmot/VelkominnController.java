package vv.intertrain.vidmot;

import javafx.event.ActionEvent;
/******************************************************************************
 *  Nafn    : Ebba Þóra Hvannberg
 *  T-póstur: ebba@hi.is
 *  Lýsing  : Controller fyrir Velkominn viðmótið. Getur farið í spurningaviðmótið
 *  eða hætt
 *****************************************************************************/

public class VelkominnController {

    /**
     * Skiptir yfir í spurningaviðmótið
     * @param ignored
     */
    public void onSpurningar(ActionEvent ignored) {
        ViewSwitcher.switchTo(View.SPURNINGAR, true);
    }

    /**
     * Skiptir yfir í kveðju viðmótið
     * @param ignored
     */
    public void onKvedja(ActionEvent ignored) {
        ViewSwitcher.switchTo(View.KVEDJA, true);
    }
}