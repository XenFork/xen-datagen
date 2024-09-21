package io.github.xenfork.xendatagen.fabric;

import io.github.xenfork.xendatagen.Xendatagen;

public final class XendatagenFabric {

    public void init() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        Xendatagen.init();
    }
}
