package eu.pb4.farmersdelightpatch.mixin.mod;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.function.Function;
import java.util.function.Supplier;

@Mixin(ModBlocks.class)
public class ModBlocksMixin {
    @Inject(method = "regBlock", at = @At("HEAD"))
    private static void opaqueBeNot(String name, Function<AbstractBlock.Settings, Block> function, AbstractBlock.Settings properties, CallbackInfoReturnable<Supplier<Block>> cir) {
        properties.nonOpaque();
    }
}
