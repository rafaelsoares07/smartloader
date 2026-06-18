package com.gertec.smartloader.app;

import javafx.application.Application;

/**
 * Entry point.
 *
 * <p>This class intentionally does NOT extend {@link Application}. Launching JavaFX from a
 * main method that lives in a non-Application class avoids the "JavaFX runtime components
 * are missing" error in classpath (non-modular) mode, and keeps {@code mvn javafx:run}
 * and a future {@code java -jar} working consistently.</p>
 */
public final class Launcher {

    private Launcher() {
    }

    public static void main(String[] args) {
        Application.launch(SmartLoaderApplication.class, args);
    }
}
