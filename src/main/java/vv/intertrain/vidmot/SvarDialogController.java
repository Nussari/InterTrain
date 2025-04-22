package vv.intertrain.vidmot;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import java.io.IOException;
import static vv.intertrain.vidmot.InterviewApplication.spurningar;

/******************************************************************************
 *  Nafn    : Viktor Óli Bjarkason
 *  T-póstur: vob7@hi.is
 *
 *  Lýsing  : Controller fyrir dialog fyrir svar og feedback við spurningu
 *
 *
 *****************************************************************************/
public class SvarDialogController extends Dialog<String> {

    // Viðmótshlutir
    @FXML
    private TextArea fxSpurning;
    @FXML
    private TextField fxSvar;
    @FXML
    private ButtonType fxILagi;
    @FXML
    private ButtonType fxHaettaVid;
    @FXML
    private TextArea fxFeedback;

    private String spurning;
    private String svar;

    public SvarDialogController(String selectedItem) {
        this.spurning = selectedItem;
        this.svar = "";
        setDialogPane(lesaSvarDialog());
        // sett regla um hvenær í lagi hnappur er virkur
        iLagiRegla();
        fxSpurning.setText(this.spurning);

        // bætir action við í lagi takka
        Button haettaVidTakki = (Button) this.getDialogPane().lookupButton(fxHaettaVid);
        haettaVidTakki.setOnAction(e -> onHaettaVid());
    }

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

    public String getSvar() {
        return this.svar;
    }

    /**
     * Regla búin til um hvenær Í lagi hnappurinn á að vera óvirkur/virkur
     */
    private void iLagiRegla() {
        // fletta upp í lagi hnappnum út frá hnappategund
        Node iLagi = getDialogPane().lookupButton(fxILagi);
        // setja reglu um hvenær í lagi hnappur er virkur
        iLagi.disableProperty()
                .bind(fxFeedback.textProperty().isEmpty());
    }

    @FXML
    protected void onSvar()
    throws IllegalArgumentException {
        try {
            this.spurning = (String) fxSpurning.getText();
            this.svar = (String) fxSvar.getText();

            fxFeedback.setText("Sæki endurgjöf...");

            Task<String> faEndurgjofTask = new Task<>() {
                @Override
                protected String call() throws InterruptedException {
                    // Sækir endurgjöf
                    return spurningar.faEndurgjof(spurning, svar);
                }
            };

            // Þegar endurgjöfin er komin
            faEndurgjofTask.setOnSucceeded(workerStateEvent -> {
                // Birtir endurgjöfina í viðmóti
                fxFeedback.setText(faEndurgjofTask.getValue());
            });

            // Villumeðhöndlun fyrir taskið
            faEndurgjofTask.setOnFailed(workerStateEvent -> {
                fxFeedback.setText("Villa: Endurgjöf mistókst.");
                faEndurgjofTask.getException().printStackTrace();
            });

            // Keyrir taskið í bakgrunnsþræði
            Thread backgroundThread = new Thread(faEndurgjofTask);
            backgroundThread.setDaemon(true);
            backgroundThread.start();

        } catch (IllegalArgumentException e) {
            System.out.println("Ólöglegt inntak! " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Óvænt villa: " + e.getMessage());
        }
    }

    protected void onHaettaVid() {
        this.svar = "";
    }
}
