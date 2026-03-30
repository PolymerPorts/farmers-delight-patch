package eu.pb4.farmersdelightpatch.impl.res;

import com.mojang.serialization.MapCodec;
import eu.pb4.polymer.resourcepack.extras.api.format.item.model.ItemModel;
import eu.pb4.polymer.resourcepack.extras.api.format.item.special.SpecialModel;

public record SkilletFlipItemModel() implements ItemModel {
    public static final MapCodec<SkilletFlipItemModel> CODEC = MapCodec.unit(new SkilletFlipItemModel());

    @Override
    public MapCodec<? extends ItemModel> codec() {
        return CODEC;
    }
}