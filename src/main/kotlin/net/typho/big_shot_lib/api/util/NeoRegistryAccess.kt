package net.typho.big_shot_lib.api.util

import net.typho.big_shot_lib.api.util.resources.NeoResourceKey

interface NeoRegistryAccess {
    fun <T> registry(key: NeoResourceKey<T>): NeoRegistry<T>?
}