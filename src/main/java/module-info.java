module vv.intertrain {
    requires javafx.controls;
    requires javafx.fxml;
    requires okhttp3;


    opens vv.intertrain.vidmot to javafx.fxml;
    exports vv.intertrain.vidmot;
}