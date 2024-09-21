package io.github.xenfork.xendatagen.neoforge;

import io.github.xenfork.xendatagen.Xendatagen;
import net.neoforged.fml.common.Mod;

@Mod(Xendatagen.MOD_ID)
public final class XendatagenNeoForge {
    public XendatagenNeoForge() {
        // Run our common setup.
        Xendatagen.init();
    }
}
