package vv.intertrain.vidmot;

import vv.intertrain.vinnsla.Spurningar;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.util.Optional;

/******************************************************************************
 *  Nafn    : Ebba Þóra Hvannberg
 *  T-póstur: ebba@hi.is
 *  Lýsing  : Controller fyrir spurningaviðmótið. Getur farið til baka í velkominn
 *  viðmótið eða hætt og farið í kveðja viðmótið. Notandi velur spurningaflokk og uppfærist
 *  þá spurningarnar. Notandi getur valið að svara spurningu og birtist þá dialog-ur
 *  fjöldi spurninga er uppfært eftir að notandi hefur svarað spurningu
 *****************************************************************************/
public class SpurningarController {

    // viðmótshlutir
    @FXML
    private Label fxFjoldiSvaradraSpurninga; // fjöldi svaraðra spurninga
    @FXML
    private ListView<String> fxFlokkar; // listi af flokkum
    @FXML
    private ListView<String> fxSpurningar; // listi af spurningum
    @FXML
    private TextArea fxHistory; // loggurinn

    // vinnslan
    private final Spurningar spurningar = new Spurningar();

    /**
     * setur upp spurningaflokka og spurningar. gerir handler (listener) fyrir það
     * þegar notandi velur flokk
     */
    public void initialize() {
        fxFjoldiSvaradraSpurninga.textProperty().
                bindBidirectional (spurningar.propertyfjoldiSvaradraSpurninga());
        fxFlokkar.setItems (spurningar.getFlokkar());
        fxFlokkar.getSelectionModel().selectedIndexProperty()
                .addListener((obs, old, newIndex) -> {
                    if (newIndex.intValue() >= 0) {
                        fxSpurningar.setItems(spurningar
                                .getSpurningalisti(spurningar.getFlokkar()
                                        .get(newIndex.intValue())));
                    }
                });

    }

    /**
     * Fer í velkominn viðmótstréð
     * @param actionEvent
     */
    public void onTilBaka(ActionEvent actionEvent) {
        ViewSwitcher.switchTo (View.VELKOMINN, true);
    }

    /**
     * Fer í kveðja viðmótstréð
     * @param actionEvent
     */
    public void onKvedja(ActionEvent actionEvent) {
        ViewSwitcher.switchTo(View.KVEDJA, true);
    }

    /**
     * Svara handler. Opnar dialog, fær streng til baka sem spurningu.
     * Bætir henni við á logg-inn
     * og fjölgar spurningum um einn
     * @param actionEvent
     */
    public void onSvara(ActionEvent actionEvent) {
        SvarDialogController svarglugginn = new SvarDialogController(fxSpurningar
                .getSelectionModel().getSelectedItem());
        Optional<String> results = svarglugginn.showAndWait();
        results.ifPresent(s -> {
            fxHistory.appendText( s + "\r\n");
            spurningar.plusEinnSpurningar();});
    }
}