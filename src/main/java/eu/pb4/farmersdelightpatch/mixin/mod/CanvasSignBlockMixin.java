package eu.pb4.farmersdelightpatch.mixin.mod;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Intrinsic;
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
public abstract class CanvasSignBlockMixin extends Block {
    public CanvasSignBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity by, ItemStack itemStack) {
        super.setPlacedBy(level, pos, state, by, itemStack);

        if (by instanceof ServerPlayer player && level.getBlockEntity(pos) instanceof BlockEntity be && be instanceof CanvasSign canvasSign && canvasSign.isDarkBackground()) {
            player.connection.send(be.getUpdatePacket());
        }
    }
}
