package dev.lisek.crazybytes.entity;

import dev.lisek.crazybytes.App;
import dev.lisek.crazybytes.config.Config;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

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
    public final Text name;
    public final Text exp;
    public final Text lvl;
    public final Text games;
    public final Text wins;
    public final ImageView image;
    public final ImageView border = new ImageView(new Image(Config.DIR + "border.png", 120, 120, true, true));
    public final Group avatar;
    public final Rectangle expBar;
    public final VBox data;
    private final Profile profile;

    public ProfileCard(Profile profile, boolean editable) {
        this.profile = profile;

        this.name = new Text(profile.name());
        this.name.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        this.name.setFill(Color.WHITE);

        this.image = new ImageView(new Image(profile.avatar(), 96, 96, true, true));
        this.image.setX(12);
        this.image.setY(12);

        Rectangle base = new Rectangle(96, 96);
        base.setX(12);
        base.setY(12);

        Node nameBox;

        if (editable) {
            TextField nameField = new TextField(profile.name());
            nameField.setMouseTransparent(true);
            nameField.setOpacity(0);
            nameField.setOnKeyPressed(eh -> {
                if (eh.getCode() == KeyCode.ENTER) {
                    this.profile.update("name", nameField.getText());
                    this.update("name");
                    nameField.setMouseTransparent(true);
                    nameField.setOpacity(0);
                    this.name.requestFocus();
                }
            });
            ImageView editName = new ImageView(new Image(Config.DIR + "icons/edit.png", 16, 16, true, true));
            editName.setTranslateY(4);
            editName.setOpacity(0);
            editName.setCursor(Cursor.HAND);
            editName.setOnMouseClicked(eh -> {
                nameField.setMouseTransparent(false);
                nameField.setOpacity(1);
                nameField.requestFocus();
            });
            HBox nameEditable = new HBox(this.name, editName);
            nameEditable.setSpacing(8);
            nameBox = new StackPane(nameEditable, nameField);
            nameBox.setOnMouseEntered(eh -> editName.setOpacity(1));
            nameBox.setOnMouseExited(eh -> editName.setOpacity(0));

            ImageView editImg = new ImageView(new Image(Config.DIR + "icons/edit.png", 48, 48, true, true));
            editImg.setX(24);
            editImg.setY(24);
            Group edit = new Group(new Rectangle(96, 96), editImg);
            edit.setTranslateX(12);
            edit.setTranslateY(12);
            edit.setOpacity(0);
            edit.setCursor(Cursor.HAND);
            edit.setOnMouseClicked(eh -> {
                this.profile.update("avatar", "file:" + new FileChooser().showOpenDialog(App.stage).getAbsolutePath());
                this.update("avatar");
            });

            this.avatar = new Group(base, this.image, edit, this.border);
            this.avatar.setOnMouseEntered(eh -> edit.setOpacity(0.5));
            this.avatar.setOnMouseExited(eh -> edit.setOpacity(0));
        } else {
            this.avatar = new Group(base, this.image, this.border);
            nameBox = this.name;
        }

        this.exp = new Text("" + profile.exp());
        this.lvl = new Text(Integer.toString(profile.exp()/1000));
        this.lvl.setStyle("-fx-font-weight: bold;");
        this.games = new Text("" + profile.games());
        this.wins = new Text("" + profile.wins());

        this.expBar = new Bar(2 * (profile.exp() % 1000) / 10, 10);
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
            nameBox,
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
    public ProfileCard(Profile profile) {
        this(profile, false);
    }

    public void update(String key) {
        switch (key) {
            case "name" -> this.name.setText(this.profile.name());
            case "avatar" -> this.image.setImage(new Image(this.profile.avatar(), 96, 96, true, true));
            case "exp" -> {
                this.exp.setText(Integer.toString(profile.exp()));
                this.lvl.setText(Integer.toString(profile.exp()/1000));
                this.expBar.setWidth(0.2 * (profile.exp() % 1000));
            }
            case "games" -> this.games.setText(Integer.toString(profile.games()));
            case "wins" -> this.wins.setText(Integer.toString(profile.wins()));
            default -> System.err.println("Unknown key: " + key);
        }
    }
}
