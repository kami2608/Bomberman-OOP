package uet.oop.bombermanoop.components;

import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;
import java.util.Random;

import static com.almasb.fxgl.dsl.FXGLForKtKt.image;
import static uet.oop.bombermanoop.components.PlayerComponent.FRAME_SIZE;

public class DahlComponent extends EnemyComponent{
    public CellMoveComponent cell;
    private AnimatedTexture texture;
    private AnimationChannel animIdle, animWalk;
    private Random random = new Random();
    private int cnt = 0;

    public DahlComponent() {
        int cnt = 0;
        random.setSeed(1234567890);
        Image image = image("dahl.png");

        animIdle = new AnimationChannel(image, 6, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.5), 0, 0);
        animWalk = new AnimationChannel(image, 6, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.5), 0, 2);

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
        if(cnt % 50 == 0) {
            int speed = 50;
            if (random.nextBoolean())
                speed = 10 + random.nextInt(100);
            cell.setSpeed(speed);
        }

    }

}
