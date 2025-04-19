module vv.intertrain {
    requires javafx.controls;
    requires javafx.fxml;
    requires okhttp3;
    requires com.fasterxml.jackson.databind;
    requires com.google.gson;
    requires spring.web;
    requires spring.core;


    opens vv.intertrain.vidmot to javafx.fxml;
    exports vv.intertrain.vidmot;
}