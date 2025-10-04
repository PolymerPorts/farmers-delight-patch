package eu.pb4.farmersdelightpatch.impl.polydex;

import eu.pb4.farmersdelightpatch.impl.polydex.pages.*;
import eu.pb4.farmersdelightpatch.impl.res.GuiTextures;
import eu.pb4.farmersdelightpatch.impl.ui.GuiUtils;
import eu.pb4.polydex.api.v1.recipe.*;
import eu.pb4.sgui.api.elements.GuiElement;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.Text;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.crafting.DoughRecipe;


public class PolydexCompatImpl {

    public static void register() {
        PolydexPage.registerRecipeViewer(CookingPotRecipe.class, CookingPotRecipePage::new);
        PolydexPage.registerRecipeViewer(CuttingBoardRecipe.class, CuttingBoardRecipePage::new);
        PolydexPage.registerRecipeViewer(DoughRecipe.class, DoughRecipePage::new);
    }

    public static GuiElement getButton(RecipeType<?> type) {
        var category = PolydexCategory.of(type);
        return GuiTextures.POLYDEX_BUTTON.get()
                .setName(Text.translatable("text.farmers-delight-patch.recipes"))
                .setCallback((index, type1, action, gui) -> {
                    PolydexPageUtils.openCategoryUi(gui.getPlayer(), category, gui::open);
                    GuiUtils.playClickSound(gui.getPlayer());
                }).build();
    }
}
