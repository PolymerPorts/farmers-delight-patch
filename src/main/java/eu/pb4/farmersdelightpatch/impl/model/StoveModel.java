package eu.pb4.farmersdelightpatch.impl.model;

import eu.pb4.factorytools.api.block.model.generic.BlockStateModel;
import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.joml.Vector3f;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.registry.ModSounds;

public class StoveModel extends BlockStateModel {
    final float X_OFFSET = 0.3F;
    final float Y_OFFSET = 0.2F;
    final Vec2f[] OFFSETS = {
            new Vec2f(X_OFFSET, Y_OFFSET),
            new Vec2f(0.0F, Y_OFFSET),
            new Vec2f(-X_OFFSET, Y_OFFSET),
            new Vec2f(X_OFFSET, -Y_OFFSET),
            new Vec2f(0.0F, -Y_OFFSET),
            new Vec2f(-X_OFFSET, -Y_OFFSET),
    };

    private final ItemDisplayElement[] item= new ItemDisplayElement[6];
    private final Random rand = Random.create();
    public StoveModel(BlockState state, BlockPos pos) {
        super(state, pos, 3);

        for (int i = 0; i < 6; i++) {
            var item = this.item[i] = ItemDisplayElementUtil.createSimple();
            item.setScale(new Vector3f(0.6f));
            item.setOffset(new Vec3d(0, 8.5f / 16, 0));
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
        if (this.blockState().get(CampfireBlock.LIT) && rand.nextFloat() < 0.1f) {
            var pos = this.blockPos();
            double x = (double) pos.getX() + 0.5D;
            double y = pos.getY();
            double z = (double) pos.getZ() + 0.5D;
            if (rand.nextInt(10) == 0) {
                this.getAttachment().getWorld().playSound(null, x, y, z, ModSounds.BLOCK_STOVE_CRACKLE.get(), SoundCategory.BLOCKS, 1.0F, 1.0F, rand.nextLong());
            }

            Direction direction = this.blockState().get(HorizontalFacingBlock.FACING);
            Direction.Axis direction$axis = direction.getAxis();
            double horizontalOffset = rand.nextDouble() * 0.6D - 0.3D;
            double xOffset = direction$axis == Direction.Axis.X ? (double) direction.getOffsetX() * 0.52D : horizontalOffset;
            double yOffset = rand.nextDouble() * 6.0D / 16.0D;
            double zOffset = direction$axis == Direction.Axis.Z ? (double) direction.getOffsetZ() * 0.52D : horizontalOffset;
            this.getAttachment().getWorld().spawnParticles(ParticleTypes.SMOKE, x + xOffset, y + yOffset, z + zOffset, 0, 0.0D, 0.0D, 0.0D,0);
            this.getAttachment().getWorld().spawnParticles(ParticleTypes.FLAME, x + xOffset, y + yOffset, z + zOffset, 0, 0.0D, 0.0D, 0.0D, 0);
        }
    }


    @Override
    protected void applyUpdates(BlockState blockState, BlockPos pos) {
        super.applyUpdates(blockState, pos);
        for (var item : this.item) {
            item.setYaw(blockState.get(CuttingBoardBlock.FACING).getPositiveHorizontalDegrees());
        }
    }

    public void setItem(int i, ItemStack stack) {
        if (ItemStack.areItemsAndComponentsEqual(stack, this.item[i].getItem())) {
            return;
        }
        this.item[i].setItem(stack.copyWithCount(1));
        this.item[i].tick();
    }
}
