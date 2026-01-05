package eu.pb4.farmersdelightpatch.impl.polydex.pages;

import eu.pb4.farmersdelightpatch.impl.polydex.PolydexTextures;
import eu.pb4.farmersdelightpatch.impl.res.GuiTextures;
import eu.pb4.farmersdelightpatch.impl.res.UiResourceCreator;
import eu.pb4.polydex.api.v1.recipe.AbstractRecipePolydexPage;
import eu.pb4.polydex.api.v1.recipe.PageBuilder;
import eu.pb4.polydex.api.v1.recipe.PolydexEntry;
import eu.pb4.polydex.api.v1.recipe.PolydexStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.ArrayList;
import java.util.List;

public class CuttingBoardRecipePage extends AbstractRecipePolydexPage<CuttingBoardRecipe> {
    private static final ItemStack ICON = ModItems.CUTTING_BOARD.get().getDefaultInstance();

    private final List<PolydexStack<ItemStack>> outputs;

    public CuttingBoardRecipePage(RecipeHolder<CuttingBoardRecipe> recipe) {
        super(recipe);

        var results = recipe.value().getRollableResults();

        this.outputs = new ArrayList<>(results.size());
        for (var result : results) {
            this.outputs.add(PolydexStack.of(result.stack(), result.chance()));
        }
    }

    @Override
    public @Nullable Component texture(ServerPlayer player) {
        return PolydexTextures.CUTTING_BOARD;
    }

    @Override
    public ItemStack getOutput(@Nullable PolydexEntry polydexEntry, MinecraftServer minecraftServer) {
        if (polydexEntry == null) {
            return this.outputs.getFirst().getBacking();
        }

        for (var result : this.outputs) {
            if (polydexEntry.isPartOf(result)) {
                return result.getBacking();
            }
        }
        return this.outputs.getFirst().getBacking();
    }

    @Override
    public boolean isOwner(MinecraftServer server, PolydexEntry entry) {
        for (var result : this.outputs) {
            if (entry.isPartOf(result)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack typeIcon(ServerPlayer serverPlayerEntity) {
        return ICON;
    }

    @Override
    public void createPage(@Nullable PolydexEntry polydexEntry, ServerPlayer serverPlayerEntity, PageBuilder pageBuilder) {
        pageBuilder.setIngredient(2, 1, this.recipe.getInput());

        pageBuilder.setOutput(6, 2, this.outputs.toArray(PolydexStack[]::new));
    }
}
