package eu.pb4.farmersdelightpatch.impl.item;

import eu.pb4.polymer.core.api.item.PolymerItem;
import it.unimi.dsi.fastutil.booleans.BooleanList;
import vectorwing.farmersdelight.common.registry.ModDataComponents;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.List;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomModelData;

public record PolySkilletItem() implements PolymerItem {

    @Override
    public void modifyBasePolymerItemStack(ItemStack out, ItemStack stack, PacketContext context) {
        if (stack.has(ModDataComponents.COOKING_TIME_LENGTH.get()) && context.getPlayer() instanceof ServerPlayer player) {
            var max = stack.getUseDuration(player);
            out.set(DataComponents.MAX_DAMAGE, max);
            out.set(DataComponents.DAMAGE, max - player.getTicksUsingItem());
            out.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), BooleanList.of(true), List.of(), List.of()));
        }
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext packetContext) {
        return Items.TRIAL_KEY;
    }
}
