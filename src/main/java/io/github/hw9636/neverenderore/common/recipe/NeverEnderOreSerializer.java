package io.github.hw9636.neverenderore.common.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.Charset;

public class NeverEnderOreSerializer implements RecipeSerializer<NeverEnderRecipe> {
    public static final NeverEnderOreSerializer INSTANCE = new NeverEnderOreSerializer();

    @Override
    public @NotNull NeverEnderRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject serializedRecipe) {
        ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(serializedRecipe, "result"));
        Block validBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(serializedRecipe.get("block").getAsString()));
        int ticks = GsonHelper.getAsInt(serializedRecipe, "ticks");
        int energy = GsonHelper.getAsInt(serializedRecipe, "energy");

        return new NeverEnderRecipe(recipeId, result, validBlock, ticks, energy);
    }

    @Override
    public @Nullable NeverEnderRecipe fromNetwork(@NotNull ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {

        int length = pBuffer.readInt();
        Block validBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(pBuffer.readCharSequence(length, Charset.defaultCharset()).toString()));
        ItemStack result = pBuffer.readItem();
        int ticks = pBuffer.readInt();
        int energy = pBuffer.readInt();

        return new NeverEnderRecipe(pRecipeId, result, validBlock, ticks, energy);
    }

    @Override
    public void toNetwork(FriendlyByteBuf pBuffer, NeverEnderRecipe pRecipe) {
        ResourceLocation rl = ForgeRegistries.BLOCKS.getKey(pRecipe.getValidBlock());
        String stringRl = rl != null ? rl.toString() : "minecraft:air";
        pBuffer.writeInt(stringRl.length());
        pBuffer.writeCharSequence(stringRl, Charset.defaultCharset());
        pBuffer.writeItemStack(pRecipe.getResultItem(), false);
        pBuffer.writeInt(pRecipe.getTicks());
        pBuffer.writeInt(pRecipe.getEnergy());
    }
}
