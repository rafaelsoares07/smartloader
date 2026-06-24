package com.gertec.smartloader.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;

@Component
public class SpringFxmlLoader {

    private final ApplicationContext context;

    public SpringFxmlLoader(ApplicationContext context) {
        this.context = context;
    }

    /**
     * @param resourcePath absolute classpath path to the FXML, e.g. {@code /fxml/catalog-view.fxml}
     */
    public Parent load(String resourcePath) {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(context::getBean);
        loader.setLocation(Objects.requireNonNull(
                getClass().getResource(resourcePath),
                () -> "FXML not found on classpath: " + resourcePath));
        try {
            return loader.load();
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load FXML: " + resourcePath, e);
        }
    }
}
