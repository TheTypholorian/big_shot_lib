package net.typho.big_shot_lib.plugin.transform

import org.objectweb.asm.ClassVisitor

class ProjectTransformer(api: Int, visitor: ClassVisitor) : ClassVisitor(api, visitor) {
}