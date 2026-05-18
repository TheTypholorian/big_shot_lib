package net.typho.big_shot_lib.api.plugin

@Target(AnnotationTarget.CLASS)
annotation class OnlyIn(
    val value: Environment
)