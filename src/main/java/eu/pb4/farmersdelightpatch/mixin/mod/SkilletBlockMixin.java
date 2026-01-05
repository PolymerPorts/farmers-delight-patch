package eu.pb4.farmersdelightpatch.mixin.mod;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import vectorwing.farmersdelight.common.block.CookingPotBlock;
import vectorwing.farmersdelight.common.block.SkilletBlock;

@Mixin(SkilletBlock.class)
public abstract class SkilletBlockMixin extends BaseEntityBlock {
    protected SkilletBlockMixin(Properties settings) {
        super(settings);
    }

    /**
     * @author Patbox
     * @reason Nuh uh
     */
    @Overwrite
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return super.getCollisionShape(state, level, pos, context);
    }
}
