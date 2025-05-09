package vv.intertrain.vidmot;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/******************************************************************************
 *  Nafn    : Viktor Andri Hermannsson
 *  T-póstur: vah32@hi.is
 *  Lýsing  : Controller fyrir chatbox. Tekur inn strengi og merkingu um hvor
 *            á strenginn og sýnir þá í scrollview.
 *****************************************************************************/

public class ChatboxController {

    @FXML private VBox skilabod;
    @FXML private ScrollPane scrollPane;

    private static String nafn;
    private static String starf;
    private static String fyrirtaeki;

    private HBox hledsluSkilabod;
    private HBox sidastaSkilabod;

    @FXML
    public void initialize() {
        skilabod.heightProperty().addListener((obs, oldVal, newVal) -> Platform.runLater(() -> {
            scrollPane.setVvalue(1.0);
            scrollPane.layout();
        }));
        // Bæti við texta sem segir að verið sé að hlaða gervigreind
        hledsluSkilabod = nyHledsla();
        skilabod.getChildren().add(hledsluSkilabod);
    }

    public void fyrstuSkilabod(String texti){
        // Tek burt textann sem segir að sé verið að hlaða
        if (hledsluSkilabod != null && skilabod.getChildren().contains(hledsluSkilabod)) {
            skilabod.getChildren().remove(hledsluSkilabod);
        }
        nySkilabod(texti, false);
    }

    public void nySkilabod(String texti, boolean notandi) {
        HBox textaBubbla = nyBubbla(texti, notandi);
        if(!notandi){
            sidastaSkilabod = textaBubbla; // held utan um nýjasta botta skilaboð tilsa sýna/fela retry takka
        } else {
            sidastaSkilabod.getChildren().get(1).setVisible(false); // fel gamla retry takka
        }
        skilabod.getChildren().add(textaBubbla); // bæti búbblu við chatbox
    }

    private HBox nyBubbla(String texti, boolean notandi) {
        Label textaLabel = new Label(texti);
        textaLabel.setWrapText(true);
        textaLabel.setFont(Font.font(14));
        textaLabel.setPadding(new Insets(10));
        textaLabel.setMaxWidth(400);

        HBox container = new HBox(textaLabel);
        container.setAlignment(notandi ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        HBox.setHgrow(textaLabel, Priority.ALWAYS);

        String klasi = notandi ? "notanda-skilabod" : "tolvu-skilabod";
        textaLabel.getStyleClass().add(klasi);

        if (!notandi) { // set "Reyna aftur" takka við hliðina á búbblu
            Button retryTakki = new Button("Reyna aftur");
            retryTakki.getStyleClass().add("retry-takki");
            retryTakki.setOnAction(e -> {
                skilabod.getChildren().remove(container); // þegar smellt er á takka eyði hann skilaboðinu
                // BREYTA ÞESSU ÞEGAR Í KALL Á GERVIGREIND ÞEGAR HÚN ER KOMIN
                nySkilabod("endurreynt", false);
            });
            container.getChildren().add(retryTakki);
        }

        return container;
    }

    private HBox nyHledsla() {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(10));

        Label loadingLabel = new Label("Hleð gervigreind...");
        loadingLabel.setStyle("-fx-text-fill: #666; -fx-font-style: italic;");

        container.getChildren().add(loadingLabel);
        return container;
    }

    public static String getNafn() {
        return nafn;
    }

    public static void setNafn(String nyttNafn) {
        nafn = nyttNafn;
    }

    public static String getFyrirtaeki() {
        return fyrirtaeki;
    }

    public static void setFyrirtaeki(String nyttFyrirtaeki) {
        fyrirtaeki = nyttFyrirtaeki;
    }

    public static String getStarf() {
        return starf;
    }

    public static void setStarf(String nyttStarf) {
        starf = nyttStarf;
    }

    public void clear() {
        skilabod.getChildren().clear();
        initialize();
    }
}