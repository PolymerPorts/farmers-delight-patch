package eu.pb4.farmersdelightpatch.mixin.mod;

import eu.pb4.factorytools.api.block.FactoryBlock;
import eu.pb4.farmersdelightpatch.impl.block.BaseFactoryBlock;
import eu.pb4.farmersdelightpatch.impl.block.StateCopyFactoryBlock;
import eu.pb4.farmersdelightpatch.impl.block.WaterloggableFactoryBlock;
import eu.pb4.farmersdelightpatch.impl.item.PolyBaseItem;
import eu.pb4.farmersdelightpatch.impl.item.PolySkilletItem;
import eu.pb4.farmersdelightpatch.impl.model.CuttingBoardModel;
import eu.pb4.farmersdelightpatch.impl.model.SignModel;
import eu.pb4.farmersdelightpatch.impl.model.SkilletModel;
import eu.pb4.farmersdelightpatch.impl.model.StoveModel;
import eu.pb4.farmersdelightpatch.impl.model.generic.BlockStateModelManager;
import eu.pb4.polymer.common.api.PolymerCommonUtils;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.core.api.block.PolymerBlockUtils;
import eu.pb4.polymer.core.api.entity.PolymerEntityUtils;
import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.core.api.item.PolymerItemGroupUtils;
import eu.pb4.polymer.core.api.other.PolymerComponent;
import eu.pb4.polymer.core.api.other.PolymerConsumeEffect;
import eu.pb4.polymer.core.api.other.PolymerSoundEvent;
import eu.pb4.polymer.core.api.utils.PolymerSyncedObject;
import eu.pb4.polymer.virtualentity.api.BlockWithElementHolder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.*;
import net.minecraft.item.consume.ConsumeEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.book.RecipeBookCategories;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.*;
import vectorwing.farmersdelight.common.block.state.CanvasSign;
import vectorwing.farmersdelight.refabricated.RegUtils;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Mixin(RegUtils.class)
public class RegUtilsMixin {
    @Inject(method = "regItem", at = @At("RETURN"))
    private static void onItemRegistered(String name, Supplier<Item> supplier, CallbackInfoReturnable<Supplier<Item>> cir) {
        PolymerItem polymerItem;
        if (name.equals("skillet")) {
            polymerItem = new PolySkilletItem();
        } else {
            polymerItem = new PolyBaseItem(cir.getReturnValue().get());
        }

        PolymerItem.registerOverlay(cir.getReturnValue().get(), polymerItem);
    }

    @Inject(method = "regBlock", at = @At("RETURN"))
    private static void onBlockRegistered(String name, Supplier<Block> supplier, CallbackInfoReturnable<Supplier<Block>> cir) {
        var block = cir.getReturnValue().get();
        FactoryBlock polymerBlock;

        if (name.equals("canvas_rug")) {
            polymerBlock = BaseFactoryBlock.TRIPWIRE_FLAT;
        } else if (block instanceof CuttingBoardBlock) {
            polymerBlock = BaseFactoryBlock.TRIPWIRE_FLAT.withModel(CuttingBoardModel::new);
        } else if (block instanceof SkilletBlock) {
            polymerBlock = WaterloggableFactoryBlock.BOTTOM_TRAPDOOR.withModel(SkilletModel::new);
        } else if (block instanceof StoveBlock) {
            polymerBlock = BaseFactoryBlock.BARRIER.withModel(StoveModel::new).withTick(true);
        } else if (name.equals("half_tatami_mat") || name.equals("full_tatami_mat")) {
            polymerBlock = BaseFactoryBlock.TRIPWIRE;
        } else if (block instanceof CanvasSign canvasSign) {
            if (name.endsWith("wall_hanging_canvas_sign")) {
                polymerBlock = canvasSign.isDarkBackground() ? StateCopyFactoryBlock.HANGING_WALL_SIGN_DARK : StateCopyFactoryBlock.HANGING_WALL_SIGN;
                SignModel.addBlock(name, block);
            } else if (name.endsWith("hanging_canvas_sign")) {
                polymerBlock = canvasSign.isDarkBackground() ? StateCopyFactoryBlock.HANGING_SIGN_DARK : StateCopyFactoryBlock.HANGING_SIGN;
                SignModel.addBlock(name, block);
            } else if (name.endsWith("canvas_sign")) {
                polymerBlock = canvasSign.isDarkBackground() ? StateCopyFactoryBlock.SIGN_DARK : StateCopyFactoryBlock.SIGN;
                SignModel.addBlock(name, block);
            } else if (name.endsWith("canvas_wall_sign")) {
                polymerBlock = canvasSign.isDarkBackground() ? StateCopyFactoryBlock.WALL_SIGN_DARK : StateCopyFactoryBlock.WALL_SIGN;
                SignModel.addBlock(name, block);
            } else {
                polymerBlock = BaseFactoryBlock.SAPLING;
            }
        } else if (block instanceof SafetyNetBlock) {
            polymerBlock = WaterloggableFactoryBlock.TOP_TRAPDOOR;
        } else if (block instanceof WildRiceBlock) {
            polymerBlock = WaterloggableFactoryBlock.PLANT;
        } else if (block instanceof RopeBlock) {
            polymerBlock = BaseFactoryBlock.VINE;
        } else if (block instanceof RiceBlock) {
            polymerBlock = BaseFactoryBlock.KELP;
        } else if (block.getDefaultState().getCollisionShape(PolymerCommonUtils.getFakeWorld(), BlockPos.ORIGIN).isEmpty()) {
            polymerBlock = BaseFactoryBlock.SAPLING;
        } else {
            polymerBlock = block instanceof Waterloggable ? WaterloggableFactoryBlock.BARRIER : BaseFactoryBlock.BARRIER;
        }

        PolymerBlock.registerOverlay(block, polymerBlock);
        BlockWithElementHolder.registerOverlay(block, polymerBlock);
        BlockStateModelManager.addBlock(FarmersDelight.res(name), block);

    }

