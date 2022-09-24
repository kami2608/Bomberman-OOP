package uet.oop.bombermanoop;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.SimpleGameMenu;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.entity.level.text.TextLevelLoader;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import javafx.scene.input.KeyCode;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

public class BombermanApp extends GameApplication{

    public static final int TILE_SIZE = 40;

    private Entity player;


    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Bomberman");
        settings.setHeight(600);
        settings.setWidth(600);
        settings.setVersion("0.1");
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {}

    @Override
    protected void initUI() {}

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
            }
        }, KeyCode.D);

        getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
            }
        }, KeyCode.W);

        getInput().addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
            }
        }, KeyCode.S);

        getInput().addAction(new UserAction("Place Bomb") {
            @Override
            protected void onAction() {
            }
        }, KeyCode.SPACE);
    }

    @Override
    protected void initGame() {
        Level level = getAssetLoader().loadLevel("0.txt", new TextLevelLoader(40, 40, '0'));
        getGameWorld().setLevel(level);
    }

    @Override
    protected void initPhysics() {}

    public static void main(String[] args) {
        launch(args);
    }
}
