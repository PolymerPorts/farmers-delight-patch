package eu.pb4.farmersdelightpatch.mixin.mod;

import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;

@Mixin(targets = "net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData$BlockEntityInfo")
public class BlockEntityDataMixin {
    @ModifyArg(method = "create", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ClientboundLevelChunkPacketData$BlockEntityInfo;<init>(IILnet/minecraft/world/level/block/entity/BlockEntityType;Lnet/minecraft/nbt/CompoundTag;)V"))
    private static BlockEntityType<?> swapBlockEntityType(BlockEntityType<?> type) {
        if (type == ModBlockEntityTypes.CANVAS_SIGN.get()) {
            return BlockEntityType.SIGN;
        } else if (type == ModBlockEntityTypes.HANGING_CANVAS_SIGN.get()) {
            return BlockEntityType.HANGING_SIGN;
        }

        return type;
    }
}
