package uet.oop.bombermanoop.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;
import uet.oop.bombermanoop.BombermanType;

import java.util.Random;

import static com.almasb.fxgl.dsl.FXGLForKtKt.image;
import static uet.oop.bombermanoop.BombermanApp.TILE_SIZE;
import static uet.oop.bombermanoop.BombermanApp.is_died;
import static uet.oop.bombermanoop.components.PlayerComponent.FRAME_SIZE;

public class MinvoComponent extends OnealComponent{
    private AnimatedTexture texture;
    private AnimationChannel animIdle, animWalk;

    public MinvoComponent() {
        Image image = image("minvo.png");

        animIdle = new AnimationChannel(image, 6, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.5), 0, 0);
        animWalk = new AnimationChannel(image, 6, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.5), 0, 5);

        texture = new AnimatedTexture(animIdle);

        texture.loop();
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(FRAME_SIZE / 2, FRAME_SIZE / 2));
        entity.getViewComponent().addChild(texture);

    }

}
