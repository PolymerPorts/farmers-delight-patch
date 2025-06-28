package eu.pb4.farmersdelightpatch.impl.block;

import eu.pb4.factorytools.api.block.FactoryBlock;
import eu.pb4.factorytools.api.virtualentity.BlockModel;
import eu.pb4.farmersdelightpatch.impl.model.SignModel;
import eu.pb4.farmersdelightpatch.impl.model.generic.BSMMParticleBlock;
import eu.pb4.farmersdelightpatch.impl.model.generic.BlockStateModel;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.function.Function;

public record StateCopyFactoryBlock(Block clientBlock, Function<BlockState, BlockModel> modelFunction) implements FactoryBlock, PolymerTexturedBlock, BSMMParticleBlock {
    public static final StateCopyFactoryBlock SIGN = new StateCopyFactoryBlock(Blocks.BIRCH_SIGN, SignModel::new);
    public static final StateCopyFactoryBlock WALL_SIGN = new StateCopyFactoryBlock(Blocks.BIRCH_WALL_SIGN, SignModel::new);
    public static final StateCopyFactoryBlock HANGING_SIGN = new StateCopyFactoryBlock(Blocks.BIRCH_HANGING_SIGN, SignModel::new);
    public static final StateCopyFactoryBlock HANGING_WALL_SIGN = new StateCopyFactoryBlock(Blocks.BIRCH_WALL_HANGING_SIGN, SignModel::new);

    public static final StateCopyFactoryBlock SIGN_DARK = new StateCopyFactoryBlock(Blocks.DARK_OAK_SIGN, SignModel::new);
    public static final StateCopyFactoryBlock WALL_SIGN_DARK = new StateCopyFactoryBlock(Blocks.DARK_OAK_WALL_SIGN, SignModel::new);
    public static final StateCopyFactoryBlock HANGING_SIGN_DARK = new StateCopyFactoryBlock(Blocks.DARK_OAK_HANGING_SIGN, SignModel::new);
    public static final StateCopyFactoryBlock HANGING_WALL_SIGN_DARK = new StateCopyFactoryBlock(Blocks.DARK_OAK_WALL_HANGING_SIGN, SignModel::new);

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return clientBlock.getStateWithProperties(state);
    }

    @Override
    public @Nullable ElementHolder createElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
        return this.modelFunction.apply(initialBlockState);
    }
}
