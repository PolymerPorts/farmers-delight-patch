package eu.pb4.farmersdelightpatch.impl.polydex.pages;

import eu.pb4.farmersdelightpatch.impl.polydex.PolydexTextures;
import eu.pb4.farmersdelightpatch.impl.res.GuiTextures;
import eu.pb4.farmersdelightpatch.impl.res.UiResourceCreator;
import eu.pb4.polydex.api.v1.recipe.AbstractRecipePolydexPage;
import eu.pb4.polydex.api.v1.recipe.PageBuilder;
import eu.pb4.polydex.api.v1.recipe.PolydexEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModItems;

public class CookingPotRecipePage extends AbstractRecipePolydexPage<CookingPotRecipe> {
    private static final ItemStack ICON = ModItems.COOKING_POT.get().getDefaultInstance();

    public CookingPotRecipePage(RecipeHolder<CookingPotRecipe> recipe) {
        super(recipe);
    }

    @Override
    public @Nullable Component texture(ServerPlayer player) {
        return PolydexTextures.COOKING_POT;
    }

    @Override
    public ItemStack getOutput(@Nullable PolydexEntry polydexEntry, MinecraftServer minecraftServer) {
        return this.recipe.result();
    }

    @Override
    public ItemStack typeIcon(ServerPlayer serverPlayerEntity) {
        return ICON;
    }

    @Override
    public void createPage(@Nullable PolydexEntry polydexEntry, ServerPlayer serverPlayerEntity, PageBuilder pageBuilder) {
        int i = 0;
        for (var item : this.recipe.input()) {
            pageBuilder.setIngredient(i % 3 + 1, i / 3 + 1, item);
            i++;
        }

        pageBuilder.setIngredient(5, 3, this.recipe.container());
        pageBuilder.setOutput(6, 1, this.recipe.result().copy());
        pageBuilder.setOutput(7, 3, this.recipe.result().copy());
    }
}
