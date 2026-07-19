package eu.pb4.farmersdelightpatch.impl.res;

import eu.pb4.factorytools.api.block.model.generic.BlockStateModelManager;
import eu.pb4.factorytools.api.resourcepack.ModelModifiers;
import eu.pb4.polymer.resourcepack.api.AssetPaths;
import eu.pb4.polymer.resourcepack.api.PackResource;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import eu.pb4.polymer.resourcepack.api.ResourcePackBuilder;
import eu.pb4.polymer.resourcepack.extras.api.format.atlas.AtlasAsset;
import eu.pb4.polymer.resourcepack.extras.api.format.atlas.SingleAtlasSource;
import eu.pb4.polymer.resourcepack.extras.api.format.item.ItemAsset;
import eu.pb4.polymer.resourcepack.extras.api.format.item.model.*;
import eu.pb4.polymer.resourcepack.extras.api.format.item.property.bool.CustomModelDataFlagProperty;
import eu.pb4.polymer.resourcepack.extras.api.format.model.ModelAsset;
import eu.pb4.polymer.resourcepack.extras.api.format.model.ModelElement;
import vectorwing.farmersdelight.FarmersDelight;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Set;

import net.minecraft.world.phys.Vec3;

import static eu.pb4.farmersdelightpatch.impl.FarmersDelightPolymerPatch.id;

public class ResourcePackGenerator {
    private static final Set<String> EXPANDABLE = Set.of("wall", "fence", "slab", "stairs", "pressure_plate", "button", "sign");

    public static void setup() {
        PolymerResourcePackUtils.RESOURCE_PACK_AFTER_INITIAL_CREATION_EVENT.register(ResourcePackGenerator::build);
        UiResourceCreator.setup();
    }

    private static void build(ResourcePackBuilder builder) {
        final var expansion = new Vec3(0.08, 0.08, 0.08);
        final var safetyNetOffset = new Vec3(0, 7, 0);

        builder.forEachResource((string, resource) -> {
            for (var expandable : EXPANDABLE) {
                if (string.contains(expandable) && string.startsWith("assets/farmersdelight/models/block/")) {
                    var asset = ModelAsset.fromJson(Objects.requireNonNull(resource.asString()));
                    if (asset.parent().isPresent()) {
                        var parentId = asset.parent().get();
                        var parentAsset = ModelAsset.fromJson(new String(Objects.requireNonNull(builder.getDataOrSource(AssetPaths.model(parentId) + ".json")), StandardCharsets.UTF_8));
                        builder.addData(AssetPaths.model("farmers-delight-patch", parentId.getPath()) + ".json", ModelModifiers.expandModel(parentAsset, expansion));
                    }
                }
            }
        });


        builder.addResourceConverter(((string, resource) -> {
            if (string.equals("assets/farmersdelight/items/skillet.json")) {
                var asset = ItemAsset.fromJson(resource.asString());
                var replacer = new ItemModel.Replacer[] { null };
                replacer[0] = (parent, model) -> {
                    if (model instanceof ConditionItemModel conditionItemModel && conditionItemModel.property() instanceof SkilletCookingConditionalItemModelProperty) {
                        return new ConditionItemModel(new CustomModelDataFlagProperty(0),
                                replacer[0].modifyDeep(model, conditionItemModel.onTrue()),
                                replacer[0].modifyDeep(model, conditionItemModel.onFalse())
                        );
                    } else if (model instanceof SpecialItemModel specialItemModel && specialItemModel.specialModel() instanceof SkilletSpecialModel) {
                        return new BasicItemModel(FarmersDelight.id("item/skillet_cooking"));
                    } else if (model instanceof SkilletFlipItemModel) {
                        return new BasicItemModel(FarmersDelight.id("item/skillet_cooking"));
                    }
                    return model;
                };
                return PackResource.fromAsset(new ItemAsset(replacer[0].modifyDeep(EmptyItemModel.INSTANCE, asset.model()), new ItemAsset.Properties(false, false)));
            } else if (string.equals("assets/farmersdelight/models/block/safety_net.json")) {
                var asset = ModelAsset.fromJson(resource.asString());
                return PackResource.fromAsset(new ModelAsset(asset.parent(), asset.elements().map(x -> x.stream()
                        .map(element -> new ModelElement(element.from().add(safetyNetOffset), element.to().add(safetyNetOffset),
                                element.faces(), element.rotation(), element.shade(), element.lightEmission())
                        ).toList()), asset.textures(), asset.display(), asset.guiLight(), asset.ambientOcclusion()));
            }
            if (!string.contains("_uvlock_")) {
                for (var expandable : EXPANDABLE) {
                    if (string.contains(expandable) && string.startsWith("assets/farmersdelight/models/block/")) {
                        var asset = ModelAsset.fromJson(Objects.requireNonNull(resource.asString()));
                        return PackResource.fromAsset(new ModelAsset(asset.parent().map(x -> id(x.getPath())), asset.elements(), asset.textures(), asset.display(), asset.guiLight(), asset.ambientOcclusion()));
                    }
                }
            }
            return resource;
        }));

        /*for (var entry : BlockStateModelManager.UV_LOCKED_MODELS.get("farmersdelight").entrySet()) {
            for (var v : entry.getValue()) {
                var suffix = "_uvlock_" + v.x() + "_" + v.y();
                var modelId = v.model().withSuffixedPath(suffix);
                var asset = ModelAsset.fromJson(new String(Objects.requireNonNull(builder.getData(AssetPaths.model(v.model()) + ".json")), StandardCharsets.UTF_8));

                if (asset.parent().isPresent()) {
                    var parentId = asset.parent().get();
                    var parentAsset = ModelAsset.fromJson(new String(Objects.requireNonNull(builder.getDataOrSource(AssetPaths.model(parentId) + ".json")), StandardCharsets.UTF_8));
                    builder.addData(AssetPaths.model("enderscape-patch", parentId.getPath() + suffix) + ".json",
                            ModelModifiers.expandModelAndRotateUVLocked(parentAsset, Vec3d.ZERO, v.x(), v.y()));
                    builder.addData(AssetPaths.model(modelId) + ".json",
                            new ModelAsset(Optional.of(Identifier.of("enderscape-patch", parentId.getPath() + suffix)), asset.elements(),
                                    asset.textures(), asset.display(), asset.guiLight(), asset.ambientOcclusion()).toBytes());
                }
            }
        }*/

        var atlas = AtlasAsset.builder();

        builder.addData("assets/minecraft/atlases/blocks.json", atlas.build());
    }
}
