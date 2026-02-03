package net.typho.big_shot_lib.api

import java.util.*

object Services {
    fun <T> load(cls: Class<T>): T {
        return ServiceLoader.load(cls)
            .findFirst()
            .orElseThrow()
    }
}