    @Inject(method = "regEntity", at = @At("RETURN"))
    private static void onEntityTypeRegistered(String name, Supplier<EntityType<?>> supplier, CallbackInfoReturnable<Supplier<EntityType<?>>> cir) {
        PolymerEntityUtils.registerOverlay(cir.getReturnValue().get(), (e) -> (context) -> EntityType.SNOWBALL);
    }

    @Inject(method = "regEffect", at = @At("RETURN"))
    private static void onStatusEffectRegistered(String name, Supplier<StatusEffect> supplier, CallbackInfoReturnable<Supplier<StatusEffect>> cir) {
        PolymerSyncedObject.setSyncedObject(Registries.STATUS_EFFECT, cir.getReturnValue().get(), (s, c) -> null);
    }

    @Inject(method = "regBlockEntity", at = @At("RETURN"))
    private static void onBlockEntityTypeRegistered(String name, Supplier<BlockEntityType<?>> supplier, CallbackInfoReturnable<Supplier<BlockEntityType<?>>> cir) {
        if (name.equals("canvas_sign")) {
            PolymerSyncedObject.setSyncedObject(Registries.BLOCK_ENTITY_TYPE, cir.getReturnValue().get(), (a, b) -> BlockEntityType.SIGN);
        } else if (name.equals("hanging_canvas_sign")) {
            PolymerSyncedObject.setSyncedObject(Registries.BLOCK_ENTITY_TYPE, cir.getReturnValue().get(), (a, b) -> BlockEntityType.HANGING_SIGN);
        }
        PolymerBlockUtils.registerBlockEntity(cir.getReturnValue().get());
    }

    @Inject(method = "regSound", at = @At("RETURN"))
    private static void onSoundEventRegistered(String name, Supplier<SoundEvent> supplier, CallbackInfoReturnable<Supplier<SoundEvent>> cir) {
        PolymerSoundEvent.registerOverlay(cir.getReturnValue().get());
    }

    @Inject(method = "regComponent(Ljava/lang/String;Ljava/util/function/Supplier;)Ljava/util/function/Supplier;", at = @At("RETURN"))
    private static void onComponentRegistered(String name, Supplier<ComponentType<?>> supplier, CallbackInfoReturnable<Supplier<ComponentType<?>>> cir) {
        PolymerComponent.registerDataComponent(cir.getReturnValue().get());
    }

    @Inject(method = "regComponent(Ljava/lang/String;Ljava/util/function/Consumer;)Ljava/util/function/Supplier;", at = @At("RETURN"))
    private static void onComponentRegistered2(String name, Consumer<ComponentType.Builder<?>> stuff, CallbackInfoReturnable<Supplier<ComponentType<?>>> cir) {
        PolymerComponent.registerDataComponent(cir.getReturnValue().get());
    }

    @Inject(method = "regEnchComponent", at = @At("RETURN"))
    private static void onEnchComponentRegistered(String name, Consumer<ComponentType.Builder<?>> stuff, CallbackInfoReturnable<Supplier<ComponentType<?>>> cir) {
        PolymerComponent.registerEnchantmentEffectComponent(cir.getReturnValue().get());
    }

    @Inject(method = "regConsumeEffectType", at = @At("RETURN"))
    private static void onConsumeEffectRegistered(String name, Supplier<ConsumeEffect.Type<?>> supplier, CallbackInfoReturnable<Supplier<ConsumeEffect.Type<?>>> cir) {
        PolymerConsumeEffect.registerConsumeEffect(cir.getReturnValue().get());
    }

    @Inject(method = "regParticle", at = @At("HEAD"), cancellable = true)
    private static void onParticleRegistered(String name, Supplier<ParticleType<?>> supplier, CallbackInfoReturnable<Supplier<ParticleType<?>>> cir) {
        cir.setReturnValue(switch (name) {
            case "star" -> (Supplier<ParticleType<?>>)() -> ParticleTypes.FIREWORK;
            case null, default -> (Supplier<ParticleType<?>>) () -> ParticleTypes.POOF;
        });
    }

    @Inject(method = "regTab", at = @At("HEAD"), cancellable = true)
    private static void onItemGroupRegistered(String name, Supplier<ItemGroup> supplier, CallbackInfoReturnable<Supplier<ItemGroup>> cir) {
        var group = supplier.get();
        PolymerItemGroupUtils.registerPolymerItemGroup(FarmersDelight.res(name), group);
        cir.setReturnValue(() -> group);
    }

    @Inject(method = "regRecipeBookCategory", at = @At("HEAD"), cancellable = true)
    private static void onRecipeBookCategoryRegistered(String name, Supplier<RecipeBookCategory> supplier, CallbackInfoReturnable<Supplier<RecipeBookCategory>> cir) {
        cir.setReturnValue(() -> RecipeBookCategories.CAMPFIRE);
    }

    @Inject(method = "regRecipeDisplay", at = @At("HEAD"), cancellable = true)
    private static void onRecipeDisplayRegistered(String name, Supplier<RecipeDisplay.Serializer<?>> supplier, CallbackInfoReturnable<Supplier<RecipeDisplay.Serializer<?>>> cir) {
        cir.setReturnValue(() -> null);
    }

    @Inject(method = "regMenu", at = @At("HEAD"), cancellable = true)
    private static void onMenuRegistered(String name, Supplier<ScreenHandlerType<?>> supplier, CallbackInfoReturnable<Supplier<ScreenHandlerType<?>>> cir) {
        var val = supplier.get();
        cir.setReturnValue(() -> val);
    }
}
