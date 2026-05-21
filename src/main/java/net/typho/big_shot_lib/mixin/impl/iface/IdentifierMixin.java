package net.typho.big_shot_lib.mixin.impl.iface;

import net.minecraft.resources.Identifier;
import net.typho.big_shot_lib.api.ext.IdentifierExtension;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(Identifier.class)
public class IdentifierMixin implements IdentifierExtension {
    @Shadow
    @Final
    public static String DEFAULT_NAMESPACE;

    @Shadow
    @Final
    private String namespace;
    @Shadow
    @Final
    private String path;

    @Override
    @NotNull
    public String toShortString() {
        return Objects.equals(namespace, DEFAULT_NAMESPACE) ? path : toString();
    }
}
