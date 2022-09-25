package uet.oop.bombermanoop;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import javafx.geometry.Point2D;
import javafx.scene.control.Cell;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import uet.oop.bombermanoop.components.BombComponent;
import uet.oop.bombermanoop.components.PlayerComponent;

import static com.almasb.fxgl.dsl.FXGL.*;
import static uet.oop.bombermanoop.BombermanApp.TILE_SIZE;
import static uet.oop.bombermanoop.BombermanType.*;

public class BombermanFactory implements EntityFactory{

    private static final int PLAYER_SPEED = 180;

    @Spawns("BG")
    public Entity newBackground(SpawnData data) {
        return entityBuilder()
                .at(0, 0)
                .viewWithBBox(new Rectangle(600, 600, Color.FORESTGREEN))
                .zIndex(-1)
                .build();
    }

    @Spawns("w")
    public Entity newWall(SpawnData data) {
        return entityBuilder(data)
                .type(WALL)
                .viewWithBBox(texture("wall.png", TILE_SIZE, TILE_SIZE))
                .build();
    }

    @Spawns("b")
    public Entity newBrick(SpawnData data) {
        return entityBuilder(data)
                .type(BRICK)
                .viewWithBBox(texture("brick.png", TILE_SIZE, TILE_SIZE))
                .build();
    }

    @Spawns("1")
    public Entity newBlock(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();

        physics.setBodyType(BodyType.DYNAMIC);
        return entityBuilder(data)
                .type(WALL)
                .viewWithBBox(new Rectangle(TILE_SIZE, TILE_SIZE, Color.DARKORANGE))
                .build();
    }

    @Spawns("player")
    public Entity newPlayer(SpawnData data) {
        return entityBuilder(data)
                .type(PLAYER)
                .atAnchored(new Point2D(20, 20), new Point2D(20, 20))
                .at(new Point2D(40, 40))
                .with(new CollidableComponent(true))
                .with(new CellMoveComponent(TILE_SIZE, TILE_SIZE, PLAYER_SPEED))
                .with(new AStarMoveComponent(FXGL.<BombermanApp>getAppCast().getGrid()))
                .with(new PlayerComponent())
                .build();
    }

    @Spawns("bomb")
    public Entity newBomb(SpawnData data) {
        return entityBuilder(data)
                .type(BOMB)
                .viewWithBBox(texture("bomb.png", TILE_SIZE, TILE_SIZE))
                .with(new BombComponent(data.get("radius")))
                .atAnchored(new Point2D(20, 20), new Point2D(data.getX() + TILE_SIZE / 2, data.getY() + TILE_SIZE / 2))
                .build();
    }

}
