package net.typho.big_shot_lib.common.annotation

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.CONSTRUCTOR)
annotation class OnlyIn(
    val value: Environment
)