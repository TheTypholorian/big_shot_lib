plugins {
    kotlin("jvm") version "2.4.0" apply false
    id("dev.kikugie.stonecutter")
    id("net.fabricmc.fabric-loom") version "1.16-SNAPSHOT" apply false
    id("net.fabricmc.fabric-loom-remap") version "1.16-SNAPSHOT" apply false
    id("net.neoforged.moddev") version "2.0.141" apply false
    id("dev.kikugie.postprocess.jsonlang") version "2.1-beta.4" apply false
}

stonecutter active "mc26_2_fabric"
stonecutter handlers {
    inherit("vsh", "glsl")
}
stonecutter parameters {
    constants.match(node.metadata.project.substringAfterLast('_'), "fabric", "neoforge")
    constants.put("deobfuscated", current.parsed >= "26.1")
    constants.put("sable", findProperty("deps.sable_companion") != null)
    filters.include("**/*.fsh", "**/*.vsh")

    replacements {
        string(current.parsed >= "26.1") {
            replace("classTweaker v1 named", "classTweaker v1 official")
        }
    }
}
