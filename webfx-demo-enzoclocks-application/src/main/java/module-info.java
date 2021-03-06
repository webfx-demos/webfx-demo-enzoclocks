// File managed by WebFX (DO NOT EDIT MANUALLY)

module webfx.demo.enzoclocks.application {

    // Direct dependencies modules
    requires java.base;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires webfx.extras.type;
    requires webfx.extras.visual.base;
    requires webfx.extras.visual.controls.grid;
    requires webfx.kit.util;
    requires webfx.lib.enzo;
    requires webfx.platform.client.uischeduler;

    // Exported packages
    exports dev.webfx.demo.enzoclocks;
    exports dev.webfx.demo.enzoclocks.circlespacker;
    exports dev.webfx.demo.enzoclocks.settings;

    // Provided services
    provides javafx.application.Application with dev.webfx.demo.enzoclocks.EnzoClocksApplication;

}