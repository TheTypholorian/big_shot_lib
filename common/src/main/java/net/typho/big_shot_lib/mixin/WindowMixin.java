package net.typho.big_shot_lib.mixin;

import com.mojang.blaze3d.platform.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;

@Mixin(Window.class)
public class WindowMixin {
    @ModifyArgs(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/glfw/GLFW;glfwWindowHint(II)V",
                    remap = false
            )
    )
    private void init(Args args) {
        switch ((Integer) args.get(0)) {
            case GLFW_CONTEXT_VERSION_MAJOR: {
                args.set(1, 4);
                break;
            }
            case GLFW_CONTEXT_VERSION_MINOR: {
                args.set(1, 5);
                break;
            }
        }
    }
}
