package eu.pb4.farmersdelightpatch.mixin;


import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.Optional;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "restituteMovementAfterCollisions", at = @At("TAIL"))
    private void handleServerSideBounce(BlockState effectState, boolean xCollision, boolean zCollision, Vec3 movement, CallbackInfo ci) {
        if (!effectState.is(ModBlocks.SAFETY_NET.get())) {
            return;
        }

        if (((Object) this) instanceof ServerPlayer player) {
            player.connection.send(new ClientboundExplodePacket(Vec3.ZERO.add(0, -99999, 0),
                    0f, 0,
                    Optional.of(new Vec3(0, (player.connection.latency() > 50 || player.onGround() ? 0 : -player.getKnownMovement().y) + player.getDeltaMovement().y, 0)),
                    ParticleTypes.BUBBLE_POP,
                    BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.EMPTY),
                    WeightedList.of()
            ));
        }
    }
}
