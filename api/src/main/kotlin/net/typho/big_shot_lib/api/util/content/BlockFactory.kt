package net.typho.big_shot_lib.api.util.content

import net.minecraft.client.color.block.BlockColor
import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.model.DelegatedModel
import net.minecraft.data.models.model.ModelLocationUtils
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.ButtonBlock
import net.minecraft.world.level.block.DoorBlock
import net.minecraft.world.level.block.FenceBlock
import net.minecraft.world.level.block.FenceGateBlock
import net.minecraft.world.level.block.PressurePlateBlock
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.TrapDoorBlock
import net.minecraft.world.level.block.WallBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.properties.BlockSetType
import net.minecraft.world.level.block.state.properties.WoodType
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.typho.big_shot_lib.api.NeoCommonInitializer
import net.typho.big_shot_lib.api.client.event.ModelLoadingEvent
import net.typho.big_shot_lib.api.client.rendering.util.NeoRenderType
import net.typho.big_shot_lib.api.event.RegisterDynamicTagsEvent
import net.typho.big_shot_lib.api.event.RegisterEvent
import net.typho.big_shot_lib.api.plugin.Environment
import net.typho.big_shot_lib.api.plugin.OnlyIn
import net.typho.big_shot_lib.api.util.platform.PlatformUtil
import net.typho.big_shot_lib.api.util.resource.RegisteredResource
import java.util.function.BiFunction
import java.util.function.Function
import java.util.function.Supplier
import java.util.function.UnaryOperator
import kotlin.collections.addAll

