package eu.pb4.farmersdelightpatch.mixin.mod;

import eu.pb4.factorytools.api.block.BlockEntityExtraListener;
import eu.pb4.farmersdelightpatch.impl.model.CuttingBoardModel;
import eu.pb4.polymer.virtualentity.api.attachment.BlockAwareAttachment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;
import vectorwing.farmersdelight.refabricated.inventory.ItemStackHandler;

@Mixin(CuttingBoardBlockEntity.class)
public abstract class CuttingBoardBlockEntityMixin extends SyncedBlockEntity implements BlockEntityExtraListener {
    @Shadow @Final private ItemStackHandler inventory;

    @Shadow public abstract boolean isItemCarvingBoard();

    @Unique
    @Nullable
    private CuttingBoardModel model = null;
    public CuttingBoardBlockEntityMixin(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    @Override
    protected void inventoryChanged() {
        super.inventoryChanged();

        if (this.model != null) {
            this.model.setItem(this.inventory.getStackInSlot(0), false);
        }
    }

    @Inject(method = "carveToolOnBoard", at = @At(value = "FIELD", target = "Lvectorwing/farmersdelight/common/block/entity/CuttingBoardBlockEntity;isItemCarvingBoard:Z"))
    private void setTransformation(ItemStack tool, CallbackInfoReturnable<Boolean> cir) {
        if (this.model != null) {
            this.model.setItem(this.inventory.getStackInSlot(0), true);
        }
    }

    @Override
    public void onListenerUpdate(LevelChunk worldChunk) {
        var holder = BlockAwareAttachment.get(worldChunk, this.worldPosition);
        if (holder != null && holder.holder() instanceof CuttingBoardModel model) {
            this.model = model;
            this.model.setItem(this.inventory.getStackInSlot(0), this.isItemCarvingBoard());
        }
    }
}
