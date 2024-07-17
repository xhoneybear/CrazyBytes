module JavaCards {
    requires javafx.controls;
    requires transitive javafx.graphics;

    requires com.google.gson;

    requires io.github.classgraph;

    exports dev.lisek.crazybytes;
}