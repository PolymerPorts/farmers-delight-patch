package eu.pb4.farmersdelightpatch.impl.res;

import com.mojang.serialization.MapCodec;
import eu.pb4.polymer.resourcepack.extras.api.format.item.property.bool.BooleanProperty;
import net.minecraft.item.ItemStack;
import vectorwing.farmersdelight.common.registry.ModDataComponents;

public class SkilletCookingConditionalItemModelProperty implements BooleanProperty {
    public static final MapCodec<SkilletCookingConditionalItemModelProperty> MAP_CODEC = MapCodec.unit(new SkilletCookingConditionalItemModelProperty());

    public static boolean test(ItemStack stack) {
        return stack.get(ModDataComponents.SKILLET_INGREDIENT.get()) != null;
    }

    @Override
    public MapCodec<? extends BooleanProperty> codec() {
        return MAP_CODEC;
    }
}
