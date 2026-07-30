package net.typho.big_shot_lib.common.annotation

import org.jetbrains.annotations.ApiStatus

/**
 * **INTERNAL, DO NOT USE**
 *
 * Dictates whether a project class is using tweaked dependencies (value = false), or using the original dependencies (value = true)
 */
@ApiStatus.Internal
@Target(AnnotationTarget.CLASS)
annotation class IsRuntimeReady(
    val value: Boolean
)