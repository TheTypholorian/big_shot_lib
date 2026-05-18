package net.typho.big_shot_lib.api.plugin

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.FIELD)
annotation class Namespace(
    val value: String
)