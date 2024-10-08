package uet.oop.bombermanoop.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.Required;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;
import uet.oop.bombermanoop.BombermanType;

import java.util.Random;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static uet.oop.bombermanoop.BombermanApp.TILE_SIZE;
import static uet.oop.bombermanoop.BombermanApp.is_died;
import static uet.oop.bombermanoop.components.PlayerComponent.FRAME_SIZE;

@Required(AStarMoveComponent.class)
public class OnealComponent extends EnemyComponent {

    public AStarMoveComponent astar;
    public CellMoveComponent cell;
    private Random random = new Random();
    private AnimatedTexture texture;
    private AnimationChannel animIdle, animWalk;

    public OnealComponent() {
        random.setSeed(1234567890);
        Image image = image("oneal.png");

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
    public void onUpdate(double tpf) {
        if(!is_died) {
            var player = FXGL.getGameWorld().getSingleton(BombermanType.PLAYER);
            int x = player.call("getCellX");
            int y = player.call("getCellY");
//            int x = (int) player.getX() / TILE_SIZE;
//            int y = (int) player.getY() / TILE_SIZE;

            if (getEntity().distance(player) < TILE_SIZE * 5) {
                cell.setSpeed(100);
                astar.moveToCell(x, y);
            } else {
                double rand = Math.random();

                if (rand > 0.8) left();
                else if (rand > 0.6) right();
                else if (rand > 0.4) up();
                else down();
                int speed = 50;
                if (random.nextBoolean())
                    speed = 10 + random.nextInt(80);
                cell.setSpeed(speed);
            }

            if (texture.getAnimationChannel() != animWalk)
                texture.loopAnimationChannel(animWalk);
        }
    }

}
