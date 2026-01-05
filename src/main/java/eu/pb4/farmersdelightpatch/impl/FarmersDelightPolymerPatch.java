package eu.pb4.farmersdelightpatch.impl;

import eu.pb4.farmersdelightpatch.impl.polydex.PolydexCompat;
import eu.pb4.farmersdelightpatch.impl.res.GuiTextures;
import eu.pb4.farmersdelightpatch.impl.res.ResourcePackGenerator;
import eu.pb4.farmersdelightpatch.impl.res.SkilletCookingConditionalItemModelProperty;
import eu.pb4.farmersdelightpatch.impl.res.SkilletSpecialModel;
import eu.pb4.polymer.core.api.utils.PolymerSyncedObject;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import eu.pb4.polymer.resourcepack.extras.api.ResourcePackExtras;
import eu.pb4.polymer.resourcepack.extras.api.format.item.ItemAsset;
import eu.pb4.polymer.resourcepack.extras.api.format.item.model.BasicItemModel;
import eu.pb4.polymer.resourcepack.extras.api.format.item.property.bool.BooleanProperty;
import eu.pb4.polymer.resourcepack.extras.api.format.item.special.SpecialModel;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.MixinEnvironment;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModEffects;

public class FarmersDelightPolymerPatch implements ModInitializer {
    public static final String MOD_ID = "farmers-delight-polymer-patch";
    public static final Logger LOGGER = LoggerFactory.getLogger("farmers-delight-polymer-patch");
    @Override
    public void onInitialize() {
        //MixinEnvironment.getCurrentEnvironment().audit();
        PolymerResourcePackUtils.addModAssets("farmersdelight");
        PolymerResourcePackUtils.addModAssets(MOD_ID);
        ResourcePackExtras.forDefault().addBridgedModelsFolder(Identifier.fromNamespaceAndPath("farmersdelight", "block"));
        ResourcePackExtras.forDefault().addBridgedModelsFolder(Identifier.fromNamespaceAndPath("farmers-delight-patch", "sgui"), (id, b) -> {
            return new ItemAsset(new BasicItemModel(id), new ItemAsset.Properties(true, true));
        });
        ResourcePackExtras.forDefault().addBridgedModelsFolder(Identifier.fromNamespaceAndPath("farmers-delight-patch", "block"));

        PolymerSyncedObject.setSyncedObject(BuiltInRegistries.MOB_EFFECT, ModEffects.COMFORT.value(), (s, c) -> null);
        PolymerSyncedObject.setSyncedObject(BuiltInRegistries.MOB_EFFECT, ModEffects.NOURISHMENT.value(), (s, c) -> null);

        BooleanProperty.TYPES.put(FarmersDelight.res("skillet/is_cooking"), SkilletCookingConditionalItemModelProperty.MAP_CODEC);
        SpecialModel.TYPES.put(FarmersDelight.res("skillet"), SkilletSpecialModel.CODEC);

        ResourcePackGenerator.setup();
        GuiTextures.register();
        PolydexCompat.register();
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath("farmers-delight-patch", path);
    }
}