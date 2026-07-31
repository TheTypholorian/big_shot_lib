plugins {
    id("java")
}

group = "net.typho.big_shot_lib"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
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