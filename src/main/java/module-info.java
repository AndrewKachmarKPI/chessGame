module com.chess.chessgame {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.chess.chessgame to javafx.fxml;
    exports com.chess.chessgame;
}
