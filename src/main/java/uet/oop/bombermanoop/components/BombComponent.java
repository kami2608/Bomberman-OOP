package uet.oop.bombermanoop.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.pathfinding.CellState;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import uet.oop.bombermanoop.BombermanApp;

import static com.almasb.fxgl.dsl.FXGL.*;
import static uet.oop.bombermanoop.BombermanApp.*;
import static uet.oop.bombermanoop.BombermanType.*;

public class BombComponent extends Component {
    private int radius;
    private boolean hasWall;

    public BombComponent(int radius) {
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
                    if (!e.isType(WALL)) {
                        FXGL.<BombermanApp>getAppCast().onEntityDestroyed(e);
                        e.removeFromWorld();
                        if(e.isType(BRICK)) {
                            Entity brickExpode = spawn("brick", e.getX(), e.getY());
                            getGameTimer().runOnceAfter(() -> {
                                brickExpode.removeFromWorld();
                            }, Duration.seconds(0.5));
                        }

                        return;
                    }
                    else {
                        this.hasWall = true;
                        return;
                    }
//                    if(e.isType(WALL)) {
//                        this.hasWall = true;
//                        return;
//                    }

                });
        if(!this.hasWall) {
            Entity flame = spawn("flame", x, y);
            getGameTimer().runOnceAfter(() -> {
                flame.removeFromWorld();
            }, Duration.seconds(0.5));
        }
    }

    private void removeEntityYDown(double x, double y) {
        this.hasWall = false;
        for (double i = y; i <= y + this.radius; i = i + 40) {
            if (i > (WIDTH - 80) || hasWall) {
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
            if (i > (WIDTH - 80) || hasWall) {
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
