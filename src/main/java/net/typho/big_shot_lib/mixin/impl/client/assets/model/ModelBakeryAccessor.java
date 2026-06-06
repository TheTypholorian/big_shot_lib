package net.typho.big_shot_lib.mixin.impl.client.assets.model;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelIdentifier;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ModelBakery.class)
public interface ModelBakeryAccessor {
    @Invoker("registerModelAndLoadDependencies")
    void big_shot_lib$registerModelAndLoadDependencies(ModelIdentifier modelLocation, UnbakedModel model);

    @Invoker("getModel")
    UnbakedModel big_shot_lib$getModel(Identifier modelLocation);
}
