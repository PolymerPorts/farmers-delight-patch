package eu.pb4.farmersdelightpatch.impl.model;

import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.farmersdelightpatch.impl.model.generic.BlockStateModel;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import net.minecraft.block.BlockState;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;

public class SkilletModel extends BlockStateModel {
    private final ItemDisplayElement[] item = new ItemDisplayElement[5];
    public SkilletModel(BlockState state) {
        super(state);
        var random = Random.create(187);
        for (int i = 0; i < 5; i++) {
            var item = this.item[i] = ItemDisplayElementUtil.createSimple();
            item.setScale(new Vector3f(0.5f));
            float xOffset = (random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
            float zOffset = (random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;

            item.setOffset(new Vec3d(xOffset, -6.5f / 16 + 0.03 * (i + 1), zOffset));
            item.setPitch(-90);
        }
        this.applyUpdates(state);
        for (var item : this.item) {
            this.addElement(item);
        }
    }

    @Override
    protected void applyUpdates(BlockState blockState) {
        for (var item : this.item) {
            item.setYaw(blockState.get(CuttingBoardBlock.FACING).getPositiveHorizontalDegrees());
        }
        super.applyUpdates(blockState);
    }


    public void setItem(ItemStack stack) {
        if (ItemStack.areEqual(stack, this.item[0].getItem())) {
            return;
        }

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

    protected int getModelCount(ItemStack stack) {
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
