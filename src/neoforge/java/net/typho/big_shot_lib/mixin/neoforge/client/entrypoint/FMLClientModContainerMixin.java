package net.typho.big_shot_lib.mixin.neoforge.client.entrypoint;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.neoforgespi.language.IModInfo;
import net.typho.big_shot_lib.api.event.NeoClientEventBus;
import net.typho.big_shot_lib.client.api.NeoClientInitializer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.lang.reflect.Constructor;

@Mixin(FMLModContainer.class)
public abstract class FMLClientModContainerMixin extends ModContainer {
    public FMLClientModContainerMixin(IModInfo info) {
        super(info);
    }

    @WrapOperation(
            method = "constructMod",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/reflect/Constructor;newInstance([Ljava/lang/Object;)Ljava/lang/Object;"
            )
    )
    private <T> T constructMod(Constructor<T> instance, Object[] args, Operation<T> original) {
        T t = original.call(instance, args);

        if (t instanceof NeoClientInitializer) {
            ((NeoClientInitializer) t).onInitializeClient(NeoClientEventBus.get(getModId()));
        }

        return null;
    }
}
