package eu.pb4.farmersdelightpatch.impl.polydex.pages;

import eu.pb4.farmersdelightpatch.impl.polydex.PolydexTextures;
import eu.pb4.polydex.api.v1.recipe.*;
import eu.pb4.polydex.impl.book.view.crafting.AbstractCraftingRecipePage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.crafting.DoughRecipe;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.List;

public class DoughRecipePage extends AbstractCraftingRecipePage<DoughRecipe> {

    private final List<PolydexIngredient<?>> ingredients;

    public DoughRecipePage(RecipeHolder<DoughRecipe> recipe) {
        super(recipe);
        this.ingredients = List.of(
                PolydexStack.of(Items.WHEAT),
                PolydexStack.of(Items.WATER_BUCKET)
        );
    }

    @Override
    protected SlotDisplay getStacksAt(DoughRecipe doughRecipe, int x, int y, @Nullable PolydexEntry polydexEntry) {
        if (x == 0 && y == 0) {
            return new SlotDisplay.ItemSlotDisplay(Items.WHEAT);
        }
        if (x == 1 && y == 0) {
            return new SlotDisplay.ItemSlotDisplay(Items.WATER_BUCKET);
        }

        return SlotDisplay.Empty.INSTANCE;
    }

    @Override
    public List<PolydexIngredient<?>> ingredients() {
        return ingredients;
    }

    @Override
    public ItemStack getOutput(@Nullable PolydexEntry polydexEntry, MinecraftServer minecraftServer) {
        return ModItems.WHEAT_DOUGH.get().getDefaultInstance();
    }
}
