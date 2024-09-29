package dev.lisek.crazybytes.ui;

import dev.lisek.crazybytes.entity.Profile;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class PlayerEntry extends HBox {

    private final Profile profile;
    private final Text username;
    private final ImageView avatar;
    public final Rectangle ready = new Rectangle(8, 48);

    public PlayerEntry(Profile profile) {
        this.profile = profile;
        this.setAlignment(Pos.TOP_CENTER);
        avatar = new ImageView(new Image(profile.avatar(), 48, 48, true, true));
        avatar.setMouseTransparent(true);
        HBox pfp = new HBox(this.ready, avatar);
        this.ready.setFill(Color.RED);
        username = new Text(profile.name());
        username.setStyle("-fx-font-size: 18px;");
        username.setWrappingWidth(240);
        Text level = new Text(String.valueOf(profile.exp() / 100));
        level.setStyle("-fx-font-size: 18px;");
        level.setTranslateX(80);
        this.getChildren().addAll(pfp, new StackPane(username, level));
        this.setSpacing(16);
    }

    public void update(String key) {
        switch (key) {
            case "name" -> this.username.setText(this.profile.name());
            case "avatar" -> this.avatar.setImage(new Image(this.profile.avatar(), 48, 48, true, true));
            case "ready" -> {
                if (this.ready.getFill() == Color.RED) {
                    this.ready.setFill(Color.GREEN);
                } else {
                    this.ready.setFill(Color.RED);
                }
            }
            default -> System.err.println("Unknown key: " + key);
        }
    }
}
