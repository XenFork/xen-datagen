package io.github.xenfork.xendatagen.neoforge;

import io.github.xenfork.xendatagen.Xendatagen;
import io.github.xenfork.xendatagen.datagen.providers.XenRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;


@EventBusSubscriber(modid = Xendatagen.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class XendatagenNeoForgeDatagen {
    public static void datagen(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        generator.addProvider(event.includeServer(), new XenRecipeProvider(packOutput, lookupProvider));
    }
}
