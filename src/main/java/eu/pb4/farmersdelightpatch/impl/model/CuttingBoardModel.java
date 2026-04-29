package eu.pb4.farmersdelightpatch.impl.model;

import eu.pb4.factorytools.api.block.model.generic.BlockStateModel;
import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;

public class CuttingBoardModel extends BlockStateModel {
    private final ItemDisplayElement[] item = new ItemDisplayElement[5];

    private boolean carved = false;
    public CuttingBoardModel(BlockState state, BlockPos pos) {
        super(state, pos, 3);
        var random = RandomSource.create(187);

        for (int i = 0; i < 5; i++) {
            var item = this.item[i] = ItemDisplayElementUtil.createSimple();
            item.setScale(new Vector3f(0.6f));
            float xOffset = (random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
            float zOffset = (random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;

            item.setOffset(new Vec3(xOffset, -6.5f / 16 + 0.036 * (i + 1), zOffset));
            item.setPitch(-90);
        }

        this.applyUpdates(state, pos);
        for (var item : this.item) {
            this.addElement(item);
        }
    }

    @Override
    protected void applyUpdates(BlockState blockState, BlockPos pos) {
        for (var item : this.item) {
            item.setYaw(blockState.getValue(CuttingBoardBlock.FACING).toYRot());
        }
        super.applyUpdates(blockState, pos);
    }

    public void setItem(ItemStack stack, boolean carved) {
        if (ItemStack.matches(stack, this.item[0].getItem()) && this.carved == carved) {
            return;
        }
        if (carved) {
            this.item[0].setPitch(0);
            this.item[0].setTranslation(new Vector3f(0, 0.23f * 0.6f, 0));
            float poseAngle;
            if (!stack.is(ItemTags.PICKAXES) && !(stack.getItem() instanceof HoeItem)) {
                if (stack.getItem() instanceof TridentItem) {
                    poseAngle = 135.0F;
                } else {
                    poseAngle = 180.0F;
                }
            } else {
                poseAngle = 225.0F;
            }

            this.item[0].setLeftRotation(new Quaternionf().rotateZ(Mth.DEG_TO_RAD * poseAngle).rotateY(Mth.PI));
        } else {
            this.item[0].setPitch(-90);
            this.item[0].setTranslation(new Vector3f());
            this.item[0].setLeftRotation(new Quaternionf());
        }

        for (int i = 1; i < this.item.length; i++) {
            this.item[i].setPitch(this.item[0].getPitch());
            this.item[i].setTranslation(this.item[0].getTranslation());
            this.item[i].setLeftRotation(this.item[0].getLeftRotation());
        }

        this.carved = carved;

        int i = 0;
        stack = stack.copy();
        for (; i < getModelCount(stack); i++) {
            this.item[i].setItem(stack);
            this.item[i].tick();
        }
        for (; i < 5; i++) {
            this.item[i].setItem(ItemStack.EMPTY);
            this.item[i].tick();
        }
    }

    public static int getModelCount(ItemStack stack) {
        if (stack.getCount() > 48) {
            return 5;
        } else if (stack.getCount() > 32) {
            return 4;
        } else if (stack.getCount() > 16) {
            return 3;
        } else if (stack.getCount() > 1) {
            return 2;
        }
        return 1;
    }
}
