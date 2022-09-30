package uet.oop.bombermanoop;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.SimpleGameMenu;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.components.TypeComponent;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.entity.level.text.TextLevelLoader;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarCell;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import uet.oop.bombermanoop.components.EnemyComponent;
import uet.oop.bombermanoop.components.PlayerComponent;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import static com.almasb.fxgl.dsl.FXGL.*;
import static uet.oop.bombermanoop.BombermanType.*;

public class BombermanApp extends GameApplication {

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
    protected void initGameVars(Map<String, Object> vars) {
    }

    @Override
    protected void initUI() {
    }

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
        //spawn("bomb", new SpawnData(120, 40));

        grid = AStarGrid.fromWorld(FXGL.getGameWorld(), WIDTH, HEIGHT, TILE_SIZE, TILE_SIZE, type -> {
            if (type.equals(WALL) || type.equals(BRICK) || type.equals(BOMB))
                return CellState.NOT_WALKABLE;

            return CellState.WALKABLE;
        });


        player = FXGL.spawn("player");
        playerComponent = player.getComponent(PlayerComponent.class);

        spawn("enemy", new SpawnData(160, 40));
        spawn("oneal", new SpawnData(520, 520));
        spawn("enemy", new SpawnData(480, 80));
        spawn("oneal", new SpawnData(280, 280));

    }

    @Override
    protected void initPhysics() {

        onCollision(FLAME, BRICK, (flame, brick) -> {
            if(Math.abs(brick.getPosition().getX() - flame.getPosition().getX()) < 20 &&
                    Math.abs(brick.getPosition().getY() - flame.getPosition().getY()) < 20) {
                brick.removeFromWorld();
                System.out.println("brick explode");
                Entity brickExpode = spawn("brick", brick.getX(), brick.getY());
                getGameTimer().runOnceAfter(() -> {
                    brickExpode.removeFromWorld();
                }, Duration.seconds(0.5));
            }
        });

        onCollision(PLAYER, ENEMY, (player, enemy) -> {
            if(Math.abs(player.getPosition().getX() - enemy.getPosition().getX()) < 20 &&
            Math.abs(player.getPosition().getY() - enemy.getPosition().getY()) < 20) {
                System.out.println("player die ne");
                hitTaken(player);
            }
        });

        onCollision(PLAYER, ONEAL, (player, oneal) -> {
            if(Math.abs(player.getPosition().getX() - oneal.getPosition().getX()) < 20 &&
                    Math.abs(player.getPosition().getY() - oneal.getPosition().getY()) < 20) {
                System.out.println("player die ne");
                hitTaken(player);
            }
        });

        onCollision(PLAYER, FLAME, (player, flame) -> {
            if (Math.abs(flame.getPosition().getX() - player.getPosition().getX()) < 20 &&
                    Math.abs(flame.getPosition().getY() - player.getPosition().getY()) < 20) {
                System.out.println("player die ne 2");
                hitTaken(player);
            }
        });

        onCollision(ENEMY, FLAME, (enemy, flame) -> {
            if (Math.abs(flame.getPosition().getX() - enemy.getPosition().getX()) < 20 &&
                    Math.abs(flame.getPosition().getY() - enemy.getPosition().getY()) < 20) {
                System.out.println("enemy die ne");
                enemy.removeFromWorld();

                Entity enemyDied = spawn("enemyDied", enemy.getX(), enemy.getY());
                getGameTimer().runOnceAfter(() -> {
                        enemyDied.removeFromWorld();
                }, Duration.seconds(0.5));
            }
        });

        onCollision(ONEAL, FLAME, (oneal, flame) -> {
            if (Math.abs(flame.getPosition().getX() - oneal.getPosition().getX()) < 20 &&
                    Math.abs(flame.getPosition().getY() - oneal.getPosition().getY()) < 20) {
                oneal.removeFromWorld();

                Entity onealDied = spawn("onealDied", oneal.getX(), oneal.getY());
                getGameTimer().runOnceAfter(() -> {
                    onealDied.removeFromWorld();
                }, Duration.seconds(0.5));
            }
        });
    }

    private void hitTaken(Entity player) {
        //playerComponent.playerDied();
        player.removeFromWorld();
        Entity playerDied = spawn("playerDied", player.getX(), player.getY());
        getGameTimer().runOnceAfter(() -> {
            playerDied.removeFromWorld();
        }, Duration.seconds(0.5));

//        this.player = spawn("player");
//        this.playerComponent = this.player.getComponent(PlayerComponent.class);
    }

    public void onEntityDestroyed(Entity e) {
        int cellX = (int) ((e.getX() + 20) / TILE_SIZE);
        int cellY = (int) ((e.getY() + 20) / TILE_SIZE);
        grid.get(cellX, cellY).setState(CellState.WALKABLE);
    }

    public void NotWalkable(Entity e) {
        int cellX = (int) ((e.getX() + 20) / TILE_SIZE);
        int cellY = (int) ((e.getY() + 20) / TILE_SIZE);
        grid.get(cellX, cellY).setState(CellState.NOT_WALKABLE);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
