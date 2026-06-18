package com.gertec.smartloader.catalog.domain;

/**
 * Domain port for the Catalog feature.
 *
 * <p>Pure domain contract: no framework. The infrastructure layer provides the
 * implementation; the application layer depends only on this interface. This is the
 * inner-most layer — dependencies point inward toward it.</p>
 */
public interface ModuleInfo {

    String message();
}
