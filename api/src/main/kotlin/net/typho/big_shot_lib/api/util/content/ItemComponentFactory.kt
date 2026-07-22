package net.typho.big_shot_lib.api.util.content

import com.mojang.serialization.Codec
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.typho.big_shot_lib.api.NeoCommonInitializer

@Suppress("UNCHECKED_CAST")
open class ItemComponentFactory(
    mod: NeoCommonInitializer
) : ContentFactory<DataComponentType<*>> {
    override val registry: ResourceKey<Registry<DataComponentType<*>>> = Registries.DATA_COMPONENT_TYPE
    @JvmField
    protected var registered = false
    @JvmField
    protected val toRegister = hashSetOf<RegisteredObject.Immediate<DataComponentType<*>>>()

    init {
        mod.addListener { bus ->
            bus.register(RegisterEvent { out ->
                out.begin(registry) { out ->
                    registered = true
                    toRegister.forEach { out.register(it) }
                }
            })
        }
    }

    open fun <T : Any> begin(key: Identifier): Builder<T, *> {
        return BuilderImpl(ResourceKey.create(registry, key) as ResourceKey<DataComponentType<T>>, this)
    }

    private class BuilderImpl<T : Any>(
        key: ResourceKey<DataComponentType<T>>,
        parent: ItemComponentFactory
    ) : Builder<T, BuilderImpl<T>>(key, parent)

    open class Builder<T : Any, B : Builder<T, B>>(
        @JvmField
        val key: ResourceKey<DataComponentType<T>>,
        @JvmField
        protected val parent: ItemComponentFactory
    ) : ObjectBuilder<RegisteredObject<DataComponentType<T>>> {
        @JvmField
        protected val builder: DataComponentType.Builder<T> = DataComponentType.builder<T>()

        fun codec(codec: Codec<T>): B {
            builder.persistent(codec)
            return this as B
        }

        fun streamCodec(codec: StreamCodec<in RegistryFriendlyByteBuf, T>): B {
            builder.networkSynchronized(codec)
            return this as B
        }

        fun cacheEncoding(): B {
            builder.cacheEncoding()
            return this as B
        }

        override fun end(): RegisteredObject<DataComponentType<T>> {
            if (parent.registered) {
                throw IllegalStateException("ContentFactory $parent has ended, it cannot receive more entries")
            }

            val obj = RegisteredObject.Immediate(key, builder.build())

            if (!parent.toRegister.add(obj as RegisteredObject.Immediate<DataComponentType<*>>)) {
                throw IllegalArgumentException("Cannot create two data component types under the same ID $key")
            }

            return obj
        }
    }
}