package eu.pb4.farmersdelightpatch.impl.block;

import eu.pb4.factorytools.api.block.FactoryBlock;
import eu.pb4.factorytools.api.virtualentity.BlockModel;
import eu.pb4.farmersdelightpatch.impl.model.generic.BSMMParticleBlock;
import eu.pb4.farmersdelightpatch.impl.model.generic.BlockStateModel;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.function.Function;

public record BaseFactoryBlock(BlockState clientState, boolean tick, Function<BlockState, BlockModel> modelFunction) implements FactoryBlock, PolymerTexturedBlock, BSMMParticleBlock {
    public static final BaseFactoryBlock BARRIER = new BaseFactoryBlock(Blocks.BARRIER. getDefaultState(), false, BlockStateModel::new);
    public static final BaseFactoryBlock TOP_TRAPDOOR = new BaseFactoryBlock(PolymerBlockResourceUtils.requestEmpty(BlockModelType.TOP_TRAPDOOR), false, BlockStateModel::new);
    public static final BaseFactoryBlock VINE = new BaseFactoryBlock(PolymerBlockResourceUtils.requestEmpty(BlockModelType.VINES_BLOCK), false, BlockStateModel::new);
    public static final BaseFactoryBlock SAPLING = new BaseFactoryBlock(PolymerBlockResourceUtils.requestEmpty(BlockModelType.PLANT_BLOCK),false, BlockStateModel::new);
    public static final BaseFactoryBlock KELP = new BaseFactoryBlock(PolymerBlockResourceUtils.requestEmpty(BlockModelType.KELP_BLOCK),false, BlockStateModel::new);
    public static final BaseFactoryBlock TRIPWIRE = new BaseFactoryBlock(PolymerBlockResourceUtils.requestEmpty(BlockModelType.TRIPWIRE_BLOCK),false, BlockStateModel::new);
    public static final BaseFactoryBlock TRIPWIRE_FLAT = new BaseFactoryBlock(PolymerBlockResourceUtils.requestEmpty(BlockModelType.TRIPWIRE_BLOCK_FLAT),false, BlockStateModel::new);

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return clientState;
    }

    @Override
    public @Nullable ElementHolder createElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
        return this.modelFunction.apply(initialBlockState);
    }

    @Override
    public boolean tickElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
        return this.tick;
    }

    public BaseFactoryBlock withModel(Function<BlockState, BlockModel> modelFunction) {
        return new BaseFactoryBlock(this.clientState, this.tick, modelFunction);
    }

    public BaseFactoryBlock withTick(boolean tick) {
        return new BaseFactoryBlock(this.clientState, tick, this.modelFunction);
    }
}
