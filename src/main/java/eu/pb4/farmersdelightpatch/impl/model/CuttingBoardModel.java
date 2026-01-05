package eu.pb4.farmersdelightpatch.impl.model;

import eu.pb4.factorytools.api.block.model.generic.BlockStateModel;
import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;

public class CuttingBoardModel extends BlockStateModel {
    private final ItemDisplayElement item = ItemDisplayElementUtil.createSimple();
    private boolean carved = false;
    public CuttingBoardModel(BlockState state, BlockPos pos) {
        super(state, pos, 3);
        this.item.setScale(new Vector3f(0.6f));
        this.item.setOffset(new Vec3(0, -6.5f / 16, 0));
        this.item.setPitch(-90);
        this.applyUpdates(state, pos);
        this.addElement(item);
    }

    @Override
    protected void applyUpdates(BlockState blockState, BlockPos pos) {
        this.item.setYaw(blockState.getValue(CuttingBoardBlock.FACING).toYRot());
        super.applyUpdates(blockState, pos);
    }


    public void setItem(ItemStack stack, boolean carved) {
        if (ItemStack.isSameItemSameComponents(stack, this.item.getItem()) && this.carved == carved) {
            return;
        }
        if (carved) {
            this.item.setPitch(0);
            this.item.setTranslation(new Vector3f(0, 0.23f * 0.6f, 0));
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

            this.item.setLeftRotation(new Quaternionf().rotateZ(Mth.DEG_TO_RAD * poseAngle).rotateY(Mth.PI));
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
