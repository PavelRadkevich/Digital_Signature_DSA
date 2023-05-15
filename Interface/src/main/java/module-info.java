module kryptografiaInt {
    requires javafx.controls;
    requires javafx.fxml;
    requires kryptografia;

    opens kryptografiaInt to javafx.fxml;
    exports kryptografiaInt;
}