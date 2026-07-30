package net.typho.big_shot_lib.api.agent

import org.objectweb.asm.ClassWriter

class ClassWriterInfo {
    @JvmField
    var changed = false
    @JvmField
    var writerFlags = 0

    fun markChanged() {
        changed = true
    }

    fun computeFrames() {
        writerFlags = writerFlags or ClassWriter.COMPUTE_FRAMES
    }

    fun computeMaxStackSizes() {
        writerFlags = writerFlags or ClassWriter.COMPUTE_MAXS
    }
}