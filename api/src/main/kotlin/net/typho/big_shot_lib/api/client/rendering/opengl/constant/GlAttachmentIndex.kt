package net.typho.big_shot_lib.api.client.rendering.opengl.constant

import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed
import org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0
import org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT
import org.lwjgl.opengl.GL30.GL_DEPTH_STENCIL_ATTACHMENT
import org.lwjgl.opengl.GL30.GL_STENCIL_ATTACHMENT

sealed interface GlAttachmentIndex : GlNamed {
    @JvmRecord
    data class Color(
        @JvmField
        val index: Int
    ) : GlAttachmentIndex {
        override val glId: Int
            get() = GL_COLOR_ATTACHMENT0 + index
    }

    object Depth : GlAttachmentIndex {
        override val glId: Int = GL_DEPTH_ATTACHMENT
    }

    object Stencil : GlAttachmentIndex {
        override val glId: Int = GL_STENCIL_ATTACHMENT
    }

    object DepthStencil : GlAttachmentIndex {
        override val glId: Int = GL_DEPTH_STENCIL_ATTACHMENT
    }
}