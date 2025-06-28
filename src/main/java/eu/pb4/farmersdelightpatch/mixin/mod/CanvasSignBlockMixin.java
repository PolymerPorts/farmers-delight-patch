package eu.pb4.farmersdelightpatch.mixin.mod;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
    @Inject(method = "onPlaced", at = @At("TAIL"))
    private void resyncBlockEntity(World level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack, CallbackInfo ci) {
        if (placer instanceof ServerPlayerEntity player && level.getBlockEntity(pos) instanceof BlockEntity be && be instanceof CanvasSign canvasSign && canvasSign.isDarkBackground()) {
            player.networkHandler.sendPacket(be.toUpdatePacket());
        }
    }
}
