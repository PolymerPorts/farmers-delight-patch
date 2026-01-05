package eu.pb4.farmersdelightpatch.impl.block;

import eu.pb4.factorytools.api.block.FactoryBlock;
import eu.pb4.factorytools.api.block.model.generic.BSMMParticleBlock;
import eu.pb4.factorytools.api.block.model.generic.BlockStateModel;
import eu.pb4.factorytools.api.virtualentity.BlockModel;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public record BaseFactoryBlock(BlockState clientState, boolean tick, BiFunction<BlockState, BlockPos, BlockModel> modelFunction) implements FactoryBlock, PolymerTexturedBlock, BSMMParticleBlock {
    public static final BaseFactoryBlock BARRIER = new BaseFactoryBlock(Blocks.BARRIER. defaultBlockState(), false, BlockStateModel::longRange);
    public static final BaseFactoryBlock TOP_TRAPDOOR = new BaseFactoryBlock(PolymerBlockResourceUtils.requestEmpty(BlockModelType.TOP_TRAPDOOR), false, BlockStateModel::longRange);
    public static final BaseFactoryBlock BOTTOM_TRAPDOOR = new BaseFactoryBlock(PolymerBlockResourceUtils.requestEmpty(BlockModelType.BOTTOM_TRAPDOOR), false, BlockStateModel::longRange);
    public static final BaseFactoryBlock VINE = new BaseFactoryBlock(PolymerBlockResourceUtils.requestEmpty(BlockModelType.VINES_BLOCK), false, BlockStateModel::midRange);
    public static final BaseFactoryBlock SAPLING = new BaseFactoryBlock(PolymerBlockResourceUtils.requestEmpty(BlockModelType.PLANT_BLOCK),false, BlockStateModel::midRange);
    public static final BaseFactoryBlock KELP = new BaseFactoryBlock(PolymerBlockResourceUtils.requestEmpty(BlockModelType.KELP_BLOCK),false, BlockStateModel::midRange);
    public static final BaseFactoryBlock TRIPWIRE = new BaseFactoryBlock(PolymerBlockResourceUtils.requestEmpty(BlockModelType.TRIPWIRE_BLOCK),false, BlockStateModel::midRange);
    public static final BaseFactoryBlock TRIPWIRE_FLAT = new BaseFactoryBlock(PolymerBlockResourceUtils.requestEmpty(BlockModelType.TRIPWIRE_BLOCK_FLAT),false, BlockStateModel::midRange);

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return clientState;
    }

    @Override
    public @Nullable ElementHolder createElementHolder(ServerLevel world, BlockPos pos, BlockState initialBlockState) {
        return this.modelFunction.apply(initialBlockState, pos);
    }

    @Override
    public boolean tickElementHolder(ServerLevel world, BlockPos pos, BlockState initialBlockState) {
        return this.tick;
    }

    public BaseFactoryBlock withModel(BiFunction<BlockState, BlockPos, BlockModel> modelFunction) {
        return new BaseFactoryBlock(this.clientState, this.tick, modelFunction);
    }

    public BaseFactoryBlock withTick(boolean tick) {
        return new BaseFactoryBlock(this.clientState, tick, this.modelFunction);
    }
}
