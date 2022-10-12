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
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
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
    public static int MAX_LEVEL = 3;

    public static final int HEIGHT = 600;
    public static final int WIDTH = 600;
    public static final int MAX_WIDTH = 600;

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
        vars.put("score", 0);
        vars.put("time", 200.0);
        vars.put("level", 0);
        vars.put("life", 3);
    }

    @Override
    protected void initUI() {

        Text levelText = getUIFactoryService().newText("", Color.WHITE, 25);
        levelText.setTranslateX(10);
        levelText.setTranslateY(30);
        levelText.textProperty().bind(getip("level").asString("Level: %d"));
        addUINode(levelText);

        Text lifeText = getUIFactoryService().newText("", Color.WHITE, 25);
        lifeText.setTranslateX(150);
        lifeText.setTranslateY(30);
        lifeText.textProperty().bind(getip("life").asString("♥ [%d]"));
        addUINode(lifeText);

        Text timerText = getUIFactoryService().newText("", Color.WHITE, 25);
        timerText.setTranslateX(260);
        timerText.setTranslateY(30);
        timerText.textProperty().bind(getdp("time").asString("Time: %.0f"));
        addUINode(timerText);

        Text scoreText = getUIFactoryService().newText("", Color.WHITE, 25);
        scoreText.setTranslateX(430);
        scoreText.setTranslateY(30);
        scoreText.textProperty().bind(getip("score").asString("Score: %d"));
        addUINode(scoreText);

    }

    @Override
    protected void onUpdate(double tpf) {
        if (is_died) return;
        inc("time", -tpf);
        if (getd("time") <= 0.0) {
            showMessage("Time Up !!!");
            hitTaken(player);
        }
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
                if (!is_died) {
                    play("place_bomb.wav");
                    playerComponent.placeBomb();
                }
            }
        }, KeyCode.SPACE);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new BombermanFactory());
        nextLevel();
    }

    public void init() {
        is_died = false;
        play("next_level.wav");
        Level level = getAssetLoader().loadLevel(geti("level") + ".txt", new TextLevelLoader(40, 40, '0'));
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
        spawn("pass", new SpawnData(480, 80));
        spawn("oneal", new SpawnData(480, 480));
        spawn("oneal", new SpawnData(240, 280));
        spawn("dahl", new SpawnData(480, 300));

//        Viewport viewport = getGameScene().getViewport();
//        viewport.setBounds(0, 0, MAX_WIDTH, HEIGHT);
//        viewport.bindToEntity(player, getAppWidth() / 2, getAppHeight() / 2);
//        viewport.setLazy(true);

        set("time", 200.0);
        set("score", 0);
        count_brick = 0;

    }

    private void nextLevel() {
        if (geti("level") == MAX_LEVEL) {
            showMessage("CONGRATULATIONS !!! \n\n\n\n GOODBYE");
            return;
        }
        count_brick = 0;
        inc("level", +1);
        init();
    }

    @Override
    protected void initPhysics() {

        onCollision(PLAYER, ENEMY, (player, enemy) -> {
            if (Math.abs(player.getPosition().getX() - enemy.getPosition().getX()) < 20 &&
                    Math.abs(player.getPosition().getY() - enemy.getPosition().getY()) < 20) {
                System.out.println("player die ne");
                hitTaken(player);
            }
        });

        onCollision(PLAYER, PASS, (player, pass) -> {
            if (Math.abs(player.getPosition().getX() - pass.getPosition().getX()) < 20 &&
                    Math.abs(player.getPosition().getY() - pass.getPosition().getY()) < 20) {
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

        onCollision(PLAYER, DAHL, (player, dahl) -> {
            if (Math.abs(player.getPosition().getX() - dahl.getPosition().getX()) < 20 &&
                    Math.abs(player.getPosition().getY() - dahl.getPosition().getY()) < 20) {
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
                play("powerup.wav");
                bombItem.removeFromWorld();
                increaseBombsMaximum();
            }
        });

        onCollision(PLAYER, FLAMEITEM, (player, flameItem) -> {
            if (Math.abs(flameItem.getPosition().getX() - player.getPosition().getX()) < 20 &&
                    Math.abs(flameItem.getPosition().getY() - player.getPosition().getY()) < 20) {
                play("powerup.wav");
                flameItem.removeFromWorld();
                increaseExplosionRadius();
            }
        });

        onCollision(PLAYER, SPEEDITEM, (player, speedItem) -> {
            if (Math.abs(speedItem.getPosition().getX() - player.getPosition().getX()) < 20 &&
                    Math.abs(speedItem.getPosition().getY() - player.getPosition().getY()) < 20) {
                play("powerup.wav");
                speedItem.removeFromWorld();
                playerComponent.increasePlayerSpeed();
            }
        });

        onCollision(PLAYER, DOOR, (player, door) -> {
            if (Math.abs(door.getPosition().getX() - player.getPosition().getX()) < 20 &&
                    Math.abs(door.getPosition().getY() - player.getPosition().getY()) < 20 &&
                    !is_died && getGameWorld().getGroup(ENEMY, ONEAL).getSize() == 0) {
                play("stage_start.wav");
                door.removeFromWorld();
                //spawn("portal", door.getX(), door.getY());
                getGameTimer().runOnceAfter(() -> {
                    getGameWorld().getEntitiesCopy().forEach(Entity::removeFromWorld);
                    nextLevel();
                }, Duration.seconds(1));
            }
        });

        onCollision(ENEMY, FLAME, (enemy, flame) -> {
            if (Math.abs(flame.getPosition().getX() - enemy.getPosition().getX()) < 20 &&
                    Math.abs(flame.getPosition().getY() - enemy.getPosition().getY()) < 20) {
                System.out.println("enemy die ne");
                enemy.removeFromWorld();
                inc("score", +100);

                Entity enemyDied = spawn("enemyDied", enemy.getX(), enemy.getY());
                getGameTimer().runOnceAfter(() -> {
                    enemyDied.removeFromWorld();
                }, Duration.seconds(0.5));
            }
        });

        onCollision(PASS, FLAME, (enemy, flame) -> {
            if (Math.abs(flame.getPosition().getX() - enemy.getPosition().getX()) < 20 &&
                    Math.abs(flame.getPosition().getY() - enemy.getPosition().getY()) < 20) {
                System.out.println("enemy die ne");
                enemy.removeFromWorld();
                inc("score", +200);

                Entity enemyDied = spawn("enemyDied", enemy.getX(), enemy.getY());
                getGameTimer().runOnceAfter(() -> {
                    enemyDied.removeFromWorld();
                    spawn("enemy", new SpawnData(40, 80));
                    spawn("enemy", new SpawnData(80, 80));
                    System.out.println("2 enemy");
                }, Duration.seconds(0.5));


            }
        });

        onCollision(ONEAL, FLAME, (oneal, flame) -> {
            if (Math.abs(flame.getPosition().getX() - oneal.getPosition().getX()) < 20 &&
                    Math.abs(flame.getPosition().getY() - oneal.getPosition().getY()) < 20) {
                oneal.removeFromWorld();
                inc("score", +250);

                Entity onealDied = spawn("onealDied", oneal.getX(), oneal.getY());
                getGameTimer().runOnceAfter(() -> {
                    onealDied.removeFromWorld();
                }, Duration.seconds(0.5));
            }
        });

        onCollision(DAHL, FLAME, (dahl, flame) -> {
            if (Math.abs(flame.getPosition().getX() - dahl.getPosition().getX()) < 20 &&
                    Math.abs(flame.getPosition().getY() - dahl.getPosition().getY()) < 20) {
                dahl.removeFromWorld();
                inc("score", +150);

                Entity dahlDied = spawn("dahlDied", dahl.getX(), dahl.getY());
                getGameTimer().runOnceAfter(() -> {
                    dahlDied.removeFromWorld();
                }, Duration.seconds(0.5));
            }
        });
    }

    public void hitTaken(Entity player) {
        is_died = true;
        play("player_die.wav");
        player.removeFromWorld();
        Entity playerDied = spawn("playerDied", player.getX(), player.getY());
        getGameTimer().runOnceAfter(() -> {
            playerDied.removeFromWorld();
        }, Duration.seconds(0.5));

        getGameTimer().runOnceAfter(() -> {
            FXGL.getGameScene().getViewport().fade(() -> {
                getGameWorld().getEntitiesCopy().forEach(Entity::removeFromWorld);
                //FXGL.getGameController().startNewGame();
                inc("life", -1);
                if (geti("life") > 0) {
                    //setup();
                    init();
                } else {
                    showMessage("YOU DIED !!!", () -> {
                        FXGL.getGameController().gotoGameMenu();
                    });
                }
            });
        }, Duration.seconds(1));

    }

    public static void incScore(int score) {
        inc("score", +score);
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
