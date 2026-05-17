package net.typho.big_shot_lib.plugin.transform

import org.objectweb.asm.ClassVisitor

class DependencyTransformer(
    api: Int,
    visitor: ClassVisitor
) : ClassVisitor(api, visitor) {
}