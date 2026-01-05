package eu.pb4.farmersdelightpatch.mixin.mod;

import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.common.item.SkilletItem;
import xyz.nucleoid.packettweaker.PacketContext;

@Mixin(SkilletItem.class)
public class SkilledItemMixin {
    @Redirect(method = "getBarWidth", at = @At(value = "INVOKE", target = "Lvectorwing/farmersdelight/common/item/SkilletItem;getClientPlayerHack()Lnet/minecraft/world/entity/player/Player;"))
    private Player thereIsNoHack() {
        return PacketContext.get().getPlayer();
    }

    @Redirect(method = "onUseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isClientSide()Z"))
    private boolean yesItsClient100Real(Level instance) {
        return true;
    }

    @Redirect(method = "onUseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"))
    private void customPlaySound(Level instance, Entity source, Entity entity, SoundEvent sound, SoundSource category, float volume, float pitch) {
        instance.playSound(null, entity, sound, category, volume, pitch);
    }

    @Inject(method = "onUseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;has(Lnet/minecraft/core/component/DataComponentType;)Z"))
    private void updateItemOnClient(Level level, LivingEntity entity, ItemStack stack, int count, CallbackInfo ci) {
        var slot = entity.getMainHandItem() == stack ? InventoryMenu.USE_ROW_SLOT_START + ((ServerPlayer) entity).getInventory().getSelectedSlot() : InventoryMenu.SHIELD_SLOT;

        ((ServerPlayer) entity).connection.send(new ClientboundContainerSetSlotPacket(((ServerPlayer) entity).inventoryMenu.containerId,
                ((ServerPlayer) entity).inventoryMenu.incrementStateId(), slot, stack.copy()));
    }
}
