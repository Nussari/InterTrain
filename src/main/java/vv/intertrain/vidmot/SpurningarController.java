package vv.intertrain.vidmot;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import vinnsla.Spurningar;

import java.util.Map;
import java.util.Optional;

public class SpurningarController {
    @FXML
    private Label fxFjoldiSvara;
    @FXML
    private ListView<String> fxFlokkar;
    @FXML
    private ListView<String> fxSpurningar;
    @FXML
    private TextArea fxSpurningaLog;

    private String valinnFlokkur;
    private String valinSpurning;
    // private Spurningar spurningar;
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
        // frt til baka í Velkominn
        ViewSwitcher.switchTo(View.VELKOMINN);
    }

    @FXML
    protected void onHaetta() {
        // skipta yfir í kveðju
        ViewSwitcher.switchTo(View.KVEDJA);
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

    public void initialize() {
        this.spurningar = new Spurningar();

        // bindur fjölda svara við viðmótshlut
        fxFjoldiSvara.textProperty().bind(spurningar.getFjoldiSvaradraSpurningaProperty());
        // bindur spurningalog við viðmótshlut
        fxSpurningaLog.textProperty().bind(spurningaLogProperty);

        fxFlokkar.setItems(spurningar.getFlokkar());

        if (!spurningar.getFlokkar().isEmpty()) {
            updateValinnFlokkur();
        }

        fxFlokkar.getSelectionModel().selectedIndexProperty().addListener((obs, old, newIndex) -> {
            if (newIndex != null && newIndex.intValue() > -1) {
                    updateValinnFlokkur();

                    Map<String, ObservableList<String>> spurningaListi = spurningar.getSpurningaListi(this.getValinnFlokkur());
                    fxSpurningar.setItems(spurningaListi.get(this.getValinnFlokkur()));
                    fxSpurningar.getSelectionModel().selectFirst();
                    updateValinSpurning();
                }
        });
    }
}
