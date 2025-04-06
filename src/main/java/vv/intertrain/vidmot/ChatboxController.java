package vv.intertrain.vidmot;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

    @FXML
    public void initialize() {
        skilabod.heightProperty().addListener((obs, oldVal, newVal) -> javafx.application.Platform.runLater(() -> {
            scrollPane.setVvalue(1.0);
            scrollPane.layout();
        }));
    }

    public void nySkilabod(String texti, boolean notandi) {
        HBox textaBubbla = nyBubbla(texti, notandi);
        skilabod.getChildren().add(textaBubbla);
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
    }
}