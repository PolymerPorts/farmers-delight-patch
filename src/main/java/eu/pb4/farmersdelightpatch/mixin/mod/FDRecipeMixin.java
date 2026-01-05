package eu.pb4.farmersdelightpatch.mixin.mod;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.refabricated.inventory.RecipeWrapper;

import java.util.List;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.display.RecipeDisplay;

@Mixin({CookingPotRecipe.class})
public abstract class FDRecipeMixin implements Recipe<RecipeWrapper> {
    @Override
    public boolean isSpecial() {
        return true;
    }

    /**
     * @author Patbox
     * @reason No
     */
    @Overwrite
    @Override
    public List<RecipeDisplay> display() {
        return List.of();
    }
}
