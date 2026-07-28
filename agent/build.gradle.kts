plugins {
    id("java")
    id("com.gradleup.shadow") version "9.6.1"
}

group = "net.typho.big_shot_lib"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.ow2.asm:asm:9.8")
    implementation("org.ow2.asm:asm-tree:9.8")
}

tasks.jar {
    archiveVersion.set("")
    destinationDirectory.set(rootProject.file("src/main/resources"))

    manifest {
        attributes(
            "Main-Class" to "net.typho.big_shot_lib.agent.BigShotLibAgent",
            "Agent-Class" to "net.typho.big_shot_lib.agent.BigShotLibAgent",
            "Can-Redefine-Classes" to "true",
            "Can-Retransform-Classes" to "true"
        )
    }
}