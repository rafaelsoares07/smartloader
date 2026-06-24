package com.gertec.smartloader.app;

import com.gertec.smartloader.app.config.AppConfig;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Objects;

public class SmartLoaderApplication extends Application {

    private static final String MAIN_SHELL_FXML = "/fxml/main-shell.fxml";
    private static final String THEME_CSS = "/css/theme.css";

    private AnnotationConfigApplicationContext context;

    @Override
    public void init() {
        // Spring used ONLY for dependency injection (annotation-based, NOT web).
        context = new AnnotationConfigApplicationContext(AppConfig.class);
    }

    @Override
    public void start(Stage stage) {
        SpringFxmlLoader fxmlLoader = context.getBean(SpringFxmlLoader.class);
        Parent root = fxmlLoader.load(MAIN_SHELL_FXML);

        Scene scene = new Scene(root, 1180, 720);
        // Single centralized stylesheet applied at the Scene level — it cascades to the
        // feature views loaded into the content area.
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource(THEME_CSS),
                        "theme.css not found on classpath").toExternalForm());

        stage.setTitle("SmartLoader v1.0.0 — Gertec");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        if (context != null) {
            context.close();
        }
    }
}
