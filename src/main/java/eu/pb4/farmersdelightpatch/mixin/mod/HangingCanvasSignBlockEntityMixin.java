package eu.pb4.farmersdelightpatch.mixin.mod;

import eu.pb4.polymer.core.api.block.PolymerBlockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import vectorwing.farmersdelight.common.block.entity.CanvasSignBlockEntity;
import vectorwing.farmersdelight.common.block.entity.HangingCanvasSignBlockEntity;

@Mixin(HangingCanvasSignBlockEntity.class)
public class HangingCanvasSignBlockEntityMixin extends BlockEntity {
    public HangingCanvasSignBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return PolymerBlockUtils.createBlockEntityPacket(this.worldPosition, BlockEntityType.HANGING_SIGN, this.getUpdateTag(this.level.registryAccess()));
    }
}
