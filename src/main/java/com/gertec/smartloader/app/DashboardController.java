package com.gertec.smartloader.app;

import javafx.fxml.FXML;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Controller for the Dashboard home view.
 *
 * <p>The Dashboard is the shell home (composition concern), not a feature. Increment 0:
 * the view is static, honest placeholder content only (no real metrics). No fields are
 * bound yet — the widgets will be connected to real data sources in a future increment.</p>
 */
@Component
@Scope("prototype")
public class DashboardController {

    @FXML
    private void initialize() {
        // TODO (future increment): bind metric cards and panels to real data sources.
    }
}
