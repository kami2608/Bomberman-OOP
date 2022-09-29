package uet.oop.bombermanoop.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.Required;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.control.Cell;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.image;
import static uet.oop.bombermanoop.components.PlayerComponent.FRAME_SIZE;

public class EnemyComponent extends Component {
    public AStarMoveComponent astar;
    public CellMoveComponent cell;
    private AnimatedTexture texture;
    private AnimationChannel animIdle, animWalk;

    public EnemyComponent() {
        Image image = image("enemy.png");

        animIdle = new AnimationChannel(image, 3, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.5), 0, 0);
        animWalk = new AnimationChannel(image, 3, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.5), 0, 2);

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
        double rand = Math.random();

        if (rand > 0.8) left();
        else if (rand > 0.6) right();
        else if (rand > 0.4) up();
        else down();
    }

    public void left() {
        getEntity().setScaleX(1);
        astar.moveToLeftCell();
        if(texture.getAnimationChannel() != animWalk)
            texture.loopAnimationChannel(animWalk);
    }

    public void right() {
        getEntity().setScaleX(-1);
        astar.moveToRightCell();
        if(texture.getAnimationChannel() != animWalk)
            texture.loopAnimationChannel(animWalk);
    }

    public void down() {
        astar.moveToDownCell();
        if(texture.getAnimationChannel() != animWalk)
            texture.loopAnimationChannel(animWalk);
    }

    public void up() {
        astar.moveToUpCell();
        if(texture.getAnimationChannel() != animWalk)
            texture.loopAnimationChannel(animWalk);
    }
}
