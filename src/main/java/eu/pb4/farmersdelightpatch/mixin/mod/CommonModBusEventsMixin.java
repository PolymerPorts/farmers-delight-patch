package eu.pb4.farmersdelightpatch.mixin.mod;

import eu.pb4.polymer.core.api.item.PolymerItemUtils;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.FoodValues;
import vectorwing.farmersdelight.common.event.CommonModBusEvents;

import java.util.ArrayList;
import java.util.List;

@Mixin(CommonModBusEvents.class)
public class CommonModBusEventsMixin {
    @Inject(method = "onModifyDefaultComponents", at = @At("TAIL"))
    private static void forceResyncData(DefaultItemComponentEvents.ModifyContext context, CallbackInfo ci) {
        if (Configuration.ENABLE_STACKABLE_SOUP_ITEMS.get()) {
            Configuration.SOUP_ITEM_LIST.get().forEach((key) -> {
                var k = Identifier.of(key);
                if (k.getNamespace().equals(Identifier.DEFAULT_NAMESPACE)) {
                    Item item = Registries.ITEM.get(k);
                    PolymerItemUtils.syncDefaultComponent(item, DataComponentTypes.MAX_STACK_SIZE);
                }
            });
        }

        //if (Configuration.RABBIT_STEW_BUFF.get()) {
        //    PolymerItemUtils.syncDefaultComponent(Items.RABBIT_STEW, DataComponentTypes.FOOD);
        //}

        //if (Configuration.VANILLA_SOUP_EXTRA_EFFECTS.get() || Configuration.RABBIT_STEW_BUFF.get()) {
        //    for (Item item : FoodValues.ConsumableValues.VANILLA_SOUP_EFFECTS.keySet()) {
        //        PolymerItemUtils.syncDefaultComponent(item, DataComponentTypes.CONSUMABLE);
        //    }
        //}
    }
}

