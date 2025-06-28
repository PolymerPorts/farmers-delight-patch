package eu.pb4.farmersdelightpatch.impl.res;

import com.mojang.serialization.MapCodec;
import eu.pb4.polymer.resourcepack.extras.api.format.item.special.SpecialModel;

public record SkilletSpecialModel() implements SpecialModel {
    public static final MapCodec<SkilletSpecialModel> CODEC = MapCodec.unit(new SkilletSpecialModel());

    @Override
    public MapCodec<? extends SpecialModel> codec() {
        return CODEC;
    }
}