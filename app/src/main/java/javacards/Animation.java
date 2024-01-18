package javacards;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class Animation {

    /**
     * Asynchronous sleep for animation purposes.
     * @param delay The amount of time to sleep (in ms).
     */
    private static CompletableFuture<Void> nonBlockingSleep(long delay) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(() -> completableFuture.complete(null), delay, TimeUnit.MILLISECONDS);
        executorService.shutdown();

        return completableFuture;
    }

    /**
     * This function allows moving cards.
     * @param card The card to move.
     * @param pos The position to move to.
     */
    public static void move(Card card, int[] pos) {
        TranslateTransition move = new TranslateTransition(Duration.seconds(0.5), card.card);
        move.setToX(pos[0]);
        move.setToY(pos[1]);
        move.play();
    }

    /**
     * This function allows flipping cards.
     * @param card The card to flip.
     */
    public static void flip(Card card) {
        RotateTransition rotate = new RotateTransition(Duration.seconds(0.5), card.card);
        rotate.setAxis(Rotate.Y_AXIS);
        rotate.setByAngle(180);
        rotate.play();
        nonBlockingSleep(250).thenRun(() -> {
            card.visible = !card.visible;
            if (card.visible) {
                card.img = Config.dir + card.getSuit().charAt(0) + card.getRank() + ".png";
            } else {
                card.img = Config.dir + "back.png";
            }
            Image e = new Image(card.img, 135, 210, true, true);
            card.view.setImage(e);
        });
    }
}
