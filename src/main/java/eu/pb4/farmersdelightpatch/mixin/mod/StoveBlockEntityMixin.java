package eu.pb4.farmersdelightpatch.mixin.mod;

import eu.pb4.factorytools.api.block.BlockEntityExtraListener;
import eu.pb4.farmersdelightpatch.impl.model.CuttingBoardModel;
import eu.pb4.farmersdelightpatch.impl.model.StoveModel;
import eu.pb4.polymer.virtualentity.api.attachment.BlockAwareAttachment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;
import vectorwing.farmersdelight.common.block.entity.StoveBlockEntity;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;
import vectorwing.farmersdelight.refabricated.inventory.ItemStackHandler;

@Mixin(StoveBlockEntity.class)
public abstract class StoveBlockEntityMixin extends SyncedBlockEntity implements BlockEntityExtraListener {
    @Shadow @Final private ItemStackHandler inventory;

    @Shadow public static void animationTick(World level, BlockPos pos, BlockState state, StoveBlockEntity stove) {};

    @Unique
    @Nullable
    private StoveModel model = null;
    public StoveBlockEntityMixin(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    @Override
    protected void inventoryChanged() {
        super.inventoryChanged();

        if (this.model != null) {
            for (int i = 0; i < 6; i++) {
                this.model.setItem(i, this.inventory.getStackInSlot(i));
            }
        }
    }

    @Override
    public void onListenerUpdate(WorldChunk worldChunk) {
        var holder = BlockAwareAttachment.get(worldChunk, this.pos);
        if (holder != null && holder.holder() instanceof StoveModel model) {
            this.model = model;
        }
    }

    @Inject(method = "cookingTick", at = @At("HEAD"))
    private static void reanimateThatTick(ServerWorld level, BlockPos pos, BlockState state, StoveBlockEntity stove, CallbackInfo ci) {
        animationTick(level, pos, state, stove);
    }

    @Redirect(method = "animationTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticleClient(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"))
    private static void fixParticles(World instance, ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        ((ServerWorld) instance).spawnParticles(parameters, x, y, z, 0, velocityX, velocityY, velocityZ, 1);
    }
}
