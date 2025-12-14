package net.typho.big_shot_lib.api

import com.mojang.blaze3d.pipeline.RenderTarget
import com.mojang.blaze3d.platform.Window
import com.mojang.blaze3d.shaders.AbstractUniform
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.AbstractTexture
import net.typho.big_shot_lib.BigShotLib
import net.typho.big_shot_lib.gl.Bindable
import net.typho.big_shot_lib.gl.GlResourceInstance
import org.joml.Matrix4f
import org.lwjgl.glfw.GLFW

interface IShader : Bindable, GlResourceInstance {
    fun getUniform(name: String): AbstractUniform?

    fun setSampler(name: String, id: Int)

    fun setSampler(name: String, target: RenderTarget) = setSampler(name, target.colorTextureId)

    fun setSampler(name: String, texture: AbstractTexture) = setSampler(name, texture.id)

    fun setSampler(name: String, texture: ITexture) = setSampler(name, texture.id())

    fun setCommonUniforms(
        time: Float = GLFW.glfwGetTime().toFloat(),
        window: Window = Minecraft.getInstance().window,
        projMat: Matrix4f = RenderSystem.getProjectionMatrix(),
        modelViewMat: Matrix4f = BigShotLib.getViewMatrix()
    ) {
        getUniform("Time")?.set(time)
        getUniform("ScreenSize")?.set(window.width.toFloat(), window.height.toFloat())
        getUniform("ProjMat")?.set(projMat)
        getUniform("ModelViewMat")?.set(modelViewMat)
    }
}