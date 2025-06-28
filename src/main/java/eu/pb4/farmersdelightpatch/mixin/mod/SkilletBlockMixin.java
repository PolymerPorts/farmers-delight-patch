package eu.pb4.farmersdelightpatch.mixin.mod;

import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import vectorwing.farmersdelight.common.block.CookingPotBlock;
import vectorwing.farmersdelight.common.block.SkilletBlock;

@Mixin(SkilletBlock.class)
public abstract class SkilletBlockMixin extends BlockWithEntity {
    protected SkilletBlockMixin(Settings settings) {
        super(settings);
    }

    /**
     * @author Patbox
     * @reason Nuh uh
     */
    @Overwrite
    public VoxelShape getCollisionShape(BlockState state, BlockView level, BlockPos pos, ShapeContext context) {
        return super.getCollisionShape(state, level, pos, context);
    }
}
