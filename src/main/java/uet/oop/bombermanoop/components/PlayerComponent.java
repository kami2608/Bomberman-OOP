package uet.oop.bombermanoop.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.control.Cell;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.concurrent.Phaser;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class PlayerComponent extends Component {
    public AStarMoveComponent astar;
    public CellMoveComponent cell;
    private AnimatedTexture texture;
    private AnimationChannel animIdle, animWalkX, animWalkUp, animWalkDown;

    public final static int FRAME_SIZE = 38;

    public PlayerComponent() {
        Image imageX = image("playerX.png");
        Image imageUp = image("playerUp.png");
        Image imageDown = image("playerDown.png");

        animIdle = new AnimationChannel(imageDown, 3, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.3), 0, 0);
        animWalkX = new AnimationChannel(imageX, 3, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.3), 0, 2);
        animWalkDown = new AnimationChannel(imageDown, 3, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.3), 0, 2);
        animWalkUp = new AnimationChannel(imageUp, 3, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.3), 0, 2);

        texture = new AnimatedTexture(animIdle);

        texture.loop();
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(FRAME_SIZE / 2, FRAME_SIZE / 2));
        entity.getViewComponent().addChild(texture);

    }

    @Override
    public void onUpdate(double tpf) {
        if(!astar.isMoving()) {
            if(texture.getAnimationChannel() != animIdle)
                texture.loopAnimationChannel(animIdle);
        }
    }

    public void left() {
        getEntity().setScaleX(1);
        astar.moveToLeftCell();
        if(texture.getAnimationChannel() != animWalkX)
            texture.loopAnimationChannel(animWalkX);
    }

    public void right() {
        getEntity().setScaleX(-1);
        astar.moveToRightCell();
        if(texture.getAnimationChannel() != animWalkX)
            texture.loopAnimationChannel(animWalkX);
    }

    public void down() {
        astar.moveToDownCell();
        if(texture.getAnimationChannel() != animWalkDown)
            texture.loopAnimationChannel(animWalkDown);
    }

    public void up() {
        astar.moveToUpCell();
        if(texture.getAnimationChannel() != animWalkUp)
            texture.loopAnimationChannel(animWalkUp);
    }

    public void placeBomb() {}

}
