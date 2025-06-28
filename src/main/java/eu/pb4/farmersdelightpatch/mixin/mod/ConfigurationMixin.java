package eu.pb4.farmersdelightpatch.mixin.mod;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vectorwing.farmersdelight.common.Configuration;

@Mixin(Configuration.class)
public class ConfigurationMixin {
    @Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/fabricmc/loader/api/FabricLoader;getEnvironmentType()Lnet/fabricmc/api/EnvType;"))
    private static EnvType heyImActualClient100Real(FabricLoader instance) {
        return EnvType.CLIENT;
    }
}
