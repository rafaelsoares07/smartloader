package com.gertec.smartloader.app.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Composition root configuration.
 *
 * <p>A single component scan over {@code com.gertec.smartloader} wires the whole app:
 * it picks up every feature {@code @Configuration} (port + use case beans) and every
 * {@code @Component} (feature view controllers, the shell controller and the FXML loader
 * helper), regardless of feature package.</p>
 */
@Configuration
@ComponentScan("com.gertec.smartloader")
public class AppConfig {
}
