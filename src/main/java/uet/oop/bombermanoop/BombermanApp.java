package uet.oop.bombermanoop;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.SimpleGameMenu;
import com.almasb.fxgl.app.scene.Viewport;
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
import uet.oop.bombermanoop.menu.BombermanMainMenu;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;

import static com.almasb.fxgl.dsl.FXGL.*;
import static uet.oop.bombermanoop.BombermanType.*;
import static uet.oop.bombermanoop.components.PlayerComponent.*;

public class BombermanApp extends GameApplication {

    public static final int TILE_SIZE = 40;
    public static boolean is_died = false;
    public static int count_brick = 0;

    public static final int HEIGHT = 600;
    public static final int WIDTH = 1200;
    public static final int MAX_WIDTH = 46 * 40;

    private static Entity player;
    private static AStarGrid grid;
    private static PlayerComponent playerComponent;

    public static AStarGrid getGrid() {
        return grid;
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Bomberman");
        settings.setHeight(HEIGHT);
        settings.setWidth(WIDTH);
        settings.setVersion("0.1");
        settings.setMainMenuEnabled(true);
        settings.setSceneFactory(new SceneFactory() {
            @Override
            public FXGLMenu newMainMenu() {
                return new BombermanMainMenu();
            }
        });

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
                if (!is_died)
                    playerComponent.right();
            }
        }, KeyCode.D);

        getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                if (!is_died)
                    playerComponent.left();
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                if (!is_died)
                    playerComponent.up();
            }
        }, KeyCode.W);

        getInput().addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                if (!is_died)
                    playerComponent.down();
            }
        }, KeyCode.S);

        getInput().addAction(new UserAction("Place Bomb") {
            @Override
            protected void onActionBegin() {
                if (!is_died)
                    playerComponent.placeBomb();
            }
        }, KeyCode.SPACE);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new BombermanFactory());
        init();
    }

    public void init() {
        is_died = false;
        Level level = getAssetLoader().loadLevel("0.txt", new TextLevelLoader(40, 40, '0'));
        getGameWorld().setLevel(level);
        spawn("BG");

        grid = AStarGrid.fromWorld(FXGL.getGameWorld(), WIDTH, HEIGHT, TILE_SIZE, TILE_SIZE, type -> {
            if (type.equals(WALL) || type.equals(BRICK) || type.equals(BOMB))
                return CellState.NOT_WALKABLE;

            return CellState.WALKABLE;
        });

        player = FXGL.spawn("player");
        playerComponent = player.getComponent(PlayerComponent.class);

        spawn("enemy", new SpawnData(160, 80));
        spawn("enemy", new SpawnData(480, 80));
        spawn("oneal", new SpawnData(480, 480));
        spawn("oneal", new SpawnData(280, 280));

        setup();

        Viewport viewport = getGameScene().getViewport();
        viewport.setBounds(0, 0, MAX_WIDTH, HEIGHT);
        viewport.bindToEntity(player, getAppWidth() / 2, getAppHeight() / 2);
        viewport.setLazy(true);

    }

    @Override
    protected void initPhysics() {

//        onCollision(FLAME, BRICK, (flame, brick) -> {
//            if (Math.abs(brick.getPosition().getX() - flame.getPosition().getX()) < 20 &&
//                    Math.abs(brick.getPosition().getY() - flame.getPosition().getY()) < 20) {
//                brick.removeFromWorld();
//                Entity brickExplode = spawn("brick", brick.getX(), brick.getY());
//                getGameTimer().runOnceAfter(() -> {
//                    brickExplode.removeFromWorld();
//                }, Duration.seconds(0.5));
//            }
//        });

        onCollision(PLAYER, ENEMY, (player, enemy) -> {
            if (Math.abs(player.getPosition().getX() - enemy.getPosition().getX()) < 20 &&
                    Math.abs(player.getPosition().getY() - enemy.getPosition().getY()) < 20) {
                System.out.println("player die ne");
                hitTaken(player);
            }
        });

        onCollision(PLAYER, ONEAL, (player, oneal) -> {
            if (Math.abs(player.getPosition().getX() - oneal.getPosition().getX()) < 20 &&
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

        onCollision(PLAYER, BOMBITEM, (player, bombItem) -> {
            if (Math.abs(bombItem.getPosition().getX() - player.getPosition().getX()) < 20 &&
                    Math.abs(bombItem.getPosition().getY() - player.getPosition().getY()) < 20) {
                System.out.println("hi");
                bombItem.removeFromWorld();
                increaseBombsMaximum();
            }
        });

        onCollision(PLAYER, FLAMEITEM, (player, flameItem) -> {
            if (Math.abs(flameItem.getPosition().getX() - player.getPosition().getX()) < 20 &&
                    Math.abs(flameItem.getPosition().getY() - player.getPosition().getY()) < 20) {
                flameItem.removeFromWorld();
                increaseExplosionRadius();
            }
        });

        onCollision(PLAYER, SPEEDITEM, (player, speedItem) -> {
            if (Math.abs(speedItem.getPosition().getX() - player.getPosition().getX()) < 20 &&
                    Math.abs(speedItem.getPosition().getY() - player.getPosition().getY()) < 20) {
                speedItem.removeFromWorld();
                playerComponent.increasePlayerSpeed();
            }
        });

        onCollision(PLAYER, DOOR, (player, door) -> {
            if (Math.abs(door.getPosition().getX() - player.getPosition().getX()) < 20 &&
                    Math.abs(door.getPosition().getY() - player.getPosition().getY()) < 20 &&
                    getGameWorld().getGroup(ENEMY, ONEAL).getSize() == 0) {
                getGameTimer().runOnceAfter(() -> {
                    FXGL.getGameScene().getViewport().fade(() -> {
                        is_died = true;
                        door.removeFromWorld();
                        player.removeFromWorld();
                        getGameWorld().getEntitiesCopy().forEach(Entity::removeFromWorld);
                        //FXGL.getGameController().startNewGame();
                        init();
                    });
                }, Duration.seconds(2));
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

    public void hitTaken(Entity player) {
        is_died = true;
        player.removeFromWorld();
        Entity playerDied = spawn("playerDied", player.getX(), player.getY());
        getGameTimer().runOnceAfter(() -> {
            playerDied.removeFromWorld();
        }, Duration.seconds(0.5));

        getGameTimer().runOnceAfter(() -> {
            FXGL.getGameScene().getViewport().fade(() -> {
                getGameWorld().getEntitiesCopy().forEach(Entity::removeFromWorld);
                //FXGL.getGameController().startNewGame();
                init();
            });
        }, Duration.seconds(2));

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
