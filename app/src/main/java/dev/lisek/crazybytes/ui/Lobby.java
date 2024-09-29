package dev.lisek.crazybytes.ui;

import java.lang.reflect.Constructor;
import java.util.List;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public abstract class Lobby extends Scene {

    static StackPane layout;

    Section modes;
    Section mods;
    public Section players;

    Text title = new Text();
    Button back = new Button("Back");
    public Button play = new Button();

    @SuppressWarnings("unchecked")
    Lobby() {
        super(layout = new StackPane(), 1600, 1000);
        this.getStylesheets().add("style.css");

        ToggleGroup mode = new ToggleGroup();
        VBox toggles = new VBox();
        toggles.setSpacing(8);
        VBox modifiers = new VBox();
        modifiers.setSpacing(8);

        VBox playerList = new VBox();
        playerList.setSpacing(16);
        this.players = new Section(1, "users", "Players", playerList);

        try (ScanResult scanResult = new ClassGraph()
            .enableAllInfo()
            .acceptPackages("dev.lisek.crazybytes.game")
            .scan()) {
            ClassInfoList widgetClasses = scanResult.getSubclasses("dev.lisek.crazybytes.game.Game");
            List<Class<?>> widgetClassNames = widgetClasses.loadClasses();
            for (Class<?> c : widgetClassNames) {
                RadioButton btn = new RadioButton(c.getSimpleName());
                toggles.getChildren().add(btn);
                btn.setToggleGroup(mode);
                btn.setOnAction(
                    eh1 -> {
                        if (!modifiers.getChildren().isEmpty())
                            modifiers.getChildren().clear();
                        try {
                            modifiers.getChildren().addAll((List<Node>) c.getField("modifiers").get(null));
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            System.err.println("WARNING: modifiers for %s don't seem to exist".formatted(c.getSimpleName()));
                        }
                        play.setOnAction(
                            eh2 -> startGame(c.getConstructors()[0], playerList.getChildren())
                        );
                    }
                );
            }
        }

        this.modes = new Section(2, "layers", "Modes", toggles);
        this.mods = new Section(2, "sliders", "Modifiers", modifiers);
        
        VBox left = new VBox(this.modes, this.mods);
        left.setSpacing(20);
        left.setAlignment(Pos.CENTER);

        HBox settings = new HBox(left, this.players);
        settings.setSpacing(20);
        settings.setAlignment(Pos.CENTER);

        StackPane header = new StackPane(new Rectangle(980, 60), this.title);
        this.title.setStyle("-fx-font-weight: bold; -fx-font-size: 24px;");
        HBox btns = new HBox(back, play);
        btns.setSpacing(840);
        btns.setAlignment(Pos.CENTER);
        StackPane footer = new StackPane(new Rectangle(980, 60), btns);
        
        VBox base = new VBox(header, settings, footer);
        base.setSpacing(20);
        base.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(base);
    }

    abstract void startGame(Constructor<?> constructor, List<Node> playerList);
}
