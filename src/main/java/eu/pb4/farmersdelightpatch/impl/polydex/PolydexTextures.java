package eu.pb4.farmersdelightpatch.impl.polydex;

import net.minecraft.text.Text;

import static eu.pb4.farmersdelightpatch.impl.res.UiResourceCreator.polydexBackground;

public class PolydexTextures {
    public static final Text DECOMPOSITION;
    public static final Text COOKING_POT;
    public static final Text CUTTING_BOARD;

    public static void register() {

    }

    static {
        var t1 = polydexBackground("1");
        var t2 = polydexBackground("2");
        COOKING_POT = t1.getLeft();
        CUTTING_BOARD = t1.getRight();
        DECOMPOSITION = t2.getLeft();
    }
}
