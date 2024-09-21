package io.github.xenfork.xendatagen.datagen.providers;

import com.mojang.datafixers.util.Pair;
import io.github.xenfork.xendatagen.faces.I2x2Shaped;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings({"DuplicatedCode", "unchecked", "unused"})
public class XenRecipeProvider extends VanillaRecipeProvider implements I2x2Shaped<XenRecipeProvider, ShapedRecipeBuilder, RecipeOutput> {

    /**
     * {@link net.minecraft.data.recipes.RecipeProvider#RecipeProvider(PackOutput, CompletableFuture)}
     * @param packOutput output
     * @param completableFuture multi-thread
     */
    public XenRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(packOutput, completableFuture);
    }

    /**
     * This is 2x2 shaped recipe
     * AB
     * CD  -> result
     * @param category {@link RecipeCategory}
     * @param result {@link ItemStack}
     * @param A {@link Item}
     * @param B {@link Item}
     * @param C {@link Item}
     * @param D {@link Item}
     * @param relativePositions {@link RelativePosition}
     * @return this
     */

    public XenRecipeProvider abcdShaped(RecipeCategory category, ItemStack result, Item A, Item B, Item C, Item D, RelativePosition... relativePositions) {
        Arrays.stream(relativePositions)
                .forEach(relativePosition -> {
                    switch(relativePosition) {
                        case defaultPattern -> writeShapedRecipe(category, result, relativePosition, new Pair[]{new Pair<>(A, B), new Pair<>(C, D)});
                        case verticalMirroring -> writeShapedRecipe(category, result, relativePosition, new Pair[]{new Pair<>(C, D), new Pair<>(A, B)});
                        case horizontalMirroring -> writeShapedRecipe(category, result, relativePosition, new Pair[]{new Pair<>(B, A), new Pair<>(D, C)});
                        case rotateRight -> writeShapedRecipe(category, result, relativePosition, new Pair[]{new Pair<>(C, A), new Pair<>(D, B)});
                        case rotateLeft -> writeShapedRecipe(category, result, relativePosition, new Pair[]{new Pair<>(B, D), new Pair<>(A, C)});
                        case rotate180 -> writeShapedRecipe(category, result, relativePosition, new Pair[]{new Pair<>(D, C), new Pair<>(B, A)});
                    }
                });
        return this;

    }



    /**
     * This is 2x2 shaped recipe
     * AB
     * BA  -> result
     * @param category {@link RecipeCategory}
     * @param result {@link ItemStack}
     * @param A {@link Item}
     * @param B {@link Item}
     * @param relativePositions {@link RelativePosition}
     * @return this
     */
    public XenRecipeProvider abbaShaped(RecipeCategory category, ItemStack result, Item A, Item B, RelativePosition... relativePositions) {
        Arrays.stream(relativePositions)
                .filter(relativePosition -> switch (relativePosition) {
                    case defaultPattern, verticalMirroring -> true;
                    default -> false;
                })
                .forEach(relativePosition -> {
                    switch (relativePosition) {
                        case defaultPattern -> writeShapedRecipe(category, result, relativePosition, new Pair[]{
                                new Pair<>("AB", new Item[]{A, B}),
                                new Pair<>("BA", new Item[]{B, A})
                        });
                        case verticalMirroring -> writeShapedRecipe(category, result, relativePosition, new Pair[]{
                                new Pair<>("AB", new Item[]{B, A}),
                                new Pair<>("BA", new Item[]{A, B})
                        });
                    }
                });
        return this;
    }

    /**
     * This is 2x2 shaped recipe
     * AA
     * BB  -> result
     * @param category {@link RecipeCategory}
     * @param result {@link ItemStack}
     * @param A {@link Item}
     * @param B {@link Item}
     * @param relativePositions {@link RelativePosition}
     * @return this
     */
    public XenRecipeProvider aabbShaped(RecipeCategory category, ItemStack result, Item A, Item B, RelativePosition... relativePositions) {
        Arrays.stream(relativePositions)
                .filter(relativePosition -> switch(relativePosition) {
                    case defaultPattern, verticalMirroring -> true;
                    default -> false;
                })
                .forEach(relativePosition -> {
                    switch (relativePosition) {
                        case defaultPattern -> writeShapedRecipe(category, result, relativePosition, new Pair[]{
                                new Pair<>("AB", new Item[]{A, A}),
                                new Pair<>("BA", new Item[]{B, B})
                        });
                        case verticalMirroring -> writeShapedRecipe(category, result, relativePosition, new Pair[]{
                                new Pair<>("AB", new Item[]{B, B}),
                                new Pair<>("BA", new Item[]{A, A})
                        });
                    }
                });
        return this;
    }

    /**
     * This is 2x2 shaped recipe
     * AB
     * AB  -> result
     * @param category {@link RecipeCategory}
     * @param result {@link ItemStack}
     * @param A {@link Item}
     * @param B {@link Item}
     * @param relativePositions {@link RelativePosition}
     * @return this
     */
    public XenRecipeProvider ababShaped(RecipeCategory category, ItemStack result, Item A, Item B, RelativePosition... relativePositions) {
        Arrays.stream(relativePositions)
                .filter(relativePosition -> switch(relativePosition) {
                    case defaultPattern, horizontalMirroring -> true;
                    default -> false;
                })
                .forEach(relativePosition -> {
                    switch (relativePosition) {
                        case defaultPattern -> writeShapedRecipe(category, result, relativePosition, new Pair[]{
                                new Pair<>("AB", new Item[]{A, B}),
                                new Pair<>("BA", new Item[]{A, B})
                        });
                        case horizontalMirroring -> writeShapedRecipe(category, result, relativePosition, new Pair[]{
                                new Pair<>("AB", new Item[]{B, A}),
                                new Pair<>("BA", new Item[]{B, A})
                        });
                    }
                });

        return this;
    }


    @Override
    public void buildRecipes(RecipeOutput recipeOutput) {
        I2x2Shaped.super.buildRecipes(recipeOutput);
    }
}
