package eu.pb4.farmersdelightpatch.mixin.mod;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

@Mixin(ModBlocks.class)
public class ModBlocksMixin {
    @Inject(method = "regBlock", at = @At("HEAD"))
    private static void opaqueBeNot(String name, Function<BlockBehaviour.Properties, Block> function, BlockBehaviour.Properties properties, CallbackInfoReturnable<Supplier<Block>> cir) {
        properties.noOcclusion();
    }
}
