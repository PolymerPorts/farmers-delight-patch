package eu.pb4.farmersdelightpatch.mixin;

import com.bawnorton.mixinsquared.adjuster.tools.AdjustableAnnotationNode;
import com.bawnorton.mixinsquared.adjuster.tools.AdjustableInjectNode;
import com.bawnorton.mixinsquared.api.MixinAnnotationAdjuster;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.injection.Inject;

import java.util.List;

public class FarmersDelightPatchMixinAnnotationAdjuster implements MixinAnnotationAdjuster {
    @Override
    public AdjustableAnnotationNode adjust(List<String> targetClassNames, String mixinClassName, MethodNode method, AdjustableAnnotationNode annotation) {
        if (mixinClassName.equals("vectorwing.farmersdelight.common.mixin.refabricated.ServerRecipeBookMixin")) return null;
        if (!mixinClassName.equals("vectorwing.farmersdelight.common.mixin.refabricated.RecipeBookSettingsMixin")) return annotation;
        if (!annotation.is(Inject.class)) return annotation;
        var node = annotation.as(AdjustableInjectNode.class);
        
        if(node.getMethod().getFirst().equals("<clinit>")) {
            return null;
        } else {
            return annotation;
        }
    }
}