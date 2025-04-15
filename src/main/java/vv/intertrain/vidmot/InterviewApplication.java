package vv.intertrain.vidmot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import vv.intertrain.vinnsla.InterviewBot;

import java.io.IOException;

/******************************************************************************
 *  Nafn    : Ebba Þóra Hvannberg
 *  T-póstur: ebba@hi.is
 *  Lýsing  : Verkefni 4 - 2025
 *****************************************************************************/

public class InterviewApplication extends Application {
    public static InterviewBot interview;
    /**
     * Ræsir notendaviðmótið í glugga stage
     * @param stage glugginn
     */
    @Override
    public void start(Stage stage) {
        Scene s = new Scene (new Pane(), 600, 600);
        ViewSwitcher.setScene(s);
        ViewSwitcher.switchTo(View.VELKOMINN, true);
        stage.setTitle("InterTrain");
        stage.setScene(s);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}