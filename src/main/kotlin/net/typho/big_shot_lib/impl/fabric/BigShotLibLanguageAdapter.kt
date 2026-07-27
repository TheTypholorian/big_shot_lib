package net.typho.big_shot_lib.impl.fabric

//? fabric {
/*import kotlin.jvm.java

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
*///? }