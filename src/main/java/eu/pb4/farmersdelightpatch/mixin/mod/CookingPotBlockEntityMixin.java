package eu.pb4.farmersdelightpatch.mixin.mod;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;

@Mixin(CookingPotBlockEntity.class)
public abstract class CookingPotBlockEntityMixin {
    @Shadow public static void animationTick(Level level, BlockPos pos, BlockState state, CookingPotBlockEntity cookingPot) {};

    @Inject(method = "cookingTick", at = @At("HEAD"))
    private static void reanimateThatTick(ServerLevel level, BlockPos pos, BlockState state, CookingPotBlockEntity cookingPot, CallbackInfo ci) {
        animationTick(level, pos, state, cookingPot);
    }

    @Redirect(method = "animationTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
    private static void fixParticles(Level instance, ParticleOptions parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        ((ServerLevel) instance).sendParticles(parameters, x, y, z, 0, velocityX, velocityY, velocityZ, 1);
    }
}
