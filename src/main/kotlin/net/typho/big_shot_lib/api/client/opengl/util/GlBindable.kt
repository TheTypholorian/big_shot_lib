package net.typho.big_shot_lib.api.client.opengl.util

import net.typho.big_shot_lib.api.client.opengl.state.GlStateTracker

interface GlBindable<B : BoundResource> {
    fun bind(tracker: GlStateTracker = OpenGL.INSTANCE): B

    interface Typed<T : GlNamed, B : BoundResource> {
        fun bind(type: T, tracker: GlStateTracker = OpenGL.INSTANCE): B
    }
}