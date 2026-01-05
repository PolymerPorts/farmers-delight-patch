package eu.pb4.farmersdelightpatch.impl.polydex;

import static eu.pb4.farmersdelightpatch.impl.res.UiResourceCreator.polydexBackground;

import net.minecraft.network.chat.Component;

public class PolydexTextures {
    public static final Component DECOMPOSITION;
    public static final Component COOKING_POT;
    public static final Component CUTTING_BOARD;

    public static void register() {

    }

    static {
        var t1 = polydexBackground("1");
        var t2 = polydexBackground("2");
        COOKING_POT = t1.getA();
        CUTTING_BOARD = t1.getB();
        DECOMPOSITION = t2.getA();
    }
}
