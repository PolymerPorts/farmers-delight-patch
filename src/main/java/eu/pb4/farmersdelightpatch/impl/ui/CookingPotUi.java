package eu.pb4.farmersdelightpatch.impl.ui;

import eu.pb4.farmersdelightpatch.impl.FarmersDelightPolymerPatch;
import eu.pb4.farmersdelightpatch.impl.polydex.PolydexCompat;
import eu.pb4.farmersdelightpatch.impl.res.GuiTextures;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.elements.GuiElementInterface;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import vectorwing.farmersdelight.common.block.entity.container.CookingPotMealSlot;
import vectorwing.farmersdelight.common.block.entity.container.CookingPotMenu;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import vectorwing.farmersdelight.common.utility.TextUtils;

public class CookingPotUi extends SimpleGui {
    private final CookingPotMenu wrapped;

    public CookingPotUi(ServerPlayer player, CookingPotMenu wrapped) {
        super(MenuType.GENERIC_9x3, player, false);
        this.wrapped = wrapped;
        this.setTitle(GuiTextures.COOKING_POT.apply(this.wrapped.blockEntity.getDisplayName()));
        int slot = 0;

        for (int row = 0; row < 2; ++row) {
            for (int column = 0; column < 3; ++column) {
                this.setSlotRedirect(column + row * 9 + 1, wrapped.slots.get(slot++));
            }
        }

        this.setSlot(6, new CookingPotMealDisplay(this.wrapped, (CookingPotMealSlot) wrapped.slots.get(slot++)));
        this.setSlotRedirect(5 + 9 * 2, wrapped.slots.get(slot++));
        this.setSlotRedirect(7 + 9 * 2, wrapped.slots.get(slot++));
        this.updateState();

        this.setSlot(2 * 9, PolydexCompat.getButton(ModRecipeTypes.COOKING.get()));

        this.open();
    }

    @Override
    public void onTick() {
        super.onTick();
        this.updateState();
    }

    @Override
    public void onScreenHandlerClosed() {
        super.onScreenHandlerClosed();
        this.wrapped.removed(this.player);
    }

    private void updateState() {
        this.setSlot(4, GuiTextures.PROGRESS_COOKING_POT.getCeil(this.wrapped.getCookProgressionScaled() / 24f));
        this.setSlot(2 + 9 * 2, (this.wrapped.isHeated() ? GuiTextures.HEATED.get() : GuiTextures.EMPTY_BUILDER.get())
                .setName(TextUtils.getTranslation("container.cooking_pot." + (this.wrapped.isHeated() ? "heated" : "not_heated"))));

    }

    private record CookingPotMealDisplay(CookingPotMenu wrapped, CookingPotMealSlot slot) implements GuiElementInterface {
        @Override
        public ItemStack getItemStack() {
            var mealStack = slot.getItem();
            var b = GuiElementBuilder.from(slot.getItem());
            b.setName(Component.translatable(mealStack.getItem().getDescriptionId()).withStyle(mealStack.getRarity().color()));
            ItemStack containerStack = this.wrapped.blockEntity.getContainer();
            String container = !containerStack.isEmpty() ? Component.translatable(containerStack.getItem().getDescriptionId()).getString() : "";
            b.addLoreLine(TextUtils.getTranslation("container.cooking_pot.served_on", container).withStyle(ChatFormatting.GRAY));
            b.hideDefaultTooltip();
            b.setMaxCount(99);

            return b.asStack();
        }
    }
}
