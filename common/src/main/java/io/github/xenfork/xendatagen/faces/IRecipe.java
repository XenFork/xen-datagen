package io.github.xenfork.xendatagen.faces;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Supplier;


@SuppressWarnings("unchecked")
public interface IRecipe<T extends RecipeProvider & IRecipe<T, E, F>, E extends RecipeBuilder, F extends RecipeOutput> {

    /**
     * {@link Map}
     * @param builds Map.of(kv kv kv kv kv kv kv kv)
     * @return this
     */
    default T shaped(Map<ResourceLocation, E> builds) {
        this.builders().putAll(builds);
        return self();
    }

    /**
     * build Recipes {@link Map#forEach(BiConsumer)}
     * @param recipeOutput {@link RecipeOutput}
     */
    default void buildRecipes(RecipeOutput recipeOutput) {
        builders().forEach((location, recipeBuilder) -> recipeBuilder.save(recipeOutput, location));
    }

    default T shaped(ResourceLocation location, E e) {
        this.builders().put(location, e);
        return self();
    }

    default T self() {
        return (T) this;
    }

    default T writeShapedRecipe(RecipeCategory category, ItemStack result, RelativePosition relativePosition, Pair<String, Item[]>[] pairs) {
        ShapedRecipeBuilder define = ShapedRecipeBuilder
                .shaped(category, result.getItem(), result.getCount());
        Map<Character, Item> patternItems = new HashMap<>();
        for (Pair<String, Item[]> pair : pairs) {
            Item[] items = pair.getSecond();
            String pattern = pair.getFirst();
            if (pattern.length() != items.length)
                throw new IllegalArgumentException("pattern length != items length");
            for (int i = 0; i < items.length; i++) {
                char c = pattern.charAt(i);
                if (c == ' ') {
                    continue;
                }
                Item item = items[i];

                if (item.equals(Items.AIR)) {
                    pattern = pattern.replace(c, ' ');
                }
                patternItems.put(c, item);
            }
            define.pattern(pattern);
        }
        patternItems.forEach((c, item) -> {
            define
                    .unlockedBy("has_" + resourceLocation(item).getPath(), RecipeProvider.has(item))
                    .define(c, item);
        });

        return shaped(resourceLocation(result.getItem()).withSuffix("_" + relativePosition.get()), (E) define);
    }


    /**
     * get item key {@link net.minecraft.core.DefaultedRegistry#getKey(Object)}}
     * @param like {@link ItemLike}
     * @return {@link ResourceLocation}
     */
    default ResourceLocation resourceLocation(ItemLike like) {
        return BuiltInRegistries.ITEM.getKey(like.asItem());
    }

    default Map<ResourceLocation, E> builders() {
        class Handle {
            public final Map<ResourceLocation, E> builders = new ConcurrentHashMap<>();
            public static final Handle handle = new Handle();
        }
        return Handle.handle.builders;
    }

    enum RelativePosition implements Supplier<String> {
        defaultPattern,
        /**
         * AB   ->  CD
         * CD   ->  AB
         */
        verticalMirroring,
        /**
         * AB   ->  BA
         * CD   ->  DC
         */
        horizontalMirroring,
        /**
         * AB   ->  CA
         * CD   ->  DB
         */
        rotateRight,
        /**
         * AB   ->  BD
         * CD   ->  AC
         */
        rotateLeft,
        /**
         * AB   ->  DC
         * CD   ->  BA
         */
        rotate180;


        @Override
        public String get() {
            return name().toLowerCase(Locale.ROOT);
        }
    }
}
