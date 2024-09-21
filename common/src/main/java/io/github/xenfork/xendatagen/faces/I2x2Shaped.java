package io.github.xenfork.xendatagen.faces;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public interface I2x2Shaped<T extends RecipeProvider & IRecipe<T, ShapedRecipeBuilder, F>, F extends RecipeOutput> extends IRecipe<T, ShapedRecipeBuilder, F> {
    enum Shape {
        Point("."),// 1x1
        One(".."),// 2x1 3x1
        Corners("""
                .       .       .       .
                ..      .       .       ...
                        ..      ...
                """),//2x2 3x3 2x3 3x2
        RectangleEmbeddedRectangle("""
                ...
                . .
                ...
                """)
        ;
        public final String desc;
        Shape(String desc) {
            this.desc = desc;
        }
    }

    //example
    String regexPattern = "[a-zA-Z]\\s+[a-zA-Z]\\s+[a-zA-Z]";
    Pattern itemStackPattern = Pattern.compile("[=|*]");
    /*
        [A, B, C]
        [D, E, F] -> J
        [G, H. I]
     */
    default T doShape(RecipeCategory category, String recipes, ResourceLocation custom) {
        /*
            A A A       |   C -> A
            A A A -> B  |
            A A A       |
            :: A=minecraft:diamond * 1
            :: B=minecraft:diamond_block * 1
            :: C=minecraft:iron_block * 1
            :: D=minecraft:iron_ingot * 9
            ;   <------ this
            A A A       |   C -> A
            A A A -> B  |
            A A A       |
            :: A=minecraft:copper_ingot * 1
            :: B=minecraft:copper_block * 1
            :: C=minecraft:gold_block * 1
            :: D=minecraft:gold_ingot * 9
            ;   <------ this
         */
        return doShape(category, recipes.split(";"), custom);
    }

    default T doShape(RecipeCategory category, String[] recipesArray,ResourceLocation custom) {
        for (String recipes : recipesArray) {
            /*
                A A A       |   C -> A
                A A A -> B  |
                A A A       |
        this--> :: A=minecraft:diamond * 1
        this--> :: B=minecraft:diamond_block * 1
        this--> :: C=minecraft:iron_block * 1
        this--> :: D=minecraft:iron_ingot * 9
             */
            if (!recipes.matches("::")) {
                throw new IllegalArgumentException("You don't assume his gender");
            }
            Map<Character, ItemStack> map = new LinkedHashMap<>();
            String[] split = recipes.split("::");
            for (String s : split) {
                // is A=minecraft:diamond * 1

                if (itemStackPattern.matcher(s).matches()) {
                    String[] charItemCount = itemStackPattern.split(s);
                    map.put(charItemCount[0].trim().charAt(0), new ItemStack(
                            BuiltInRegistries.ITEM.get(ResourceLocation.parse(charItemCount[1].trim())),
                            charItemCount.length == 3 ? Integer.parseInt(charItemCount[2].trim()) : 1
                    ));
                } else {
                    /*

                        A A A       |   C -> A
                        A A A -> B  |
                        A A A       |
                        line from
                        A A A       |   C -> A
                     */
                    String[] lines = s.split("\n");
                    char[][] charsArray = new char[lines.length][];
                    Pattern compile = Pattern.compile("->");
                    Matcher matcher = compile.matcher(s);
                    int i = 0;

                    while (matcher.find()) {
                        i++;
                    }

                }
            }
        }




        return self();
    }

    default String kv(int k, int v) {
        return "(%d, %d)".formatted(k, v);
    }

    default Pair<Integer, Integer> check(Item[][] items) {
        return new Pair<>(items.length, Arrays.stream(items).map(it -> it.length).max(Comparator.naturalOrder()).orElse(0));
    }

}
