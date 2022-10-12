package uet.oop.bombermanoop.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.pathfinding.CellState;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import uet.oop.bombermanoop.BombermanApp;
import uet.oop.bombermanoop.BombermanType;

import java.util.Date;
import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.*;
import static uet.oop.bombermanoop.BombermanApp.*;
import static uet.oop.bombermanoop.BombermanType.*;

public class BombComponent extends Component {
    private int radius;
    private boolean hasWall;
    private Random random = new Random();

    public BombComponent(int radius) {
        //random.setSeed(1234567890);
        this.radius = radius;
    }

    @Override
    public void onAdded() {
//        FXGL.<BombermanApp>getAppCast().getGrid()
//                .get((int) this.getEntity().getX(), (int) this.getEntity().getY())
//                .setState(CellState.NOT_WALKABLE);
        FXGL.<BombermanApp>getAppCast().NotWalkable(this.getEntity());

    }

    public void explode(double x, double y, Entity bomb) {
        play("explosion.wav");
        FXGL.getGameTimer().runOnceAfter(bomb::removeFromWorld, Duration.seconds(0.2));

        removeEntityXRight(x, y);
        removeEntityYDown(x, y);
        removeEntityYUp(x, y);
        removeEntityXLeft(x, y);
    }

    private void removeEntity(double x, double y) {
        getGameWorld().getEntitiesAt(new Point2D(x, y))
                .stream()
                .forEach(e -> {
//                    if (e.isType(WALL)) {
//                        this.hasWall = true;
//                        return;
//                    }
//                    if (e.isType(BRICK)) {
//                        FXGL.<BombermanApp>getAppCast().onEntityDestroyed(e);
//                        e.removeFromWorld();
//                        Entity brickExplode = spawn("brick", e.getX(), e.getY());
//                        getGameTimer().runOnceAfter(() -> {
//                            brickExplode.removeFromWorld();
//                        }, Duration.seconds(0.5));
//
//                        return;
//                    }

                    if (!e.isType(WALL) && !e.isType(BOMBITEM) && !e.isType(FLAMEITEM) && !e.isType(SPEEDITEM) && !e.isType(DOOR)) {
                        FXGL.<BombermanApp>getAppCast().onEntityDestroyed(e);
                        if(!e.isType(BOMB))
                            e.removeFromWorld();
                        if(e.isType(PLAYER)) {
                            FXGL.<BombermanApp>getAppCast().hitTaken(e);
                        }
                        if(e.isType(ENEMY)) {
                            incScore(100);
                            Entity enemyDied = spawn("enemyDied", e.getX(), e.getY());
                            getGameTimer().runOnceAfter(() -> {
                                enemyDied.removeFromWorld();
                            }, Duration.seconds(0.5));
                        }
                        else if(e.isType(DAHL)) {
                            incScore(150);
                            Entity dahlDied = spawn("dahlDied", e.getX(), e.getY());
                            getGameTimer().runOnceAfter(() -> {
                                dahlDied.removeFromWorld();
                            }, Duration.seconds(0.5));
                        }
                        else if(e.isType(PASS)) {
                            incScore(200);
                            Entity enemyDied = spawn("enemyDied", e.getX(), e.getY());
                            getGameTimer().runOnceAfter(() -> {
                                enemyDied.removeFromWorld();
                                spawn("enemy", new SpawnData(40, 80));
                                spawn("enemy", new SpawnData(80, 80));
                                System.out.println("2 enemy");
                            }, Duration.seconds(0.5));
                        }
                        else if(e.isType(ONEAL)) {
                            incScore(250);
                            Entity onealDied = spawn("onealDied", e.getX(), e.getY());
                            getGameTimer().runOnceAfter(() -> {
                                onealDied.removeFromWorld();
                            }, Duration.seconds(0.5));
                        }
                        if (e.isType(BRICK)) {
                            count_brick++;
                            System.out.println(count_brick);
                            if (count_brick == 5) {
                                spawn("bombItem", e.getX(), e.getY());
                            } else if (count_brick == 10) {
                                spawn("flameItem", e.getX(), e.getY());
                            } else if (count_brick == 15) {
                                spawn("portal", e.getX(), e.getY());
                            } else if (count_brick == 20) {
                                spawn("speedItem", e.getX(), e.getY());
                            } else {
                                Entity brickExplode = spawn("brick", e.getX(), e.getY());
                                getGameTimer().runOnceAfter(() -> {
                                    brickExplode.removeFromWorld();
                                }, Duration.seconds(0.5));
                            }
                        }

                        return;
                    } else {
                        this.hasWall = true;
                        return;
                    }
////                    if(e.isType(WALL)) {
////                        this.hasWall = true;
////                        return;
////                    }

                });
        if (!this.hasWall) {
            Entity flame = spawn("flame", x, y);
            getGameTimer().runOnceAfter(() -> {
                flame.removeFromWorld();
            }, Duration.seconds(0.5));
        }
    }

    private void removeEntityYDown(double x, double y) {
        this.hasWall = false;
        for (double i = y; i <= y + this.radius; i = i + 40) {
            if (i > (MAX_WIDTH - 80) || hasWall) {
                break;
            }

            removeEntity(x, i);
        }
    }

    private void removeEntityYUp(double x, double y) {
        this.hasWall = false;
        for (double i = y; i >= y - this.radius; i = i - 40) {
            if (i < 40 || hasWall) {
                break;
            }

            removeEntity(x, i);
        }
    }

    private void removeEntityXLeft(double x, double y) {
        this.hasWall = false;
        for (double i = x; i <= x + this.radius; i = i + 40) {
            if (i > (MAX_WIDTH - 80) || hasWall) {
                break;
            }

            removeEntity(i, y);
        }
    }

    private void removeEntityXRight(double x, double y) {
        this.hasWall = false;
        for (double i = x; i >= x - this.radius; i = i - 40) {
            if (i < 40 || hasWall) {
                break;
            }

            removeEntity(i, y);
        }
    }

}
