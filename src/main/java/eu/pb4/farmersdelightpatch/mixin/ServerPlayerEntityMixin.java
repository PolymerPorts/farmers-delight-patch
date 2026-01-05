package eu.pb4.farmersdelightpatch.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import eu.pb4.farmersdelightpatch.impl.ui.CookingPotUi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.entity.container.CookingPotMenu;

import java.util.OptionalInt;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;

@Mixin(value = ServerPlayer.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "openMenu", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V", shift = At.Shift.BEFORE), cancellable = true)
    private void openCustomScreen(MenuProvider factory, CallbackInfoReturnable<OptionalInt> cir, @Local AbstractContainerMenu handler) {
        if (handler instanceof CookingPotMenu wrapped) {
            new CookingPotUi((ServerPlayer) (Object) this, wrapped);
            cir.setReturnValue(OptionalInt.empty());
        }
    }
}