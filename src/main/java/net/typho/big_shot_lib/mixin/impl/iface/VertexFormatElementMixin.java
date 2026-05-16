package net.typho.big_shot_lib.mixin.impl.iface;

import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlDataType;
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexFormat;
import net.typho.big_shot_lib.impl.util.ImmutableExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(VertexFormatElement.class)
public abstract class VertexFormatElementMixin implements ImmutableExtension<NeoVertexFormat.Element.ExtensionValue> {
    @Shadow
    @Final
    private int index;
    @Shadow
    @Final
    private VertexFormatElement.Type type;
    @Shadow
    @Final
    private VertexFormatElement.Usage usage;
    @Shadow
    @Final
    private int count;

    @Shadow
    public abstract int byteSize();

    @Unique
    private final NeoVertexFormat.Element.ExtensionValue big_shot_lib$extension_value = new NeoVertexFormat.Element.ExtensionValue() {
        @Override
        public int getIndex() {
            return index;
        }

        @Override
        public @NotNull GlDataType getType() {
            return switch (type) {
                case FLOAT -> GlDataType.FLOAT;
                case UBYTE -> GlDataType.UNSIGNED_BYTE;
                case BYTE -> GlDataType.BYTE;
                case USHORT -> GlDataType.UNSIGNED_SHORT;
                case SHORT -> GlDataType.SHORT;
                case UINT -> GlDataType.UNSIGNED_INT;
                case INT -> GlDataType.INT;
            };
        }

        @Override
        @Nullable
        public Boolean getNormalized() {
            return switch (usage) {
                case POSITION -> false;
                case NORMAL, COLOR -> true;
                case UV -> type == VertexFormatElement.Type.FLOAT ? false : null;
                case GENERIC -> null;
            };
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public int getSizeBytes() {
            return byteSize();
        }

        @Override
        public void vertexAttribPointer(int index, long offset, int stride) {
            getType().vertexAttribPointer(index, getCount(), getNormalized(), stride, offset);
        }

        @Override
        @NotNull
        public VertexFormatElement getBig_shot_lib$extension_value() {
            return (VertexFormatElement) (Object) VertexFormatElementMixin.this;
        }
    };

    @Override
    public NeoVertexFormat.Element.ExtensionValue getBig_shot_lib$extension_value() {
        return big_shot_lib$extension_value;
    }
}
