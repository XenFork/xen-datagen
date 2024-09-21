package io.github.xenfork.xendatagen.datagen.providers;

import com.google.common.hash.HashingOutputStream;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;

public class XenLanguageProvider extends XenBaseProvider<Object> {
    public XenLanguageProvider(PackOutput output) {
        super(output);
    }

    @Override
    public byte[] build(Object object, HashingOutputStream hashOut, ByteArrayOutputStream out) throws IOException {
        return new byte[0];
    }

    @Override
    public Path serialize(PackOutput output, ResourceLocation location) {
        return null;
    }
}
