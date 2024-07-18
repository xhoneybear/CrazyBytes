module JavaCards {
    requires java.prefs;

    requires javafx.controls;
    requires transitive javafx.graphics;

    requires io.github.classgraph;

    exports dev.lisek.crazybytes;
}