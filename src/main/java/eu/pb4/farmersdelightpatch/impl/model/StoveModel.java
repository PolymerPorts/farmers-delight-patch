package eu.pb4.farmersdelightpatch.impl.model;

import eu.pb4.factorytools.api.block.model.generic.BlockStateModel;
import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.registry.ModSounds;

public class StoveModel extends BlockStateModel {
    final float X_OFFSET = 0.3F;
    final float Y_OFFSET = 0.2F;
    final Vec2[] OFFSETS = {
            new Vec2(X_OFFSET, Y_OFFSET),
            new Vec2(0.0F, Y_OFFSET),
            new Vec2(-X_OFFSET, Y_OFFSET),
            new Vec2(X_OFFSET, -Y_OFFSET),
            new Vec2(0.0F, -Y_OFFSET),
            new Vec2(-X_OFFSET, -Y_OFFSET),
    };

    private final ItemDisplayElement[] item= new ItemDisplayElement[6];
    private final RandomSource rand = RandomSource.create();
    public StoveModel(BlockState state, BlockPos pos) {
        super(state, pos, 3);

        for (int i = 0; i < 6; i++) {
            var item = this.item[i] = ItemDisplayElementUtil.createSimple();
            item.setScale(new Vector3f(0.6f));
            item.setOffset(new Vec3(0, 8.5f / 16, 0));
            var offset = OFFSETS[i];
            item.setTranslation(new Vector3f(-offset.x, offset.y, 0));
            item.setScale(new Vector3f(0.375F));
            item.setPitch(-90);
        }

        this.applyUpdates(state, pos);
        for (int i = 0; i < 6; i++) {
            this.addElement(this.item[i]);
        }
    }

    @Override
    protected void onTick() {
        super.onTick();
        if (this.blockState().getValue(CampfireBlock.LIT) && rand.nextFloat() < 0.1f) {
            var pos = this.blockPos();
            double x = (double) pos.getX() + 0.5D;
            double y = pos.getY();
            double z = (double) pos.getZ() + 0.5D;
            if (rand.nextInt(10) == 0) {
                this.getAttachment().getWorld().playSeededSound(null, x, y, z, ModSounds.BLOCK_STOVE_CRACKLE.get(), SoundSource.BLOCKS, 1.0F, 1.0F, rand.nextLong());
            }

            Direction direction = this.blockState().getValue(HorizontalDirectionalBlock.FACING);
            Direction.Axis direction$axis = direction.getAxis();
            double horizontalOffset = rand.nextDouble() * 0.6D - 0.3D;
            double xOffset = direction$axis == Direction.Axis.X ? (double) direction.getStepX() * 0.52D : horizontalOffset;
            double yOffset = rand.nextDouble() * 6.0D / 16.0D;
            double zOffset = direction$axis == Direction.Axis.Z ? (double) direction.getStepZ() * 0.52D : horizontalOffset;
            this.getAttachment().getWorld().sendParticles(ParticleTypes.SMOKE, x + xOffset, y + yOffset, z + zOffset, 0, 0.0D, 0.0D, 0.0D,0);
            this.getAttachment().getWorld().sendParticles(ParticleTypes.FLAME, x + xOffset, y + yOffset, z + zOffset, 0, 0.0D, 0.0D, 0.0D, 0);
        }
    }


    @Override
    protected void applyUpdates(BlockState blockState, BlockPos pos) {
        super.applyUpdates(blockState, pos);
        for (var item : this.item) {
            item.setYaw(blockState.getValue(CuttingBoardBlock.FACING).toYRot());
        }
    }

    public void setItem(int i, ItemStack stack) {
        if (ItemStack.isSameItemSameComponents(stack, this.item[i].getItem())) {
            return;
        }
        this.item[i].setItem(stack.copyWithCount(1));
        this.item[i].tick();
    }
}
