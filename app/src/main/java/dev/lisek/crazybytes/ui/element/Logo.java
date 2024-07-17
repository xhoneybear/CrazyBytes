package dev.lisek.crazybytes.ui.element;

import dev.lisek.crazybytes.config.Config;
import javafx.scene.Group;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Logo extends Group {
    private static final ImageView c8 = new ImageView(new Image(Config.DIR + "logo.png", 591, 321, true, true));
    private static final Rectangle rec = new Rectangle(551, 281);
    private static final Text subtitle = new Text("The pixel-perfect card shedding game!");
    private static final PerspectiveTransform pt = new PerspectiveTransform();

    public Logo() {
        super(rec, c8, subtitle);

        rec.setFill(Paint.valueOf(Color.BLACK.toString()));
        rec.setX(20);
        rec.setY(20);

        subtitle.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        subtitle.setX(100);
        subtitle.setY(361);

        pt.setUlx(60);
        pt.setUly(60);
        pt.setUrx(546);
        pt.setLly(291);
        pt.setLrx(591);
        pt.setLry(321);

        this.setEffect(pt);
        this.setCache(true);
    }
}
