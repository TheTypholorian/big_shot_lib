package net.typho.big_shot_lib.mixin.impl;

//? if <1.21.5 {
import com.mojang.blaze3d.platform.GlDebug;
//? } else {
/*import com.mojang.blaze3d.opengl.GlDebug;
*///? }

import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager;
import net.typho.big_shot_lib.api.client.rendering.opengl.util.GlFlag;
import net.typho.big_shot_lib.api.error.GlException;
import net.typho.big_shot_lib.api.util.platform.PlatformUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GlDebug.class)
public class GlDebugMixin {
    static {
        if (PlatformUtil.Companion.getINSTANCE().isDevEnv()) {
            NeoGlStateManager.Companion.getINSTANCE().getDebugOutputSynchronousEnabled().push(true);
        }
    }

    @ModifyArg(
            method = "printDebugLog",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/slf4j/Logger;info(Ljava/lang/String;Ljava/lang/Object;)V"
            ),
            index = 1
    )
    private static Object printDebugLog(Object arg) {
        StringBuilder builder = new StringBuilder(arg.toString());

        for (StackTraceElement ste : (new GlException().getStackTrace())) {
            builder.append("\n\tat ").append(ste);
        }

        return builder.toString();
    }
}
