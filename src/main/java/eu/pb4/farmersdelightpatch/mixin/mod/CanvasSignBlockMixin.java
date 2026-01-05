package eu.pb4.farmersdelightpatch.mixin.mod;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.common.block.CeilingHangingCanvasSignBlock;
import vectorwing.farmersdelight.common.block.StandingCanvasSignBlock;
import vectorwing.farmersdelight.common.block.WallCanvasSignBlock;
import vectorwing.farmersdelight.common.block.WallHangingCanvasSignBlock;
import vectorwing.farmersdelight.common.block.state.CanvasSign;

@Mixin({StandingCanvasSignBlock.class, CeilingHangingCanvasSignBlock.class, WallCanvasSignBlock.class, WallHangingCanvasSignBlock.class})
public class CanvasSignBlockMixin {
    @Inject(method = "setPlacedBy", at = @At("TAIL"))
    private void resyncBlockEntity(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack, CallbackInfo ci) {
        if (placer instanceof ServerPlayer player && level.getBlockEntity(pos) instanceof BlockEntity be && be instanceof CanvasSign canvasSign && canvasSign.isDarkBackground()) {
            player.connection.send(be.getUpdatePacket());
        }
    }
}
