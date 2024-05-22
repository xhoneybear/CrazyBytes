package dev.lisek.crazybytes.ui.element;

import dev.lisek.crazybytes.config.Config;
import dev.lisek.crazybytes.entity.Profile;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

class Bar extends Rectangle {
    public Bar(int x, int y) {
        super(x, y);
        this.setFill(Color.WHITE);
        this.setArcHeight(10);
        this.setArcWidth(10);
        this.setOpacity(0.5);
    }
}

public class ProfileCard extends HBox {
    public final Text name, exp, lvl, games, wins;
    public final ImageView image, border = new ImageView(new Image(Config.DIR + "border.png", 120, 120, true, true));
    public final Group avatar;
    public final Rectangle expBar;
    public final VBox data;
    private final Profile profile;

    public ProfileCard(Profile profile) {
        this.profile = profile;

        this.name = new Text(profile.name);
        this.name.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        this.name.setFill(Color.WHITE);
        this.name.setWrappingWidth(240);

        this.image = new ImageView(new Image(profile.avatar, 96, 96, true, true));
        this.image.setX(12);
        this.image.setY(12);

        Rectangle base = new Rectangle(96, 96);
        base.setX(12);
        base.setY(12);
        this.avatar = new Group(base, this.image, this.border);

        this.exp = new Text("" + profile.exp);
        this.lvl = new Text(Integer.toString(profile.exp/100));
        this.lvl.setStyle("-fx-font-weight: bold;");
        this.games = new Text("" + profile.games);
        this.wins = new Text("" + profile.wins);

        this.expBar = new Bar(2 * (profile.exp % 100), 10);
        Group bar = new Group(new Bar(200, 10), this.expBar);

        HBox xpLvl = new HBox(bar, this.lvl);
        xpLvl.setSpacing(8);
        xpLvl.setTranslateY(4);
        xpLvl.setAlignment(Pos.CENTER_LEFT);
        xpLvl.setPadding(new Insets(0, 0, 8, 0));

        Text e = new Text("Exp: ");
        e.setStyle("-fx-font-weight: bold;");
        Text g = new Text("Games: ");
        g.setStyle("-fx-font-weight: bold;");
        Text w = new Text("Wins: ");
        w.setStyle("-fx-font-weight: bold;");

        this.data = new VBox(
            this.name,
            xpLvl,
            new HBox(e, this.exp),
            new HBox(g, this.games),
            new HBox(w, this.wins)
        );
        data.setSpacing(3);

        this.getChildren().addAll(this.avatar, this.data);
        this.setAlignment(Pos.TOP_CENTER);
        this.setPadding(new Insets(16));
        this.setSpacing(4);
    }

    public void update(String key, String value) {
        switch (key) {
            case "name" -> this.name.setText(value);
            case "avatar" -> this.image.setImage(new Image(this.profile.avatar, 96, 96, true, true));
            case "exp" -> {
                this.exp.setText(Integer.toString(profile.exp));
                this.lvl.setText(Integer.toString(profile.exp/100));
                this.expBar.setWidth(2 * (profile.exp % 100));
            }
            case "games" -> this.games.setText(Integer.toString(profile.games));
            case "wins" -> this.wins.setText(Integer.toString(profile.wins));
        }
    }
}
