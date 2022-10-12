package uet.oop.bombermanoop.components;

import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.image;
import static uet.oop.bombermanoop.components.PlayerComponent.FRAME_SIZE;

public class PassComponent extends EnemyComponent{
    private AnimatedTexture texture;
    private AnimationChannel animIdle, animWalk;

    public PassComponent() {
        Image image = image("pass.png");

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

    @Override
    public void onUpdate(double tpf) {};
}
