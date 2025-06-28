package eu.pb4.farmersdelightpatch.impl.item;

import eu.pb4.polymer.core.api.item.PolymerItem;
import it.unimi.dsi.fastutil.booleans.BooleanList;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import vectorwing.farmersdelight.common.registry.ModDataComponents;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.List;

public record PolySkilletItem() implements PolymerItem {

    @Override
    public void modifyBasePolymerItemStack(ItemStack out, ItemStack stack, PacketContext context) {
        if (stack.contains(ModDataComponents.COOKING_TIME_LENGTH.get()) && context.getPlayer() instanceof ServerPlayerEntity player) {
            var max = stack.getMaxUseTime(player);
            out.set(DataComponentTypes.MAX_DAMAGE, max);
            out.set(DataComponentTypes.DAMAGE, max - player.getItemUseTime());
            out.set(DataComponentTypes.CUSTOM_MODEL_DATA, new CustomModelDataComponent(List.of(), BooleanList.of(true), List.of(), List.of()));
        }
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext packetContext) {
        return Items.TRIAL_KEY;
    }
}
