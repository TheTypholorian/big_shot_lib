package net.typho.big_shot_lib.plugin.transform.util

import org.objectweb.asm.AnnotationVisitor

class KotlinMetadataVisitor(
    api: Int,
    @JvmField
    val annotation: () -> AnnotationVisitor,
    @JvmField
    val end: (metadata: Metadata) -> Metadata
) : AnnotationVisitor(api) {
    var kind = 1
    var extraInt = 0
    var metadataVersion = intArrayOf()
    var bytecodeVersion = intArrayOf()
    var extraString = ""
    var packageName = ""
    val data1 = mutableListOf<String>()
    val data2 = mutableListOf<String>()

    override fun visit(name: String, value: Any) {
        when (name) {
            "k" -> kind = value as Int
            "mv" -> metadataVersion = value as IntArray
            "bv" -> bytecodeVersion = value as IntArray
            "xs" -> extraString = value as String
            "pn" -> packageName = value as String
            "xi" -> extraInt = value as Int
        }
    }

    override fun visitArray(name: String): AnnotationVisitor {
        return object : AnnotationVisitor(api) {
            override fun visit(unused: String?, value: Any) {
                when (name) {
                    "d1" -> data1.add(value as String)
                    "d2" -> data2.add(value as String)
                }
            }
        }
    }

    override fun visitEnd() {
        val metadata = end(Metadata(
            kind = kind,
            metadataVersion = metadataVersion,
            bytecodeVersion = bytecodeVersion,
            data1 = data1.toTypedArray(),
            data2 = data2.toTypedArray(),
            extraString = extraString,
            packageName = packageName,
            extraInt = extraInt
        ))
        val annotation = annotation()

        annotation.visit("k", metadata.kind)
        annotation.visit("mv", metadata.metadataVersion)
        annotation.visit("bv", metadata.bytecodeVersion)

        annotation.visitArray("d1").let {
            for (data in metadata.data1) {
                it.visit(null, data)
            }

            it.visitEnd()
        }

        annotation.visitArray("d2").let {
            for (data in metadata.data2) {
                it.visit(null, data)
            }

            it.visitEnd()
        }

        annotation.visit("xs", metadata.extraString)
        annotation.visit("pn", metadata.packageName)
        annotation.visit("xi", metadata.extraInt)

        annotation.visitEnd()
    }
}