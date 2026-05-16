package net.typho.big_shot_lib.mixin.impl.iface;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexFormat;
import net.typho.big_shot_lib.impl.util.ImmutableExtension;
import net.typho.big_shot_lib.impl.util.ImmutableExtensionKt;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(VertexFormat.Builder.class)
public abstract class VertexFormatBuilderMixin implements ImmutableExtension<NeoVertexFormat.Builder.ExtensionValue> {
    @Shadow
    public abstract VertexFormat.Builder add(String string, VertexFormatElement vertexFormatElement);

    @Shadow
    public abstract VertexFormat.Builder padding(int i);

    @Shadow
    public abstract VertexFormat build();

    @Unique
    private final NeoVertexFormat.Builder.ExtensionValue big_shot_lib$extension_value = new NeoVertexFormat.Builder.ExtensionValue() {
        @Override
        public NeoVertexFormat.Builder add(@NotNull String name, @NotNull NeoVertexFormat.Element element) {
            VertexFormatBuilderMixin.this.add(name, ImmutableExtensionKt.getExtensionValue(element, VertexFormatElement.class));
            return this;
        }

        @Override
        public NeoVertexFormat.Builder padding(int bytes) {
            VertexFormatBuilderMixin.this.padding(bytes);
            return this;
        }

        @Override
        @NotNull
        public NeoVertexFormat build() {
            return ImmutableExtensionKt.getExtensionValue(VertexFormatBuilderMixin.this.build(), NeoVertexFormat.class);
        }

        @Override
        public VertexFormat.Builder getBig_shot_lib$extension_value() {
            return (VertexFormat.Builder) (Object) VertexFormatBuilderMixin.this;
        }
    };

    @Override
    public NeoVertexFormat.Builder.ExtensionValue getBig_shot_lib$extension_value() {
        return big_shot_lib$extension_value;
    }
}
