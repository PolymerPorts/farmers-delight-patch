package eu.pb4.farmersdelightpatch.mixin.mod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.common.item.SkilletItem;
import xyz.nucleoid.packettweaker.PacketContext;

@Mixin(SkilletItem.class)
public class SkilledItemMixin {
    @Redirect(method = "getItemBarStep", at = @At(value = "INVOKE", target = "Lvectorwing/farmersdelight/common/item/SkilletItem;getClientPlayerHack()Lnet/minecraft/entity/player/PlayerEntity;"))
    private PlayerEntity thereIsNoHack() {
        return PacketContext.get().getPlayer();
    }

    @Redirect(method = "usageTick", at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;isClient:Z"))
    private boolean yesItsClient100Real(World instance) {
        return true;
    }

    @Redirect(method = "usageTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSoundFromEntity(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"))
    private void customPlaySound(World instance, Entity source, Entity entity, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        instance.playSoundFromEntity(null, entity, sound, category, volume, pitch);
    }

    @Inject(method = "usageTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;contains(Lnet/minecraft/component/ComponentType;)Z"))
    private void updateItemOnClient(World level, LivingEntity entity, ItemStack stack, int count, CallbackInfo ci) {
        var slot = entity.getMainHandStack() == stack ? PlayerScreenHandler.HOTBAR_START + ((ServerPlayerEntity) entity).getInventory().getSelectedSlot() : PlayerScreenHandler.OFFHAND_ID;

        ((ServerPlayerEntity) entity).networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(((ServerPlayerEntity) entity).playerScreenHandler.syncId,
                ((ServerPlayerEntity) entity).playerScreenHandler.nextRevision(), slot, stack.copy()));
    }
}
