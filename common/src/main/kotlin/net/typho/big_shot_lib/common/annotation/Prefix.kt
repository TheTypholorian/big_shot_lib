package net.typho.big_shot_lib.common.annotation

import kotlin.reflect.KClass

/**
 * Adds a prefix to field and method names post-compile, which is useful for injected interfaces to prevent compatibility problems.
 * The prefix should be your mod id, since that's already a unique string.
 * You can also put the annotation on a class to apply to all fields and methods inside that class.
 *
 * Notes:
 * - Given the prefix `abc` and the name `def`, the new name will be `abc$def`.
 * - This annotation is compile-time, and only affects the single class/field/method instance you put it on (it doesn't deal with inheritance).
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
annotation class Prefix(
    val value: String
)