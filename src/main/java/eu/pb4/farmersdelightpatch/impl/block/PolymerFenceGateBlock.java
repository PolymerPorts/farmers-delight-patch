package eu.pb4.farmersdelightpatch.impl.block;

import eu.pb4.factorytools.api.block.FactoryBlock;
import eu.pb4.factorytools.api.block.model.generic.BSMMParticleBlock;
import eu.pb4.factorytools.api.block.model.generic.BlockStateModel;
import eu.pb4.factorytools.api.virtualentity.BlockModel;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Util;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.function.BiFunction;

public record PolymerFenceGateBlock(EnumMap<Direction, BlockState> closed, EnumMap<Direction, BlockState> open, boolean tick, BiFunction<BlockState, BlockPos, BlockModel> modelFunction) implements FactoryBlock, PolymerTexturedBlock, BSMMParticleBlock {
    public static final PolymerFenceGateBlock INSTANCE = new PolymerFenceGateBlock(Util.make(new EnumMap<>(Direction.class), map -> {
        for (var dir : Direction.Plane.HORIZONTAL) {
            map.put(dir, PolymerBlockResourceUtils.requestEmpty(BlockModelType.getGate(dir.getClockWise().getAxis(), false, false)));
        }
    }), Util.make(new EnumMap<>(Direction.class), map -> {
        for (var dir : Direction.Plane.HORIZONTAL) {
            map.put(dir, PolymerBlockResourceUtils.requestEmpty(BlockModelType.getGate(dir.getClockWise().getAxis(), false, true)));
        }
    }), false, BlockStateModel::longRange);

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return (state.getValue(FenceGateBlock.OPEN) ? open : closed).get(state.getValue(FenceGateBlock.FACING));
    }

    @Override
    public @Nullable ElementHolder createElementHolder(ServerLevel world, BlockPos pos, BlockState initialBlockState) {
        return this.modelFunction.apply(initialBlockState, pos);
    }

    @Override
    public boolean tickElementHolder(ServerLevel world, BlockPos pos, BlockState initialBlockState) {
        return this.tick;
    }

    public PolymerFenceGateBlock withModel(BiFunction<BlockState, BlockPos, BlockModel> modelFunction) {
        return new PolymerFenceGateBlock(this.closed, this.open, this.tick, modelFunction);
    }

    public PolymerFenceGateBlock withTick(boolean tick) {
        return new PolymerFenceGateBlock(this.closed, this.open, tick, this.modelFunction);
    }

    @Override
    public boolean isIgnoringBlockInteractionPlaySoundExceptedEntity(BlockState state, ServerPlayer player, InteractionHand hand, ItemStack stack, ServerLevel world, BlockHitResult blockHitResult) {
        return true;
    }
}
