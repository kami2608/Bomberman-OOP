package uet.oop.bombermanoop;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import javafx.geometry.Point2D;
import javafx.scene.control.Cell;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import uet.oop.bombermanoop.components.*;

import static com.almasb.fxgl.dsl.FXGL.*;
import static uet.oop.bombermanoop.BombermanApp.*;
import static uet.oop.bombermanoop.BombermanType.*;

public class BombermanFactory implements EntityFactory{

    private static final int PLAYER_SPEED = 150;
    private static final int ENEMY_SPEED = 50;

    private static final int PASS_SPEED = 150;

    @Spawns("BG")
    public Entity newBackground(SpawnData data) {
        return entityBuilder()
                .at(0, 0)
                .viewWithBBox(new Rectangle(MAX_WIDTH, HEIGHT, Color.FORESTGREEN))
                .zIndex(-1)
                .build();
    }

    @Spawns("w")
    public Entity newWall(SpawnData data) {
        return entityBuilder(data)
                .type(WALL)
                .viewWithBBox(texture("wall.png", TILE_SIZE, TILE_SIZE))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("b")
    public Entity newBrick(SpawnData data) {
        return entityBuilder(data)
                .type(BRICK)
                .viewWithBBox(texture("brick.png", TILE_SIZE, TILE_SIZE))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("brick")
    public Entity newBrickExplode(SpawnData data) {
        return entityBuilder(data)
                .view(texture("brickExplode.png").toAnimatedTexture(6, Duration.seconds(1)).play())
                .build();
    }

    @Spawns("portal")
    public Entity newPortal(SpawnData data) {
        return entityBuilder(data)
                .type(DOOR)
                .viewWithBBox(texture("door.png", TILE_SIZE, TILE_SIZE))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("speedItem")
    public Entity newSpeedItem(SpawnData data) {
        return entityBuilder(data)
                .type(SPEEDITEM)
                .viewWithBBox(texture("speedItem.png", TILE_SIZE, TILE_SIZE))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("flameItem")
    public Entity newFlameItem(SpawnData data) {
        return entityBuilder(data)
                .type(FLAMEITEM)
                .viewWithBBox(texture("flameItem.png", TILE_SIZE, TILE_SIZE))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("bombItem")
    public Entity newBombItem(SpawnData data) {
        return entityBuilder(data)
                .type(BOMBITEM)
                .viewWithBBox(texture("bombItem.png", TILE_SIZE, TILE_SIZE))
                .with(new CollidableComponent(true))
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
                .at(new Point2D(40, 80))
                .bbox(new HitBox(new Point2D(2, 2), BoundingShape.circle(18)))
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
                .view(texture("bomb.png").toAnimatedTexture(12, Duration.seconds(2)).play())
                .bbox(new HitBox(new Point2D(2, 2), BoundingShape.circle(18)))
                .with(new BombComponent(data.get("radius")))
                .atAnchored(new Point2D(20, 20), new Point2D(data.getX() + TILE_SIZE / 2, data.getY() + TILE_SIZE / 2))
                .build();
    }

    @Spawns("enemy")
    public Entity newEnemy(SpawnData data) {
        return entityBuilder(data)
                .type(ENEMY)
                .atAnchored(new Point2D(20, 20), new Point2D(20, 20))
                .at(new Point2D(data.getX(), data.getY()))
                //.at(new Point2D(40, 40))
                .bbox(new HitBox(new Point2D(5, 5), BoundingShape.circle(18)))
                .with(new CollidableComponent(true))
                .with(new CellMoveComponent(TILE_SIZE, TILE_SIZE, ENEMY_SPEED))
                .with(new AStarMoveComponent(FXGL.<BombermanApp>getAppCast().getGrid()))
                .with(new EnemyComponent())
                .build();
    }

    @Spawns("oneal")
    public Entity newOneal(SpawnData data) {
        return entityBuilder(data)
                .type(ONEAL)
                .atAnchored(new Point2D(20, 20), new Point2D(20, 20))
                .at(new Point2D(data.getX(), data.getY()))
                .bbox(new HitBox(new Point2D(5, 5), BoundingShape.circle(18)))
                .with(new CollidableComponent(true))
                .with(new CellMoveComponent(TILE_SIZE, TILE_SIZE, ENEMY_SPEED))
                .with(new AStarMoveComponent(FXGL.<BombermanApp>getAppCast().getGrid()))
                .with(new OnealComponent())
                .build();
    }

    @Spawns("dahl")
    public Entity newDahl(SpawnData data) {
        return entityBuilder(data)
                .type(DAHL)
                .atAnchored(new Point2D(20, 20), new Point2D(20, 20))
                .at(new Point2D(data.getX(), data.getY()))
                .bbox(new HitBox(new Point2D(5, 5), BoundingShape.circle(18)))
                .with(new CollidableComponent(true))
                .with(new CellMoveComponent(TILE_SIZE, TILE_SIZE, ENEMY_SPEED))
                .with(new AStarMoveComponent(FXGL.<BombermanApp>getAppCast().getGrid()))
                .with(new DahlComponent())
                .build();
    }

    @Spawns("pass")
    public Entity newPass(SpawnData data) {
        return entityBuilder(data)
                .type(PASS)
                .atAnchored(new Point2D(20, 20), new Point2D(20, 20))
                .at(new Point2D(data.getX(), data.getY()))
                //.at(new Point2D(40, 40))
                .bbox(new HitBox(new Point2D(5, 5), BoundingShape.circle(18)))
                .with(new CollidableComponent(true))
                .with(new CellMoveComponent(TILE_SIZE, TILE_SIZE, PASS_SPEED))
                .with(new AStarMoveComponent(FXGL.<BombermanApp>getAppCast().getGrid()))
                .with(new PassComponent())
                .build();
    }

    @Spawns("flame")
    public Entity newFlame(SpawnData data) {

        return entityBuilder(data)
                .type(FLAME)
                .view(texture("flame.png").toAnimatedTexture(16, Duration.seconds(0.3)).play())
                .with(new CollidableComponent(true))
                .bbox(new HitBox(new Point2D(5, 5), BoundingShape.circle(20)))
                .build();
    }

    @Spawns("playerDied")
    public Entity newPlayerDied(SpawnData data) {
        return entityBuilder(data)
                .view(texture("playerDied.png").toAnimatedTexture(7, Duration.seconds(1)).play())
                .build();

    }

    @Spawns("enemyDied")
    public Entity newEnemyDied(SpawnData data) {
        return entityBuilder(data)
                .view(texture("enemyDied.png").toAnimatedTexture(5, Duration.seconds(1)).play())
                .build();
    }

    @Spawns("onealDied")
    public Entity newOnealDied(SpawnData data) {
        return entityBuilder(data)
                .view(texture("onealDied.png").toAnimatedTexture(4, Duration.seconds(1)).play())
                .build();
    }

    @Spawns("dahlDied")
    public Entity newDahlDied(SpawnData data) {
        return entityBuilder(data)
                .view(texture("dahlDied.png").toAnimatedTexture(5, Duration.seconds(1)).play())
                .build();
    }

}