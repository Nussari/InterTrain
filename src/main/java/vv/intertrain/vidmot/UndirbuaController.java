package vv.intertrain.vidmot;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import vv.intertrain.vinnsla.ChatMode;
import vv.intertrain.vinnsla.InterviewBot;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import vv.intertrain.vinnsla.InterviewPreparer;
// import vv.intertrain.vinnsla.InterviewPreparer;
// import vinnsla.Spurningar;

import static vv.intertrain.vidmot.InterviewApplication.interview;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UndirbuaController {
    @FXML
    private Label fxFjoldiSvara;
    @FXML
    private ListView<String> fxFlokkar;
    @FXML
    private ListView<String> fxSpurningar;
    @FXML
    private TextArea fxSpurningaLog;

    //private InterviewPreparer interviewPreparer;

    private String valinnFlokkur;
    private String valinSpurning;
    // private Spurningar spurningar;
    private final ObservableList<String> spurningaLog = FXCollections.observableArrayList();
    private final StringProperty spurningaLogProperty = new SimpleStringProperty("");
    private InterviewPreparer interviewPreparer;

    @FXML
    protected void onSvara() {
        // sækja valið item úr spurningalistanum, bara virkja takkann ef eitthvað er valið
        updateValinSpurning();
        SvarDialogController svarDialogController = new SvarDialogController(valinSpurning);
        Optional<String> utkoma = svarDialogController.showAndWait();

        String svar = svarDialogController.getSvar();

        if (svar != null && !svar.isEmpty()) { // ef notandi svaraði
            // hækkar fjölda svara um 1
            //int fjoldi = Integer.parseInt(spurningar.getFjoldiSvaradraSpurninga());
            //spurningar.setFjoldiSvaradraSpurninga(String.valueOf(++fjoldi));
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

    public void initialize() {

        String nafn = ChatboxController.getNafn();
        String starf = ChatboxController.getStarf();
        String fyrirtaeki = ChatboxController.getFyrirtaeki();

        interview = new InterviewBot(nafn, fyrirtaeki, starf, 10, ChatMode.PREPARATION);


        this.interviewPreparer = new InterviewPreparer(starf, fyrirtaeki, 3);

        List<String> groups = List.of("Tæknilegar", "Samskipta");

        this.questions = interviewpreparer.requestQuestions(starf, fyrirtaeki, groups);

        try {
            //String test = interviewPreparer.requestQuestions();
            //System.out.println(test);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        // bindur fjölda svara við viðmótshlut
        // fxFjoldiSvara.textProperty().bind(spurningar.getFjoldiSvaradraSpurningaProperty());
        // bindur spurningalog við viðmótshlut
        fxSpurningaLog.textProperty().bind(spurningaLogProperty);

        //fxFlokkar.setItems(spurningar.getFlokkar());

        // if (!spurningar.getFlokkar().isEmpty()) {
            updateValinnFlokkur();
        }

        /*
        fxFlokkar.getSelectionModel().selectedIndexProperty().addListener((obs, old, newIndex) -> {
            if (newIndex != null && newIndex.intValue() > -1) {
                updateValinnFlokkur();

                Map<String, ObservableList<String>> spurningaListi = spurningar.getSpurningaListi(this.getValinnFlokkur());
                fxSpurningar.setItems(spurningaListi.get(this.getValinnFlokkur()));
                fxSpurningar.getSelectionModel().selectFirst();
                updateValinSpurning();
            }
        });
        */
}
















/*
public class UndirbuaController {
    @FXML private ChatboxController chatBoxController;
    @FXML private TextField inntak;

    @FXML
    public void initialize() throws Exception {
        String nafn = ChatboxController.getNafn();
        String starf = ChatboxController.getStarf();
        String fyrirtaeki = ChatboxController.getFyrirtaeki();

        // TEMP FIX COMMENT
        prep = new InterviewBot(nafn, fyrirtaeki, starf, 10, ChatMode.PREPARATION);

        // Hefur viðtalið
        // TEMP FIX COMMENT
        chatBoxController.nySkilabod(prep.start(), false);
        // TEMP FIX LÍNA
        // chatBoxController.nySkilabod("temp start", false);
    }

    public void onSenda() throws Exception {
        // Sækir skilaboð notanda og setur í textabúbblu
        String notandaSkilabod = inntak.getText();
        chatBoxController.nySkilabod(notandaSkilabod, true);

        // Hreinsar inntak
        inntak.clear();

        // Sendir skilaboðin á Gemini og setur svarið í búbblu
        // TEMP FIX COMMENT
        String svar = prep.respond(notandaSkilabod);
        // TEMP FIX COMMENT
        chatBoxController.nySkilabod(svar, false);
        // TEMP FIX LÍNA
        chatBoxController.nySkilabod("temp svar", false);
    }

    public void onEndurstilla() throws Exception {
        chatBoxController.clear();

        initialize();
    }

    public void onTilBaka() {
        chatBoxController.clear();
        ViewSwitcher.switchTo (View.VELKOMINN, true);
    }

    public void onKvedja() {
        ViewSwitcher.switchTo(View.KVEDJA, true);
    }
}
 */