@Suppress("UNCHECKED_CAST")
open class BlockFactory @JvmOverloads constructor(
    mod: NeoCommonInitializer,
    @JvmField
    protected val items: ItemFactory? = null,
    @JvmField
    protected val loot: LootTableFactory? = null
) : ContentFactory<Block> {
    override val registry: ResourceKey<Registry<Block>> = Registries.BLOCK
    @JvmField
    protected var registered = false
    @JvmField
    protected val toRegister = arrayListOf<RegisteredObject<out Block>>()
    @JvmField
    protected val dynamicTags = hashMapOf<TagKey<out Block>, MutableSet<ResourceKey<out Block>>>()
    @JvmField
    @OnlyIn(Environment.CLIENT)
    protected val models = arrayListOf<ModelLoadingEvent>()

    init {
        mod.addListener { bus ->
            bus.register(RegisterEvent { out ->
                out.beginBlocks { out ->
                    registered = true

                    toRegister.forEach { out.register(it) }
                }
            })
            bus.register(RegisterDynamicTagsEvent { out ->
                registered = true

                dynamicTags.forEach { (key, value) -> out.addBlocks(key, *value.toTypedArray()) }
            })
        }
        mod.addClientListener { bus ->
            models.forEach { bus.register(it) }
        }
    }

    override fun begin(key: Identifier): Builder<Block, *> {
        return beginComplex(key) { Block(it) }
            .client {
                it.model { block, textures ->
                    ModelLoadingEvent { out ->
                        val block = block.get()
                        val textures = textures.get()

                        out.register(
                            block,
                            BlockModelGenerators.createSimpleBlock(
                                block,
                                out.register(ModelTemplates.CUBE_ALL, block, textures)
                            )
                        )
                    }
                }
            }
    }

    @JvmOverloads
    open fun beginStairs(key: Identifier, copyState: Supplier<out Block>, copyTextures: Supplier<out Block> = copyState): Builder<StairBlock, *> {
        return beginComplex(key) { StairBlock(copyState.get().defaultBlockState(), it) }
            .client {
                it.textureParent(copyTextures)
                    .model { block, textures ->
                        ModelLoadingEvent { out ->
                            val block = block.get()
                            val textures = textures.get()

                            out.register(
                                block,
                                BlockModelGenerators.createStairs(
                                    block,
                                    out.register(ModelTemplates.STAIRS_INNER, block, textures),
                                    out.register(ModelTemplates.STAIRS_STRAIGHT, block, textures),
                                    out.register(ModelTemplates.STAIRS_OUTER, block, textures)
                                )
                            )
                        }
                    }
            }
            .item {
                it.client {
                    it.model { item ->
                        ModelLoadingEvent { out ->
                            out.registerModelJson(
                                ModelLocationUtils.getModelLocation(item.get()),
                                DelegatedModel(ModelTemplates.STAIRS_STRAIGHT.getDefaultModelLocation(copyTextures.get())).get()
                            )
                        }
                    }
                }
            }
            .tags(BlockTags.STAIRS)
    }

    open fun beginSlab(key: Identifier, fullBlock: Supplier<out Block>): Builder<SlabBlock, *> {
        return beginComplex(key) { SlabBlock(it) }
            .client {
                it.textureParent(fullBlock)
                    .model { block, textures ->
                        ModelLoadingEvent { out ->
                            val block = block.get()
                            val textures = textures.get()

                            out.register(
                                block,
                                BlockModelGenerators.createSlab(
                                    block,
                                    out.register(ModelTemplates.SLAB_BOTTOM, block, textures),
                                    out.register(ModelTemplates.SLAB_TOP, block, textures),
                                    ModelLocationUtils.getModelLocation(fullBlock.get())
                                )
                            )
                        }
                    }
            }
            .item {
                it.client {
                    it.model { item ->
                        ModelLoadingEvent { out ->
                            out.registerModelJson(
                                ModelLocationUtils.getModelLocation(item.get()),
                                DelegatedModel(ModelTemplates.SLAB_BOTTOM.getDefaultModelLocation(item.get().block)).get()
                            )
                        }
                    }
                }
            }
            .tags(BlockTags.SLABS)
    }

    open fun beginDoor(key: Identifier, blockSet: Supplier<BlockSetType>): Builder<DoorBlock, *> {
        return beginComplex(key) { DoorBlock(blockSet.get(), it) }
            .client {
                it.textureMapping { TextureMapping.door(it.get()) }
                    .model { block, textures ->
                        ModelLoadingEvent { out ->
                            val block = block.get()
                            val textures = textures.get()

                            out.register(
                                block,
                                BlockModelGenerators.createDoor(
                                    block,
                                    out.register(ModelTemplates.DOOR_BOTTOM_LEFT, block, textures),
                                    out.register(ModelTemplates.DOOR_BOTTOM_LEFT_OPEN, block, textures),
                                    out.register(ModelTemplates.DOOR_BOTTOM_RIGHT, block, textures),
                                    out.register(ModelTemplates.DOOR_BOTTOM_RIGHT_OPEN, block, textures),
                                    out.register(ModelTemplates.DOOR_TOP_LEFT, block, textures),
                                    out.register(ModelTemplates.DOOR_TOP_LEFT_OPEN, block, textures),
                                    out.register(ModelTemplates.DOOR_TOP_RIGHT, block, textures),
                                    out.register(ModelTemplates.DOOR_TOP_RIGHT_OPEN, block, textures),
                                )
                            )
                        }
                    }
            }
            .item {
                it.client {
                    it.model { item ->
                        ModelLoadingEvent { out ->
                            out.registerModelJson(
                                ModelLocationUtils.getModelLocation(item.get()),
                                DelegatedModel(out.register(ModelTemplates.FLAT_ITEM, item.get().block, TextureMapping.layer0(item.get()))).get()
                            )
                        }
                    }
                }
            }
            .tags(BlockTags.DOORS)
    }

    open fun beginTrapdoor(key: Identifier, blockSet: Supplier<BlockSetType>, rotatable: Boolean): Builder<TrapDoorBlock, *> {
        return beginComplex(key) { TrapDoorBlock(blockSet.get(), it) }
            .client {
                it.textureMapping { TextureMapping.defaultTexture(it.get()) }
                    .model { block, textures ->
                        ModelLoadingEvent { out ->
                            val block = block.get()
                            val textures = textures.get()

                            out.register(
                                block,
                                if (rotatable) {
                                    BlockModelGenerators.createOrientableTrapdoor(
                                        block,
                                        out.register(ModelTemplates.ORIENTABLE_TRAPDOOR_TOP, block, textures),
                                        out.register(ModelTemplates.ORIENTABLE_TRAPDOOR_BOTTOM, block, textures),
                                        out.register(ModelTemplates.ORIENTABLE_TRAPDOOR_OPEN, block, textures)
                                    )
                                } else {
                                    BlockModelGenerators.createTrapdoor(
                                        block,
                                        out.register(ModelTemplates.TRAPDOOR_TOP, block, textures),
                                        out.register(ModelTemplates.TRAPDOOR_BOTTOM, block, textures),
                                        out.register(ModelTemplates.TRAPDOOR_OPEN, block, textures)
                                    )
                                }
                            )
                        }
                    }
            }
            .item {
                it.client {
                    it.model { item ->
                        ModelLoadingEvent { out ->
                            out.registerModelJson(
                                ModelLocationUtils.getModelLocation(item.get()),
                                if (rotatable) {
                                    DelegatedModel(ModelTemplates.ORIENTABLE_TRAPDOOR_BOTTOM.getDefaultModelLocation(item.get().block)).get()
                                } else {
                                    DelegatedModel(ModelTemplates.TRAPDOOR_BOTTOM.getDefaultModelLocation(item.get().block)).get()
                                }
                            )
                        }
                    }
                }
            }
            .tags(BlockTags.TRAPDOORS)
    }

    @JvmOverloads
    open fun beginPressurePlate(key: Identifier, blockSet: Supplier<BlockSetType>, copyTextures: Supplier<out Block>? = null): Builder<PressurePlateBlock, *> {
        return beginComplex(key) { PressurePlateBlock(blockSet.get(), it) }
            .client {
                it.textureParent(copyTextures)
                    .model { block, textures ->
                        ModelLoadingEvent { out ->
                            val block = block.get()
                            val textures = textures.get()

                            out.register(
                                block,
                                BlockModelGenerators.createPressurePlate(
                                    block,
                                    out.register(ModelTemplates.PRESSURE_PLATE_UP, block, textures),
                                    out.register(ModelTemplates.PRESSURE_PLATE_DOWN, block, textures)
                                )
                            )
                        }
                    }
            }
            .item {
                it.client {
                    it.model { item ->
                        ModelLoadingEvent { out ->
                            out.registerModelJson(
                                ModelLocationUtils.getModelLocation(item.get()),
                                DelegatedModel(ModelTemplates.PRESSURE_PLATE_UP.getDefaultModelLocation(item.get().block)).get()
                            )
                        }
                    }
                }
            }
            .tags(BlockTags.PRESSURE_PLATES)
    }

    @JvmOverloads
    open fun beginWall(key: Identifier, copyTextures: Supplier<out Block>? = null): Builder<WallBlock, *> {
        return beginComplex(key) { WallBlock(it) }
            .client {
                it.textureParent(copyTextures)
                    .model { block, textures ->
                        ModelLoadingEvent { out ->
                            val block = block.get()
                            val textures = textures.get()

                            out.register(
                                block,
                                BlockModelGenerators.createWall(
                                    block,
                                    out.register(ModelTemplates.WALL_POST, block, textures),
                                    out.register(ModelTemplates.WALL_LOW_SIDE, block, textures),
                                    out.register(ModelTemplates.WALL_TALL_SIDE, block, textures)
                                )
                            )

                            out.register(ModelTemplates.WALL_INVENTORY, block, textures)
                        }
                    }
            }
            .item {
                it.client {
                    it.model { item ->
                        ModelLoadingEvent { out ->
                            out.registerModelJson(
                                ModelLocationUtils.getModelLocation(item.get()),
                                DelegatedModel(ModelTemplates.WALL_INVENTORY.getDefaultModelLocation(item.get().block)).get()
                            )
                        }
                    }
                }
            }
            .tags(BlockTags.WALLS)
    }

    @JvmOverloads
    open fun beginFence(key: Identifier, copyTextures: Supplier<out Block>? = null): Builder<FenceBlock, *> {
        return beginComplex(key) { FenceBlock(it) }
            .client {
                it.textureParent(copyTextures)
                    .model { block, textures ->
                        ModelLoadingEvent { out ->
                            val block = block.get()
                            val textures = textures.get()

                            out.register(
                                block,
                                BlockModelGenerators.createFence(
                                    block,
                                    out.register(ModelTemplates.FENCE_POST, block, textures),
                                    out.register(ModelTemplates.FENCE_SIDE, block, textures)
                                )
                            )

                            out.register(ModelTemplates.FENCE_INVENTORY, block, textures)
                        }
                    }
            }
            .item {
                it.client {
                    it.model { item ->
                        ModelLoadingEvent { out ->
                            out.registerModelJson(
                                ModelLocationUtils.getModelLocation(item.get()),
                                DelegatedModel(ModelTemplates.FENCE_INVENTORY.getDefaultModelLocation(item.get().block)).get()
                            )
                        }
                    }
                }
            }
            .tags(BlockTags.FENCES)
    }

    @JvmOverloads
    open fun beginFenceGate(key: Identifier, woodType: Supplier<WoodType>, uvLock: Boolean = true, copyTextures: Supplier<out Block>? = null): Builder<FenceGateBlock, *> {
        return beginComplex(key) { FenceGateBlock(woodType.get(), it) }
            .client {
                it.textureParent(copyTextures)
                    .model { block, textures ->
                        ModelLoadingEvent { out ->
                            val block = block.get()
                            val textures = textures.get()

                            out.register(
                                block,
                                BlockModelGenerators.createFenceGate(
                                    block,
                                    out.register(ModelTemplates.FENCE_GATE_OPEN, block, textures),
                                    out.register(ModelTemplates.FENCE_GATE_CLOSED, block, textures),
                                    out.register(ModelTemplates.FENCE_GATE_WALL_OPEN, block, textures),
                                    out.register(ModelTemplates.FENCE_GATE_WALL_CLOSED, block, textures),
                                    uvLock
                                )
                            )
                        }
                    }
            }
            .tags(BlockTags.FENCE_GATES)
    }

    @JvmOverloads
    open fun beginButton(key: Identifier, pressDuration: Int, setType: Supplier<BlockSetType>, copyTextures: Supplier<out Block>? = null): Builder<ButtonBlock, *> {
        return beginComplex(key) { ButtonBlock(setType.get(), pressDuration, it) }
            .client {
                it.textureParent(copyTextures)
                    .model { block, textures ->
                        ModelLoadingEvent { out ->
                            val block = block.get()
                            val textures = textures.get()

                            out.register(
                                block,
                                BlockModelGenerators.createButton(
                                    block,
                                    out.register(ModelTemplates.BUTTON, block, textures),
                                    out.register(ModelTemplates.BUTTON_PRESSED, block, textures)
                                )
                            )

                            out.register(ModelTemplates.BUTTON_INVENTORY, block, textures)
                        }
                    }
            }
            .item {
                it.client {
                    it.model { item ->
                        ModelLoadingEvent { out ->
                            out.registerModelJson(
                                ModelLocationUtils.getModelLocation(item.get()),
                                DelegatedModel(ModelTemplates.BUTTON_INVENTORY.getDefaultModelLocation(item.get().block)).get()
                            )
                        }
                    }
                }
            }
            .tags(BlockTags.BUTTONS)
    }

    open fun <V : Block> beginComplex(key: Identifier, constructor: (properties: BlockBehaviour.Properties) -> V): Builder<V, *> {
        return BuilderImpl(ResourceKey.create(registry, key) as ResourceKey<V>, constructor, this)
    }

    private class ClientInfoImpl<T : Block>(
        parent: BlockFactory
    ) : ClientInfo<T, ClientInfoImpl<T>>(parent)

    open class ClientInfo<T : Block, B : ClientInfo<T, B>>(
        @JvmField
        protected val parent: BlockFactory
    ) {
        @JvmField
        protected var renderType: NeoRenderType = NeoRenderType.BUILTINS.solid
        @JvmField
        protected var model: Function<Supplier<T>, ModelLoadingEvent>? = null
        @JvmField
        protected var textureParent: Supplier<out Block>? = null
        @JvmField
        protected var textureMapping: Function<Supplier<out Block>, TextureMapping> = Function { TextureMapping.cube(it.get()) }

        fun renderType(renderType: NeoRenderType): B {
            this.renderType = renderType
            return this as B
        }

        fun model(model: BiFunction<Supplier<out Block>, Supplier<TextureMapping>, ModelLoadingEvent>): B {
            this.model = Function { block -> model.apply(block, Supplier { textureMapping.apply(textureParent ?: block) }) }
            return this as B
        }

        fun textureParent(parent: Supplier<out Block>?): B {
            this.textureParent = parent
            return this as B
        }

        fun textureMapping(textures: Function<Supplier<out Block>, TextureMapping>): B {
            this.textureMapping = textures
            return this as B
        }

        fun end(block: RegisteredObject<T>) {
            model?.apply(block)?.let { parent.models.add(it) }
        }
    }

    private class BuilderImpl<T : Block>(
        key: ResourceKey<T>,
        constructor: (properties: BlockBehaviour.Properties) -> T,
        parent: BlockFactory
    ) : Builder<T, BuilderImpl<T>>(key, constructor, parent)

    open class Builder<T : Block, B : Builder<T, B>>(
        @JvmField
        val key: ResourceKey<T>,
        @JvmField
        protected val constructor: (properties: BlockBehaviour.Properties) -> T,
        @JvmField
        protected val parent: BlockFactory
    ) : ObjectBuilder<RegisteredObject<T>> {
        @JvmField
        protected var properties: BlockBehaviour.Properties = BlockBehaviour.Properties.of()
        @JvmField
        protected var clientInfo: ClientInfo<T, *>? = null
        // TODO block item
        // TODO block entity
        @JvmField
        protected var color: BlockColor? = null
        // TODO model
        @JvmField
        protected var lootTable: BiFunction<T, BlockItem, RegisteredObject<LootTable>>? = parent.loot?.let { loot ->
            BiFunction { block, item ->
                loot.begin(block.lootTable.location())
                    .withPool {
                        LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1f))
                            .`when`(ExplosionCondition.survivesExplosion())
                            .add(LootItem.lootTableItem(item))
                    }
                    .end()
            }
        }
        // TODO client side extensions
        @JvmField
        protected val tags: MutableList<TagKey<Block>> = arrayListOf()
        @JvmField
        protected var item: ItemFactory.Builder<BlockItem, *>? = parent.items?.beginBlockItem(key.location()) { registered!!.get() }
        @JvmField
        protected var registered: RegisteredObject<T>? = null

        fun properties(properties: UnaryOperator<BlockBehaviour.Properties>): B {
            this.properties = properties.apply(this.properties)
            return this as B
        }

        fun client(info: UnaryOperator<ClientInfo<T, *>>): B {
            if (PlatformUtil.INSTANCE.isClient()) {
                clientInfo = info.apply(clientInfo ?: ClientInfoImpl(parent))
            }

            return this as B
        }

        fun color(color: BlockColor): B {
            this.color = color
            return this as B
        }

        fun tags(vararg tags: TagKey<Block>): B {
            this.tags.addAll(tags)
            return this as B
        }

        fun noLoot(): B {
            lootTable = null
            return this as B
        }

        fun item(builder: UnaryOperator<ItemFactory.Builder<BlockItem, *>>): B {
            item = item?.let(builder::apply)

            return this as B
        }

        fun noItem(): B {
            item = null
            return this as B
        }

        fun tabs(vararg tabs: ResourceKey<CreativeModeTab>): B {
            item { it.tabs(*tabs) }
            return this as B
        }

        fun tabs(vararg tabs: RegisteredResource<CreativeModeTab>): B {
            item { it.tabs(*tabs) }
            return this as B
        }

        fun drops(builder: UnaryOperator<LootTableFactory.Builder<*>>): B {
            lootTable = BiFunction { block, item ->
                (parent.loot ?: throw UnsupportedOperationException("Must pass a LootTableContentFactory to the BlockContentFactory to be able to call Builder.drops()")).begin(block.lootTable.location())
                    .let(builder::apply)
                    .end()
            }

            return this as B
        }

        override fun end(): RegisteredObject<T> {
            if (parent.registered) {
                throw IllegalStateException("ContentFactory $parent has ended, it cannot receive more entries")
            }

            val block = RegisteredObject.Late(key) { constructor(properties) }

            registered = block

            if (parent.toRegister.contains(block)) {
                throw IllegalArgumentException("Cannot create two blocks under the same ID $key")
            }

            parent.toRegister.add(block)

            for (tag in tags) {
                parent.dynamicTags.computeIfAbsent(tag) { hashSetOf() }.add(key)
            }

            item?.end()
            clientInfo?.end(block)

            return block
        }
    }
}