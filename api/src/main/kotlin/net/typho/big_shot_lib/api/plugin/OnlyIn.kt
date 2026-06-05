package net.typho.big_shot_lib.api.plugin

@Target(AnnotationTarget.CLASS, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION, AnnotationTarget.CONSTRUCTOR)
annotation class OnlyIn(
    val value: Environment
)