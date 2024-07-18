package dev.lisek.crazybytes;

import dev.lisek.crazybytes.entity.Profile;
import dev.lisek.crazybytes.game.Game;
import dev.lisek.crazybytes.ui.Menu;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    public static final Profile profile = new Profile();

    public static final Menu menu = new Menu(profile);

    public static Stage stage;

    public static Game game;

    @Override
    public void start(Stage stage) {
        App.stage = stage;

        stage.setScene(menu);
        stage.show();
    }
}
