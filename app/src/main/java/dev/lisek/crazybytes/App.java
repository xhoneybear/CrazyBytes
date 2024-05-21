package dev.lisek.crazybytes;

import dev.lisek.crazybytes.config.Config;
import dev.lisek.crazybytes.entity.Profile;
import dev.lisek.crazybytes.game.Game;
import dev.lisek.crazybytes.ui.scene.LocalGame;
import dev.lisek.crazybytes.ui.scene.Menu;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    public static final Profile profile = Profile.init();

    public static Stage stage;

    public static Menu menu = new Menu(profile);

    public final static LocalGame local = new LocalGame();
    // private final static SPGame single = new SPGame();
    // private final static MPGame multi = new MPGame();

    public static Game game;

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        stage.setScene(menu);
        stage.show();
    }

    public static void main(String[] args) {
        System.out.println(Config.DIR);
        launch();
    }
}
