module uet.oop.bombermanoop {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;

    opens uet.oop.bombermanoop to javafx.fxml;
    opens assets.levels;
    opens assets.textures;
    exports uet.oop.bombermanoop;
}