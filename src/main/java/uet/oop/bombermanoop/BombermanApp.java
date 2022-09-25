package uet.oop.bombermanoop;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.SimpleGameMenu;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.entity.level.text.TextLevelLoader;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import javafx.scene.input.KeyCode;
import uet.oop.bombermanoop.components.PlayerComponent;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;
import static uet.oop.bombermanoop.BombermanType.*;

public class BombermanApp extends GameApplication{

    public static final int TILE_SIZE = 40;

    public static final int HEIGHT = 600;
    public static final int WIDTH = 600;

    private Entity player;
    private AStarGrid grid;
    private PlayerComponent playerComponent;

    public AStarGrid getGrid() {
        return grid;
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Bomberman");
        settings.setHeight(HEIGHT);
        settings.setWidth(WIDTH);
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
                playerComponent.right();
            }
        }, KeyCode.D);

        getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                playerComponent.left();
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                playerComponent.up();
            }
        }, KeyCode.W);

        getInput().addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                playerComponent.down();
            }
        }, KeyCode.S);

        getInput().addAction(new UserAction("Place Bomb") {
            @Override
            protected void onActionBegin() {
                playerComponent.placeBomb();
            }
        }, KeyCode.SPACE);
    }

    @Override
    protected void initGame() {

        getGameWorld().addEntityFactory(new BombermanFactory());

        Level level = getAssetLoader().loadLevel("0.txt", new TextLevelLoader(40, 40, '0'));
        getGameWorld().setLevel(level);

        spawn("BG");

        grid = AStarGrid.fromWorld(FXGL.getGameWorld(), 600, 600, 40, 40, type -> {
            if(type.equals(WALL) || type.equals(BRICK))
                return CellState.NOT_WALKABLE;

            return CellState.WALKABLE;
        });

        player = FXGL.spawn("player");
        playerComponent = player.getComponent(PlayerComponent.class);

    }

    @Override
    protected void initPhysics() {}

    public void onEntityDestroyed(Entity e) {
        int cellX = (int)((e.getX() + 20) / TILE_SIZE);
        int cellY = (int)((e.getY() + 20) / TILE_SIZE);
        grid.get(cellX, cellY).setState(CellState.WALKABLE);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
