package vv.intertrain.vidmot;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import vv.intertrain.vinnsla.Spurningar;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import static vv.intertrain.vidmot.InterviewApplication.spurningar;

public class UndirbuaController {
    @FXML
    private Label fxFjoldiSvara;
    @FXML
    private ListView<String> fxFlokkar;
    @FXML
    private ListView<String> fxSpurningar;
    @FXML
    private TextArea fxSpurningaLog;
    @FXML
    private Label fxLoading;

    private String valinnFlokkur;
    private String valinSpurning;
    private final ObservableList<String> spurningaLog = FXCollections.observableArrayList();
    private final StringProperty spurningaLogProperty = new SimpleStringProperty("");

    @FXML
    protected void onSvara() {
        // sækja valið item úr spurningalistanum, bara virkja takkann ef eitthvað er valið
        updateValinSpurning();
        SvarDialogController svarDialogController = new SvarDialogController(valinSpurning);
        Optional<String> utkoma = svarDialogController.showAndWait();

        String svar = svarDialogController.getSvar();

        if (svar != null && !svar.isEmpty()) { // ef notandi svaraði
            // hækkar fjölda svara um 1
            int fjoldi = Integer.parseInt(spurningar.getFjoldiSvaradraSpurninga());
            spurningar.setFjoldiSvaradraSpurninga(String.valueOf(++fjoldi));
            // bætir spurningu í log
            spurningaLog.add(valinSpurning);
            updateSpurningaLog();
        }
    }

    @FXML
    protected void onTilBaka() {
        // fara til baka í Velkominn
        ViewSwitcher.switchTo(View.VELKOMINN, true);
    }

    @FXML
    protected void onHaetta() {
        // skipta yfir í kveðju
        ViewSwitcher.switchTo(View.KVEDJA, true);
    }

    protected String getValinnFlokkur() {
        return this.valinnFlokkur;
    }

    protected void updateValinnFlokkur() {
        this.valinnFlokkur = fxFlokkar.getSelectionModel().getSelectedItem();
    }

    protected String getValinSpurning() {
        return this.valinSpurning;
    }

    protected void updateValinSpurning() {
        this.valinSpurning = fxSpurningar.getSelectionModel().getSelectedItem();
    }

    private void updateSpurningaLog() {
        String logText = String.join("\n", spurningaLog);
        spurningaLogProperty.set(logText);
    }

    public void initialize() throws InterruptedException {

        String starf = ChatboxController.getStarf();
        String fyrirtaeki = ChatboxController.getFyrirtaeki();

        // Býr til nýtt task
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                List<String> hopar = List.of("Tæknilegt", "Samskipti", "Hópavinna", "Lausnamiðun");
                spurningar = new Spurningar(starf, fyrirtaeki, hopar);

                // Sækir flokka
                ObservableList<String> flokkar = spurningar.getFlokkar();

                // Uppfærir viðmót
                Platform.runLater(() -> {
                    fxLoading.setText("");
                    fxFlokkar.setItems(flokkar);
                });

                Platform.runLater(() -> {
                    fxFjoldiSvara.setText(spurningar.getFjoldiSvaradraSpurningaProperty().get());
                });

                return null;
            }
        };

        task.setOnSucceeded(event -> {
            // Bindur hluti eftir að taskið klárar
            fxFjoldiSvara.textProperty().bind(spurningar.getFjoldiSvaradraSpurningaProperty());
            fxSpurningaLog.textProperty().bind(spurningaLogProperty);

            // Bindur flokka við viðmótshlut
            fxFlokkar.setItems(spurningar.getFlokkar());

            if (!spurningar.getFlokkar().isEmpty()) {
                updateValinnFlokkur();
            }

            fxFlokkar.getSelectionModel().selectedIndexProperty().addListener((obs, old, newIndex) -> {
                if (newIndex != null && newIndex.intValue() > -1) {
                    updateValinnFlokkur();

                    Map<String, ObservableList<String>> spurningaListi = spurningar.getSpurningaListi();
                    fxSpurningar.setItems(spurningaListi.get(this.getValinnFlokkur()));
                    fxSpurningar.getSelectionModel().selectFirst();
                    updateValinSpurning();
                }
            });
        });

        // Listener fyrir villur
        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            exception.printStackTrace();
        });

        // Keyrir taskið í nýjum þræði
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}