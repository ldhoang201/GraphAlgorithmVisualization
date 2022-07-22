module com.example.oop {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.oop to javafx.fxml;
    exports com.example.oop;
    exports com.example.oop.controller;
    opens com.example.oop.controller to javafx.fxml;
    exports com.example.oop.algorithm;
    opens com.example.oop.algorithm to javafx.fxml;

}