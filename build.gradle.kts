import org.gradle.kotlin.dsl.libs

plugins {
    alias(libs.plugins.loom) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    id("co.uzzu.dotenv.gradle") version "4.0.0"
}