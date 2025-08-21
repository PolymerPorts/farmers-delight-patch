package eu.pb4.farmersdelightpatch.impl.res;

import eu.pb4.factorytools.api.block.model.generic.BlockStateModelManager;
import eu.pb4.factorytools.api.resourcepack.ModelModifiers;
import eu.pb4.polymer.resourcepack.api.AssetPaths;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import eu.pb4.polymer.resourcepack.api.ResourcePackBuilder;
import eu.pb4.polymer.resourcepack.extras.api.format.atlas.AtlasAsset;
import eu.pb4.polymer.resourcepack.extras.api.format.atlas.SingleAtlasSource;
import eu.pb4.polymer.resourcepack.extras.api.format.item.ItemAsset;
import eu.pb4.polymer.resourcepack.extras.api.format.item.model.*;
import eu.pb4.polymer.resourcepack.extras.api.format.item.property.bool.CustomModelDataFlagProperty;
import eu.pb4.polymer.resourcepack.extras.api.format.model.ModelAsset;
import eu.pb4.polymer.resourcepack.extras.api.format.model.ModelElement;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import vectorwing.farmersdelight.FarmersDelight;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

import static eu.pb4.farmersdelightpatch.impl.FarmersDelightPolymerPatch.id;

public class ResourcePackGenerator {
    public static void setup() {
        PolymerResourcePackUtils.RESOURCE_PACK_CREATION_EVENT.register(ResourcePackGenerator::build);
        UiResourceCreator.setup();
    }

    private static void build(ResourcePackBuilder builder) {
        final var signExtension = new Vec3d(0.04, 0.04, 0.04);
        final var safetyNetOffset = new Vec3d(0, 7, 0);

        builder.addWriteConverter(((string, bytes) -> {
            if (string.equals("assets/farmersdelight/items/skillet.json")) {
                var asset = ItemAsset.fromJson(new String(bytes, StandardCharsets.UTF_8));
                var replacer = new ItemModel.Replacer[] { null };
                replacer[0] = (parent, model) -> {
                    if (model instanceof ConditionItemModel conditionItemModel && conditionItemModel.property() instanceof SkilletCookingConditionalItemModelProperty) {
                        return new ConditionItemModel(new CustomModelDataFlagProperty(0),
                                replacer[0].modifyDeep(model, conditionItemModel.onTrue()),
                                replacer[0].modifyDeep(model, conditionItemModel.onFalse())
                        );
                    } else if (model instanceof SpecialItemModel specialItemModel && specialItemModel.specialModel() instanceof SkilletSpecialModel) {
                        return new BasicItemModel(FarmersDelight.res("item/skillet_cooking"));
                    }
                    return model;
                };
                return new ItemAsset(replacer[0].modifyDeep(EmptyItemModel.INSTANCE, asset.model()), new ItemAsset.Properties(false, false)).toBytes();
            } else if (string.startsWith("assets/farmers-delight-patch/models/block/template_")) {
                var asset = ModelAsset.fromJson(new String(bytes, StandardCharsets.UTF_8));
                return new ModelAsset(asset.parent(), asset.elements().map(x -> x.stream()
                        .map(element -> new ModelElement(element.from().subtract(signExtension), element.to().add(signExtension),
                                element.faces(), element.rotation(), element.shade(), element.lightEmission())
                        ).toList()), asset.textures(), asset.display(), asset.guiLight(), asset.ambientOcclusion()).toBytes();
            } else if (string.equals("assets/farmersdelight/models/block/safety_net.json")) {
                var asset = ModelAsset.fromJson(new String(bytes, StandardCharsets.UTF_8));
                return new ModelAsset(asset.parent(), asset.elements().map(x -> x.stream()
                        .map(element -> new ModelElement(element.from().add(safetyNetOffset), element.to().add(safetyNetOffset),
                                element.faces(), element.rotation(), element.shade(), element.lightEmission())
                        ).toList()), asset.textures(), asset.display(), asset.guiLight(), asset.ambientOcclusion()).toBytes();
            }
            return bytes;
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


        createCanvasModel(builder, "", "", atlas);
        for (var color : DyeColor.values()) {
            createCanvasModel(builder, "_" + color.asString(), color.asString() + "_", atlas);
        }

        builder.addData("assets/minecraft/atlases/blocks.json", atlas.build());
    }

    private static void createCanvasModel(ResourcePackBuilder builder, String suffix, String prefix, AtlasAsset.Builder atlas) {
        var textureRegular = FarmersDelight.res("entity/signs/canvas" + suffix);
        var textureHanging = FarmersDelight.res("entity/signs/hanging/canvas" + suffix);

        atlas.add(new SingleAtlasSource(textureRegular, Optional.empty()));
        atlas.add(new SingleAtlasSource(textureHanging, Optional.empty()));

        builder.addData(AssetPaths.blockModel(id(prefix + "canvas_sign")), ModelAsset.builder()
                .parent(Identifier.of("factorytools", "block_sign/template_sign"))
                .texture("sign", textureRegular.toString()).build());
        builder.addData(AssetPaths.blockModel(id(prefix + "canvas_wall_sign")), ModelAsset.builder()
                .parent(Identifier.of("factorytools", "block_sign/template_wall_sign"))
                .texture("sign", textureRegular.toString()).build());
        builder.addData(AssetPaths.blockModel(id(prefix + "hanging_canvas_sign")), ModelAsset.builder()
                .parent(Identifier.of("factorytools", "block_sign/template_hanging_sign"))
                .texture("sign", textureHanging.toString()).build());
        builder.addData(AssetPaths.blockModel(id(prefix + "wall_hanging_canvas_sign")), ModelAsset.builder()
                .parent(Identifier.of("factorytools", "block_sign/template_wall_hanging_sign"))
                .texture("sign", textureHanging.toString()).build());
    }
}
