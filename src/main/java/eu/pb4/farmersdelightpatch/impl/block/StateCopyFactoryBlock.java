package eu.pb4.farmersdelightpatch.impl.block;

import eu.pb4.factorytools.api.block.FactoryBlock;
import eu.pb4.factorytools.api.block.model.generic.BSMMParticleBlock;
import eu.pb4.factorytools.api.block.model.SignModel;
import eu.pb4.factorytools.api.virtualentity.BlockModel;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public record StateCopyFactoryBlock(Block clientBlock, BiFunction<BlockState, BlockPos, BlockModel> modelFunction) implements FactoryBlock, PolymerTexturedBlock, BSMMParticleBlock {
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
        return clientBlock.withPropertiesOf(state);
    }

    @Override
    public @Nullable ElementHolder createElementHolder(ServerLevel world, BlockPos pos, BlockState initialBlockState) {
        return this.modelFunction.apply(initialBlockState, pos);
    }
}
