package net.typho.big_shot_lib.api.util.content

import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.typho.big_shot_lib.api.InternalUtil
import net.typho.big_shot_lib.api.event.NeoEventBus
import net.typho.big_shot_lib.api.event.RegisterEvent
import java.util.function.Supplier

@Suppress("UNCHECKED_CAST")
open class CreativeTabFactory : ContentFactory<CreativeModeTab> {
    override val registry: ResourceKey<Registry<CreativeModeTab>> = Registries.CREATIVE_MODE_TAB
    @JvmField
    protected var registered = false
    @JvmField
    protected val toRegister = arrayListOf<RegisteredObject.Immediate<CreativeModeTab>>()

    override fun begin(key: Identifier): Builder<*> {
        return BuilderImpl(ResourceKey.create(registry, key), this)
    }

    override fun end(bus: NeoEventBus) {
        bus.register(RegisterEvent { out ->
            out.begin(registry) { out ->
                toRegister.forEach { out.register(it) }
            }
        })
    }

    private class BuilderImpl(
        key: ResourceKey<CreativeModeTab>,
        parent: CreativeTabFactory
    ) : Builder<BuilderImpl>(key, parent)

    open class Builder<B : Builder<B>>(
        @JvmField
        val key: ResourceKey<CreativeModeTab>,
        @JvmField
        protected val parent: CreativeTabFactory
    ) : ObjectBuilder<RegisteredObject<CreativeModeTab>> {
        @JvmField
        protected val builder = InternalUtil.INSTANCE.createCreativeTabBuilder()

        init {
            builder.title(Component.translatable(key.location().toLanguageKey("itemGroup")))
        }

        fun title(title: Component): B {
            builder.title(title)
            return this as B
        }

        fun icon(icon: Supplier<ItemStack>): B {
            builder.icon(icon)
            return this as B
        }

        fun items(gen: CreativeModeTab.DisplayItemsGenerator): B {
            builder.displayItems(gen)
            return this as B
        }

        fun alignedRight(): B {
            builder.alignedRight()
            return this as B
        }

        fun hideTitle(): B {
            builder.hideTitle()
            return this as B
        }

        fun noScrollBar(): B {
            builder.noScrollBar()
            return this as B
        }

        fun backgroundTexture(texture: Identifier): B {
            builder.backgroundTexture(texture)
            return this as B
        }

        override fun end(): RegisteredObject<CreativeModeTab> {
            if (parent.registered) {
                throw IllegalStateException("ContentFactory $parent has ended, it cannot receive more entries")
            }

            val obj = RegisteredObject.Immediate(key, builder.build())

            if (parent.toRegister.contains(obj)) {
                throw IllegalArgumentException("Cannot create two creative mode tabs under the same ID $key")
            }

            parent.toRegister.add(obj)

            return obj
        }
    }
}