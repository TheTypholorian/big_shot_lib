plugins {
    // see https://fabricmc.net/develop/ for new versions
    alias(libs.plugins.loom) apply false
    // see https://projects.neoforged.net/neoforged/moddevgradle for new versions
    alias(libs.plugins.moddev) apply false
}

subprojects {
    repositories {
        ivy {
            url = uri("https://github.com/TheTypholorian/big_shot_lib/releases/download")
            patternLayout {
                artifact("[revision]/[artifact]-[revision](-[classifier]).[ext]")
            }
            metadataSources {
                artifact()
            }
        }
    }
}