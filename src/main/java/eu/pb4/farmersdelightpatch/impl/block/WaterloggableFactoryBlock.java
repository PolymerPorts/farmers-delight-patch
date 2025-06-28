package eu.pb4.farmersdelightpatch.impl.block;

import eu.pb4.factorytools.api.block.FactoryBlock;
import eu.pb4.factorytools.api.virtualentity.BlockModel;
import eu.pb4.farmersdelightpatch.impl.model.generic.BSMMParticleBlock;
import eu.pb4.farmersdelightpatch.impl.model.generic.BlockStateModel;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import net.minecraft.block.BarrierBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.function.Function;

public record WaterloggableFactoryBlock(BlockState clientState, BlockState waterloggedClientState, boolean tick, Function<BlockState, BlockModel> modelFunction) implements FactoryBlock, PolymerTexturedBlock, BSMMParticleBlock {
    public static final WaterloggableFactoryBlock BARRIER = new WaterloggableFactoryBlock(Blocks.BARRIER.getDefaultState(),
            Blocks.BARRIER.getDefaultState().with(BarrierBlock.WATERLOGGED, true), false,
            BlockStateModel::new);
    public static final WaterloggableFactoryBlock TOP_TRAPDOOR = new WaterloggableFactoryBlock(PolymerBlockResourceUtils.requestEmpty(BlockModelType.TOP_TRAPDOOR),
            PolymerBlockResourceUtils.requestEmpty(BlockModelType.TOP_TRAPDOOR_WATERLOGGED), false,
            BlockStateModel::new);

    public static final WaterloggableFactoryBlock BOTTOM_TRAPDOOR = new WaterloggableFactoryBlock(PolymerBlockResourceUtils.requestEmpty(BlockModelType.BOTTOM_TRAPDOOR),
            PolymerBlockResourceUtils.requestEmpty(BlockModelType.BOTTOM_TRAPDOOR_WATERLOGGED), false,
            BlockStateModel::new);

    public static final WaterloggableFactoryBlock PLANT = new WaterloggableFactoryBlock(
            PolymerBlockResourceUtils.requestEmpty(BlockModelType.PLANT_BLOCK),
            PolymerBlockResourceUtils.requestEmpty(BlockModelType.KELP_BLOCK),
            false, BlockStateModel::new);


    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return state.getFluidState().isIn(FluidTags.WATER) ? waterloggedClientState : clientState;
    }

    @Override
    public @Nullable ElementHolder createElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
        return this.modelFunction.apply(initialBlockState);
    }

    @Override
    public boolean tickElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
        return this.tick;
    }

    public WaterloggableFactoryBlock withModel(Function<BlockState, BlockModel> modelFunction) {
        return new WaterloggableFactoryBlock(this.clientState, this.waterloggedClientState, this.tick, modelFunction);
    }

    public WaterloggableFactoryBlock withTick(boolean tick) {
        return new WaterloggableFactoryBlock(this.clientState, this.waterloggedClientState, tick, modelFunction);
    }
}

