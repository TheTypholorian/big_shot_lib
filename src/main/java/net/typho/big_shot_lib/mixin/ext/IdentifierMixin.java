package net.typho.big_shot_lib.mixin.ext;

import net.minecraft.resources.Identifier;
import net.typho.big_shot_lib.api.ext.IdentifierExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Identifier.class)
public class IdentifierMixin implements IdentifierExtension {
}