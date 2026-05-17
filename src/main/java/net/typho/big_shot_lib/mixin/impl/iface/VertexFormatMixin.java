package net.typho.big_shot_lib.mixin.impl.iface;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import kotlin.collections.CollectionsKt;
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexFormat;
import net.typho.big_shot_lib.impl.client.rendering.opengl.NeoVertexFormatExtensionValue;
import net.typho.big_shot_lib.impl.util.ImmutableExtension;
import net.typho.big_shot_lib.impl.util.ImmutableExtensionKt;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;

@Mixin(VertexFormat.class)
public abstract class VertexFormatMixin implements ImmutableExtension<NeoVertexFormatExtensionValue> {
    @Shadow
    @Final
    private int vertexSize;
    @Shadow
    @Final
    private List<VertexFormatElement> elements;

    @Shadow
    public abstract List<String> getElementAttributeNames();

    @Shadow
    public abstract String getElementName(VertexFormatElement vertexFormatElement);

    @Shadow
    public abstract int getOffset(VertexFormatElement vertexFormatElement);

    @Unique
    private final NeoVertexFormatExtensionValue big_shot_lib$extension_value = new NeoVertexFormatExtensionValue() {
        @Override
        public int getVertexSizeBytes() {
            return vertexSize;
        }

        @Override
        @NotNull
        public Element[] getElements() {
            return CollectionsKt.map(elements, element -> ImmutableExtensionKt.getExtensionValue(element, NeoVertexFormat.Element.class)).toArray(Element[]::new);
        }

        @Override
        @NotNull
        public String[] getElementNames() {
            return getElementAttributeNames().toArray(String[]::new);
        }

        @Override
        @NotNull
        public String getElementName(@NotNull Element element) {
            return VertexFormatMixin.this.getElementName(ImmutableExtensionKt.getExtensionValue(element, VertexFormatElement.class));
        }

        @Override
        public int getElementOffset(@NotNull Element element) {
            return VertexFormatMixin.this.getOffset(ImmutableExtensionKt.getExtensionValue(element, VertexFormatElement.class));
        }

        @Override
        public void initVertexArrayState() {
            Element[] elements = getElements();

            for (int i = 0; i < elements.length; i++) {
                glEnableVertexAttribArray(i);
                Element element = elements[i];
                element.vertexAttribPointer(i, getElementOffset(element), vertexSize);
            }
        }

        @Override
        @NotNull
        public VertexFormat getBig_shot_lib$extension_value() {
            return (VertexFormat) (Object) VertexFormatMixin.this;
        }
    };

    @Override
    public NeoVertexFormatExtensionValue getBig_shot_lib$extension_value() {
        return big_shot_lib$extension_value;
    }
}
