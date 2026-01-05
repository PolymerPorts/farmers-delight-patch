package eu.pb4.farmersdelightpatch.mixin.mod;

import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vectorwing.farmersdelight.common.item.RottenTomatoItem;

@Mixin(RottenTomatoItem.class)
public class RottenTomatoItemMixin {
    @Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isClientSide()Z", ordinal = 1))
    private boolean actLikeTheClientYouArent(Level instance) {
        return true;
    }
}
