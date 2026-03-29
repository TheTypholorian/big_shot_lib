package net.typho.big_shot_lib.impl.mixin.iface;

//? if <1.21.6 {
/*import com.mojang.blaze3d.buffers.BufferType;
import com.mojang.blaze3d.buffers.BufferUsage;
*///? } else {
import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed;
//? }

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.opengl.GlBuffer;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferTarget;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferUsage;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundBuffer;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResourceType;
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager;
import net.typho.big_shot_lib.impl.util.ImmutableExtension;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

//? if <1.21.5 {
/*import dev.kikugie.fletching_table.annotation.MixinIgnore;

@MixinIgnore
*///? }
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(GlBuffer.class)
public abstract class GlBufferMixin extends GpuBuffer implements ImmutableExtension<net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlBuffer> {
    //? if <1.21.6 {
    /*public GlBufferMixin(BufferType bufferType, BufferUsage bufferUsage, int i) {
        super(bufferType, bufferUsage, i);
    }
    *///? } else {
    public GlBufferMixin(int p_374082_, int p_418241_) {
        super(p_374082_, p_418241_);
    }
    //? }

    @Shadow
    public abstract boolean isClosed();

    @Shadow
    public abstract void close();

    @Shadow
    @Final
    protected int handle;

    @Override
    public net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlBuffer getBig_shot_lib$extension_value() {
        return new net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlBuffer() {
            @Override
            public void free() {
                GlBufferMixin.this.close();
            }

            @Override
            public int getGlId() {
                return handle;
            }

            @Override
            public boolean getFreed() {
                return isClosed();
            }

            @Override
            public @NotNull GlResourceType getType() {
                return GlResourceType.BUFFER;
            }

            @Override
            public @NotNull GlBoundBuffer bind(@NotNull GlBufferTarget target) {
                return new GlBoundBuffer.NonResizable(
                        this,
                        target,
                        NeoGlStateManager.INSTANCE.getBuffers().get(target).push(handle),
                        size(),
                        //? if <1.21.6 {
                        /*switch (usage()) {
                            case DYNAMIC_WRITE -> GlBufferUsage.DYNAMIC_DRAW;
                            case STATIC_WRITE -> GlBufferUsage.STATIC_DRAW;
                            case STREAM_WRITE -> GlBufferUsage.STREAM_DRAW;
                            case STATIC_READ -> GlBufferUsage.STATIC_READ;
                            case DYNAMIC_READ -> GlBufferUsage.DYNAMIC_READ;
                            case STREAM_READ -> GlBufferUsage.STREAM_READ;
                            case DYNAMIC_COPY -> GlBufferUsage.DYNAMIC_COPY;
                            case STATIC_COPY -> GlBufferUsage.STATIC_COPY;
                            case STREAM_COPY -> GlBufferUsage.STREAM_COPY;
                        }
                        *///? } else {
                        GlNamed.getEnum(GlBufferUsage.class, usage())
                        //? }
                );
            }
        };
    }
}
