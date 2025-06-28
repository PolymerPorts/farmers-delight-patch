package eu.pb4.farmersdelightpatch.impl.model;

import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.farmersdelightpatch.impl.model.generic.BlockStateModel;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import net.minecraft.block.BlockState;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;

public class CuttingBoardModel extends BlockStateModel {
    private final ItemDisplayElement item = ItemDisplayElementUtil.createSimple();
    private boolean carved = false;
    public CuttingBoardModel(BlockState state) {
        super(state);
        this.item.setScale(new Vector3f(0.6f));
        this.item.setOffset(new Vec3d(0, -6.5f / 16, 0));
        this.item.setPitch(-90);
        this.applyUpdates(state);
        this.addElement(item);
    }

    @Override
    protected void applyUpdates(BlockState blockState) {
        this.item.setYaw(blockState.get(CuttingBoardBlock.FACING).getPositiveHorizontalDegrees());
        super.applyUpdates(blockState);
    }


    public void setItem(ItemStack stack, boolean carved) {
        if (ItemStack.areItemsAndComponentsEqual(stack, this.item.getItem()) && this.carved == carved) {
            return;
        }
        if (carved) {
            this.item.setPitch(0);
            this.item.setTranslation(new Vector3f(0, 0.23f * 0.6f, 0));
            float poseAngle;
            if (!stack.isIn(ItemTags.PICKAXES) && !(stack.getItem() instanceof HoeItem)) {
                if (stack.getItem() instanceof TridentItem) {
                    poseAngle = 135.0F;
                } else {
                    poseAngle = 180.0F;
                }
            } else {
                poseAngle = 225.0F;
            }

            this.item.setLeftRotation(new Quaternionf().rotateZ(MathHelper.RADIANS_PER_DEGREE * poseAngle).rotateY(MathHelper.PI));
        } else {
            this.item.setPitch(-90);
            this.item.setTranslation(new Vector3f());
            this.item.setLeftRotation(new Quaternionf());
        }
        this.carved = carved;

        this.item.setItem(stack.copyWithCount(1));
        this.item.tick();
    }
}
