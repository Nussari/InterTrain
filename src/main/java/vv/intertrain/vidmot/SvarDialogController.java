package vv.intertrain.vidmot;

import vv.intertrain.vinnsla.FeedbackService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

/******************************************************************************
 *  Nafn    : Ebba Þóra Hvannberg
 *  T-póstur: ebba@hi.is
 *  Lýsing  : Dialog controller fyrir svar dialoginn
 *****************************************************************************/
public class SvarDialogController extends Dialog<String> {

    @FXML
    private Label fxSpurning; // spurning

    @FXML
    private TextField fxSvar; // svarið fært inn

    @FXML
    private Label fxFeedback; // feedback frá bakenda


    /**
     * Smiður sem setur spurningu í spurningareitinn
     * Setur upp resultConverter sem skilar spurningunni
     * @param selectedItem spurningin
     */
    public SvarDialogController(String selectedItem) {
        setDialogPane(lesaSvarDialog());
        fxSpurning.setText(selectedItem);
        setResultConverter(b -> {
            if (b.getButtonData().isDefaultButton()) {
                return fxSpurning.getText();
            }
            else {
                return null;
            }
        });
    }

    /**
     * handler fyrir það þegar svarið er sett inn. feedback fengið frá bakenda
     * @param event
     */
    @FXML
    public void faFeedback(ActionEvent event) {
        fxFeedback.setText(FeedbackService.provideFeedback(fxSvar.getText()));
        event.consume(); // til að atburðurinn fari ekki næst á OK hnappinn
    }

    /**
     * Les inn .fxml skrá fyrir DialogPane dialogsins
     * @return DialogPane
     */
    private DialogPane lesaSvarDialog() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("svar-view.fxml"));
        try {
            // controller er settur sem þessi hlutur
            fxmlLoader.setController(this);
            return fxmlLoader.load();
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}