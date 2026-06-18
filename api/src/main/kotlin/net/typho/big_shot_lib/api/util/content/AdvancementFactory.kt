package net.typho.big_shot_lib.api.util.content

import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.AdvancementType
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.DisplayInfo
import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.typho.big_shot_lib.api.NeoCommonInitializer

@Suppress("UNCHECKED_CAST")
open class AdvancementFactory(
    mod: NeoCommonInitializer
) : ContentFactory<Advancement> {
    override val registry: ResourceKey<Registry<Advancement>> = Registries.ADVANCEMENT
    @JvmField
    protected var registered = false
    @JvmField
    protected val toRegister = hashSetOf<RegisteredObject.Late<Advancement>>()

    init {
        TODO("advancements")
        mod.addListener { bus ->
            /*
            bus.register(RegisterDynamicAdvancementsEvent { out, registries ->
                registered = true

                toRegister.forEach {
                    val value = it.constructor()
                    it.value = value
                    out.register(AdvancementHolder(it.location, value))
                }
            })
             */
        }
    }

    open fun begin(key: Identifier): Builder<*> {
        return BuilderImpl(ResourceKey.create(registry, key), this)
    }

    private class BuilderImpl(
        key: ResourceKey<Advancement>,
        parent: AdvancementFactory
    ) : Builder<BuilderImpl>(key, parent)

    open class Builder<B : Builder<B>>(
        @JvmField
        val key: ResourceKey<Advancement>,
        @JvmField
        protected val parent: AdvancementFactory
    ) : ObjectBuilder<RegisteredObject<Advancement>> {
        @JvmField
        protected val mutators = arrayListOf<(builder: Advancement.Builder) -> Advancement.Builder>()

        fun parent(parent: () -> AdvancementHolder): B {
            mutators.add { it.parent(parent()) }
            return this as B
        }

        @Suppress("DEPRECATION", "REMOVAL")
        fun parent(parent: Identifier): B {
            mutators.add { it.parent(parent) }
            return this as B
        }

        @JvmOverloads
        fun display(
            icon: ItemLike,
            title: Component = Component.translatable(key.identifier().toLanguageKey("advancements", "title")),
            description: Component = Component.translatable(key.identifier().toLanguageKey("advancements", "description")),
            background: Identifier? = null,
            type: AdvancementType = AdvancementType.TASK,
            showToast: Boolean = true,
            announceChat: Boolean = true,
            hidden: Boolean = false
        ): B {
            mutators.add { it.display(icon, title, description, background, type, showToast, announceChat, hidden) }
            return this as B
        }

        @JvmOverloads
        fun display(
            icon: RegisteredObject<out Item>,
            title: Component = Component.translatable(key.identifier().toLanguageKey("advancements", "title")),
            description: Component = Component.translatable(key.identifier().toLanguageKey("advancements", "description")),
            background: Identifier? = null,
            type: AdvancementType = AdvancementType.TASK,
            showToast: Boolean = true,
            announceChat: Boolean = true,
            hidden: Boolean = false
        ): B {
            mutators.add { it.display(icon, title, description, background, type, showToast, announceChat, hidden) }
            return this as B
        }

        fun displayInfo(display: () -> DisplayInfo): B {
            mutators.add { it.display(display()) }
            return this as B
        }

        fun rewards(rewardsBuilder: AdvancementRewards.Builder): B {
            mutators.add { it.rewards(rewardsBuilder) }
            return this as B
        }

        fun rewards(rewards: AdvancementRewards): B {
            mutators.add { it.rewards(rewards) }
            return this as B
        }

        fun requirements(requirementsStrategy: AdvancementRequirements.Strategy): B {
            mutators.add { it.requirements(requirementsStrategy) }
            return this as B
        }

        fun requirements(requirements: AdvancementRequirements): B {
            mutators.add { it.requirements(requirements) }
            return this as B
        }

        fun sendsTelemetryEvent(): B {
            mutators.add { it.sendsTelemetryEvent() }
            return this as B
        }

        fun addCriterion(
            key: String,
            criterion: () -> Criterion<*>
        ): B {
            mutators.add { it.addCriterion(key, criterion()) }
            return this as B
        }

        override fun end(): RegisteredObject<Advancement> {
            if (parent.registered) {
                throw IllegalStateException("ContentFactory $parent has ended, it cannot receive more entries")
            }

            val obj = RegisteredObject.Late(key) { mutators.fold(Advancement.Builder.advancement()) { builder, mutator -> mutator(builder) }.build(key.identifier()).value }

            if (!parent.toRegister.add(obj)) {
                throw IllegalArgumentException("Cannot create two advancements under the same ID $key")
            }

            return obj
        }
    }
}