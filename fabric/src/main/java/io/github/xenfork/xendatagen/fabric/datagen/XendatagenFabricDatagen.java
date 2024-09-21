package io.github.xenfork.xendatagen.fabric.datagen;

import io.github.xenfork.xendatagen.datagen.providers.XenRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public interface XendatagenFabricDatagen extends DataGeneratorEntrypoint {

    default void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
        dataGenerator.createPack().addProvider(XenRecipeProvider::new);
    }
}
