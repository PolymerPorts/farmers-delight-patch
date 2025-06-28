package eu.pb4.farmersdelightpatch.mixin.mod;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.display.RecipeDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.refabricated.inventory.RecipeWrapper;

import java.util.List;

@Mixin({CookingPotRecipe.class})
public abstract class FDRecipeMixin implements Recipe<RecipeWrapper> {
    @Override
    public boolean isIgnoredInRecipeBook() {
        return true;
    }

    /**
     * @author Patbox
     * @reason No
     */
    @Overwrite
    @Override
    public List<RecipeDisplay> getDisplays() {
        return List.of();
    }
}
