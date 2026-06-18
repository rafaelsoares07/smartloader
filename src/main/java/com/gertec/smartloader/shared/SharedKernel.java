package com.gertec.smartloader.shared;

/**
 * Marker for the cross-cutting shared kernel.
 *
 * <p>This module is intentionally minimal in Increment 0. It exists to anchor the
 * {@code com.gertec.smartloader.shared} package and to host future cross-cutting
 * concerns shared by all features (e.g. value types, error model, relative-path
 * resolution with a configurable root).</p>
 *
 * <p>Constraint: this module must remain <b>pure Java</b> — no Spring, no JavaFX,
 * no JPA. The distributed nature of the system (ADB orchestration, ephemeral
 * self-uninstalling on-device components) must be hidden behind a domain port and
 * must never leak into the core.</p>
 *
 * <p>TODO (future increment): file paths must always be RELATIVE to a configurable
 * root — never absolute.</p>
 */
public final class SharedKernel {

    private SharedKernel() {
        // Utility/marker type — not instantiable.
    }
}
