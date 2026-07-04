package net.typho.big_shot_lib.mixin.impl.client.rendering.opengl.ssbo;

import com.mojang.blaze3d.opengl.Uniform;
import net.typho.big_shot_lib.impl.client.rendering.opengl.ssbo.UboUniformExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Uniform.Ubo.class)
public class UboUniformMixin implements UboUniformExtension {
    @Unique
    private boolean big_shot_lib$isSsbo = false;

    @Override
    public boolean getBig_shot_lib$isSsbo() {
        return big_shot_lib$isSsbo;
    }

    @Override
    public void setBig_shot_lib$isSsbo(boolean b) {
        big_shot_lib$isSsbo = b;
    }
}
