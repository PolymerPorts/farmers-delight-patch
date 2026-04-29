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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.BiFunction;

public record MapPolymerBlock(Map<BlockState, BlockState> map, BiFunction<BlockState, BlockPos, BlockModel> modelFunction) implements FactoryBlock, PolymerTexturedBlock, BSMMParticleBlock {
    public static MapPolymerBlock ofPaneBlock(Block block) {
        var map = new IdentityHashMap<BlockState, BlockState>();


        for (var state : block.getStateDefinition().getPossibleStates()) {
            map.put(state, PolymerBlockResourceUtils.requestEmpty(BlockModelType.getBars(state.getValue(CrossCollisionBlock.WATERLOGGED), state.getValue(CrossCollisionBlock.NORTH), state.getValue(CrossCollisionBlock.SOUTH), state.getValue(CrossCollisionBlock.WEST), state.getValue(CrossCollisionBlock.EAST))));
        }

        return new MapPolymerBlock(map, BlockStateModel::longRange);
    }
    public static MapPolymerBlock ofFenceLike(Block block) {
        var map = new IdentityHashMap<BlockState, BlockState>();


        for (var state : block.getStateDefinition().getPossibleStates()) {
            var waterlogged = state.getValue(CrossCollisionBlock.WATERLOGGED);
            var north = state.getValue(CrossCollisionBlock.NORTH);
            var south = state.getValue(CrossCollisionBlock.SOUTH);
            var west = state.getValue(CrossCollisionBlock.WEST);
            var east = state.getValue(CrossCollisionBlock.EAST);

            BlockModelType type;
            if (north && south && !west && !east && !waterlogged) {
                type = BlockModelType.GATE_EAST_WEST;
            } else if (!north && !south && west && east && !waterlogged) {
                type = BlockModelType.GATE_NORTH_SOUTH;
            } else if (!north && !south && !west && !east && !waterlogged) {
                type = BlockModelType.BARS_CENTER;
            } else {
                type = BlockModelType.getBars(waterlogged, north, south, west, east);
            }

            map.put(state, PolymerBlockResourceUtils.requestEmpty(type));
        }

        return new MapPolymerBlock(map, BlockStateModel::longRange);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState blockState, PacketContext packetContext) {
        return map.get(blockState);
    }

    @Override
    public @Nullable ElementHolder createElementHolder(ServerLevel world, BlockPos pos, BlockState initialBlockState) {
        return modelFunction.apply(initialBlockState, pos);
    }

    @Override
    public boolean isIgnoringBlockInteractionPlaySoundExceptedEntity(BlockState state, ServerPlayer player, InteractionHand hand, ItemStack stack, ServerLevel world, BlockHitResult blockHitResult) {
        return true;
    }
}