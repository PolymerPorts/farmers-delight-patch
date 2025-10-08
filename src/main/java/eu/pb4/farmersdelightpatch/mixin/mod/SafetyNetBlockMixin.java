package eu.pb4.farmersdelightpatch.mixin.mod;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.common.block.SafetyNetBlock;

import java.util.Optional;

@Mixin(SafetyNetBlock.class)
public class SafetyNetBlockMixin {
    @Inject(method = "bounceEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setVelocity(DDD)V", shift = At.Shift.AFTER))
    private void bounceThePlayer(Entity entityIn, CallbackInfo ci, @Local double entityWeightOffset) {
        if (entityIn instanceof ServerPlayerEntity player) {
            player.networkHandler.sendPacket(new ExplosionS2CPacket(Vec3d.ZERO.add(0, -99999, 0),
                0f, 0,
                Optional.of(new Vec3d(0, (player.networkHandler.getLatency() > 50 || player.isOnGround() ? 0 : -player.getMovement().y) + player.getVelocity().y, 0)),
                ParticleTypes.BUBBLE_POP,
                Registries.SOUND_EVENT.getEntry(SoundEvents.INTENTIONALLY_EMPTY),
                Pool.empty()
            ));
        }
    }
}
