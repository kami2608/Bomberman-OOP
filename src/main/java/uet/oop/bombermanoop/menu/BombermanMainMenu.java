package uet.oop.bombermanoop.menu;


import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.view.KeyView;
import com.almasb.fxgl.input.view.MouseButtonView;
import com.almasb.fxgl.logging.Logger;
import com.almasb.fxgl.scene.Scene;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;

import static com.almasb.fxgl.dsl.FXGL.*;
import static javafx.scene.input.KeyCode.*;
import static uet.oop.bombermanoop.BombermanApp.*;

public class BombermanMainMenu extends FXGLMenu {

    private Node highScores;

    public BombermanMainMenu() {
        super(MenuType.MAIN_MENU);

        getContentRoot().getChildren().setAll(texture("wallpaper.png", WIDTH, HEIGHT));

//        var title = getUIFactoryService().newText(getSettings().getTitle(), Color.web("#f52307"), 150.0);
//        title.setStroke(Color.web("3bd962"));
//        title.setStrokeWidth(4.5);
//        title.setEffect(new Bloom(0.6));
//        centerTextBind(title, getAppWidth() / 2.0, 235);
//
//        getContentRoot().getChildren().addAll(title);

        var menuBox = new VBox(
                5,
                new MenuButton("New Game", () -> fireNewGame()),
                new MenuButton("Manual", () -> instructions()),
                new MenuButton("Exit", () -> fireExit())
        );
        menuBox.setAlignment(Pos.TOP_CENTER);

        menuBox.setTranslateX(getAppWidth() / 2.0 - 125);
        menuBox.setTranslateY(getAppHeight() / 2.0 + 50);

        var centeringLine = new Line(getAppWidth() / 2.0, 0, getAppWidth() / 2.0, getAppHeight());
        centeringLine.setStroke(Color.WHITE);

        getContentRoot().getChildren().addAll(menuBox);
    }



    private void instructions() {
        GridPane pane = new GridPane();

        pane.setEffect(new DropShadow(5, 3.5, 3.5, Color.DARKRED));
        pane.setHgap(25);
        pane.setVgap(10);
        pane.addRow(0, getUIFactoryService().newText("Movement"), new HBox(4, new KeyView(W), new KeyView(S), new KeyView(A), new KeyView(D)));
        pane.addColumn(2, new HBox(4, new KeyView(UP), new KeyView(DOWN), new KeyView(LEFT), new KeyView(RIGHT)));
        pane.addRow(2, getUIFactoryService().newText("Place bomb"), new KeyView(SPACE));

        getDialogService().showBox("Manual", pane, getUIFactoryService().newButton("OK"));
    }

    private static class MenuButton extends Parent {
        MenuButton(String name, Runnable action) {
            var text = getUIFactoryService().newText(name, Color.WHITESMOKE, 40.0);
            text.setStrokeWidth(1.5);
            text.strokeProperty().bind(text.fillProperty());

            text.fillProperty().bind(
                    Bindings.when(hoverProperty())
                            .then(Color.WHITESMOKE)
                            .otherwise(Color.web("#f05316"))
            );

            setOnMouseClicked(e -> action.run());

            setPickOnBounds(true);

            getChildren().add(text);
        }
    }
}
