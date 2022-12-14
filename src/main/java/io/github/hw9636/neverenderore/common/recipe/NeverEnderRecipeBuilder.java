package io.github.hw9636.neverenderore.common.recipe;

import com.google.gson.JsonObject;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

public class NeverEnderRecipeBuilder implements RecipeBuilder {

    private Block block;
    private ItemStack output;
    private int energy, ticks;

    public NeverEnderRecipeBuilder() {
        ticks = 40;
        energy = 500;
    }

    public NeverEnderRecipeBuilder setBlock(Block block) {
        this.block = block;
        return this;
    }

    public NeverEnderRecipeBuilder setResult(ItemStack result) {
        this.output = result;
        return this;
    }

    public NeverEnderRecipeBuilder setTicks(int ticks) {
        this.ticks = ticks;
        return this;
    }

    public NeverEnderRecipeBuilder setEnergy(int energy) {
        this.energy = energy;
        return this;
    }

    @Override
    public @NotNull RecipeBuilder unlockedBy(@NotNull String pCriterionName, @NotNull CriterionTriggerInstance pCriterionTrigger) {
        return this;
    }

    @Override
    public @NotNull RecipeBuilder group(@Nullable String pGroupName) {
        return this;
    }

    @Override
    public @NotNull Item getResult() {
        return output.getItem();
    }

    @Override
    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, @NotNull ResourceLocation pRecipeId) {
        pFinishedRecipeConsumer.accept(new Result(pRecipeId, output, block, ticks, energy));
    }

    public static class Result implements FinishedRecipe {

        private final ResourceLocation id;
        private final ItemStack result;
        private final Block validBlock;
        private final int ticks, energy;

        private Result(ResourceLocation id, ItemStack result, Block validBlock, int ticks, int energy) {
            this.id = id;
            this.result = result;
            this.validBlock = validBlock;
            this.ticks = ticks;
            this.energy = energy;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.addProperty("block", Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(this.validBlock)).toString());
            JsonObject result = new JsonObject();
            result.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(this.result.getItem())).toString());
            result.addProperty("count", this.result.getCount());
            json.addProperty("ticks", ticks);
            json.addProperty("energy", energy);

            json.add("result", result);
        }

        @Override
        public @NotNull ResourceLocation getId() {
            return id;
        }

        @Override
        public @NotNull RecipeSerializer<?> getType() {
            return NeverEnderOreSerializer.INSTANCE;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
