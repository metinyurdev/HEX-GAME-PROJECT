module org.example.hexgame {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.example.hexgame.controller to javafx.fxml;
    opens org.example.hexgame.model to javafx.base;

    exports org.example.hexgame;
}
