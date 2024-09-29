package dev.lisek.crazybytes.ui;

import dev.lisek.crazybytes.config.Config;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Section extends Group {

    HBox header;
    public VBox content;

    public Section(int size, String icon, String title, VBox content) {
        ImageView img = new ImageView(new Image(Config.DIR + "icons/%s.png".formatted(icon), 64, 64, true, true));
        Text text = new Text(title);
        text.setStyle("-fx-font-weight: bold; -fx-font-size: 32px;");
        this.header = new HBox(img, text);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(8));
        header.setSpacing(32);

        this.content = content;
        this.content.setPadding(new Insets(8));

        Rectangle bg = new Rectangle(480, 720/size - 10 * (size - 1));
        bg.setFill(Color.DARKGREEN);
        VBox wrapper = new VBox(new Rectangle(480, 80), this.content);
        StackPane body = new StackPane(bg, wrapper);
        this.getChildren().addAll(body, this.header);
    }
}
