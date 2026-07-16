package net.typho.big_shot_lib.impl.fabric

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.language.kotlin.KotlinAdapter
import net.fabricmc.loader.api.LanguageAdapter
import net.fabricmc.loader.api.LanguageAdapterException
import net.fabricmc.loader.api.ModContainer
import net.typho.big_shot_lib.api.NeoCommonInitializer
import net.typho.big_shot_lib.client.api.NeoClientInitializer
import net.typho.big_shot_lib.api.event.NeoClientEventBus
import net.typho.big_shot_lib.api.event.NeoEventBus
import kotlin.jvm.java

class BigShotLibLanguageAdapter : LanguageAdapter {
    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> create(
        mod: ModContainer,
        value: String,
        type: Class<T>
    ): T {
        val adapter = KotlinAdapter()

        try {
            return adapter.create(mod, value, type)
        } catch (e: LanguageAdapterException) {
            when (type) {
                ModInitializer::class.java -> {
                    val instance = adapter.create(mod, value, NeoCommonInitializer::class.java)
                    return ModInitializer { instance.onInitialize(NeoEventBus[mod.metadata.id]) } as T
                }
                ClientModInitializer::class.java -> {
                    val instance = adapter.create(mod, value, NeoClientInitializer::class.java)
                    return ClientModInitializer { instance.onInitializeClient(NeoClientEventBus[mod.metadata.id]) } as T
                }
                else -> throw e
            }
        }
    }
}