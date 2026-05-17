package net.typho.big_shot_lib.plugin.transform

import net.typho.big_shot_lib.plugin.transform.util.AnnotationScanner
import org.objectweb.asm.ClassVisitor

class ProjectTransformer(
    api: Int,
    visitor: ClassVisitor,
    @JvmField
    val annotations: AnnotationScanner
) : ClassVisitor(api, visitor) {
}