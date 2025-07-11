package eu.pb4.farmersdelightpatch.mixin.mod;

import eu.pb4.polymer.core.api.block.PolymerBlockUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import vectorwing.farmersdelight.common.block.entity.CanvasSignBlockEntity;

@Mixin(CanvasSignBlockEntity.class)
public class CanvasSignBlockEntityMixin extends BlockEntity {
    public CanvasSignBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return PolymerBlockUtils.createBlockEntityPacket(this.pos, BlockEntityType.SIGN, this.toInitialChunkDataNbt(this.world.getRegistryManager()));
    }
}
