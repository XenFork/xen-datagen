package io.github.xenfork.xendatagen.datagen.providers;

import com.google.common.hash.Hashing;
import com.google.common.hash.HashingOutputStream;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class XenBaseProvider<T> implements DataProvider {
    public final PackOutput output;
    public final ConcurrentHashMap<ResourceLocation, T> builds = new ConcurrentHashMap<>();
    public XenBaseProvider(PackOutput output) {
        this.output = output;
    }

    public XenBaseProvider<T> add(ResourceLocation key, T value) {
        builds.put(key, value);
        return this;
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public @NotNull CompletableFuture<?> run(CachedOutput cachedOutput) {
        CompletableFuture<?>[] completableFutures = new CompletableFuture[builds.size()];
        AtomicInteger i = new AtomicInteger(0);

        for (; i.get() < builds.size(); i.getAndIncrement()) {
            builds.forEach((location, object) -> {
                completableFutures[i.get()] = CompletableFuture.runAsync(() -> {
                    try(ByteArrayOutputStream out = new ByteArrayOutputStream();
                        HashingOutputStream hashOut = new HashingOutputStream(Hashing.sha256(), out)
                    ) {
                        hashOut.write(build(object, hashOut, out));
                        cachedOutput.writeIfNeeded(serialize(output, location), out.toByteArray(), hashOut.hash());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

            });
        }
        return CompletableFuture.allOf(completableFutures);
    }

    @SuppressWarnings("UnstableApiUsage")
    public abstract byte[] build(T object, HashingOutputStream hashOut, ByteArrayOutputStream out) throws IOException;

    public abstract Path serialize(PackOutput output, ResourceLocation location);

    @Override
    public @NotNull String getName() {
        return String.valueOf(this.getClass());
    }
}
