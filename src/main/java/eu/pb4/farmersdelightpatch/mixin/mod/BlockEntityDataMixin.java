package eu.pb4.farmersdelightpatch.mixin.mod;

import net.minecraft.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;

@Mixin(targets = "net.minecraft.network.packet.s2c.play.ChunkData$BlockEntityData")
public class BlockEntityDataMixin {
    @ModifyArg(method = "of", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/s2c/play/ChunkData$BlockEntityData;<init>(IILnet/minecraft/block/entity/BlockEntityType;Lnet/minecraft/nbt/NbtCompound;)V"))
    private static BlockEntityType<?> swapBlockEntityType(BlockEntityType<?> type) {
        if (type == ModBlockEntityTypes.CANVAS_SIGN.get()) {
            return BlockEntityType.SIGN;
        } else if (type == ModBlockEntityTypes.HANGING_CANVAS_SIGN.get()) {
            return BlockEntityType.HANGING_SIGN;
        }

        return type;
    }
}
