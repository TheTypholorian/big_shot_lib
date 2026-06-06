package net.typho.eye_spy

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.typho.big_shot_lib.api.NeoCommonInitializer
import net.typho.big_shot_lib.api.client.event.ModelLoadingEvent
import net.typho.big_shot_lib.api.event.ModifyDefaultItemComponentsEvent
import net.typho.big_shot_lib.api.event.NeoEventBus
import net.typho.big_shot_lib.api.util.NeoColor
import net.typho.big_shot_lib.api.util.content.ItemComponentFactory
import net.typho.big_shot_lib.api.util.content.ItemFactory

object EyeSpy : NeoCommonInitializer {
    override val modId: String = "big_shot_lib" // TODO

    @JvmStatic
    fun id(path: String): Identifier = Identifier.of(modId, path)

    const val SOUND_DISTANCE_MULTIPLIER = 0.25f

    @JvmField
    val items = ItemFactory(this)
    @JvmField
    val itemComponents = ItemComponentFactory(this)

    @JvmField
    val spyglassDataComponent = itemComponents.begin<SpyglassData>(id("spyglass_data"))
        .codec(SpyglassData.CODEC.codec())
        .streamCodec(SpyglassData.STREAM_CODEC)
        .cacheEncoding()
        .end()

    @JvmStatic
    fun lens(id: Identifier, color: NeoColor?): ItemFactory.Builder<LensItem, *> {
        return items.beginComplex(id) { LensItem(it, color) }
            .properties { properties ->
                properties.stacksTo(1)
            }
    }

    @JvmStatic
    fun lens(id: Identifier, color: NeoColor?, borderItem: ItemLike, centerItem: ItemLike): ItemFactory.Builder<LensItem, *> {
        return lens(id, color)
            .recipe { item ->
                ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, item)
                    .unlockedBy("has_${BuiltInRegistries.ITEM.getKey(centerItem.asItem())}", RecipeProvider.has(centerItem))
                    .define('C', centerItem)
                    .define('B', borderItem)
                    .pattern(" B ")
                    .pattern("BCB")
                    .pattern(" B ")
            }
    }

    @JvmField
    val basicLens = lens(id("basic_lens"), null).end()
    @JvmField
    val dissipationLens = lens(id("dissipation_lens"), NeoColor.RGB(229, 114, 200), Items.GLASS, Items.QUARTZ).end()
    @JvmField
    val hydroVisionLens = lens(id("hydro_vision_lens"), NeoColor.RGB(70, 140, 175), Items.TINTED_GLASS, Items.SEA_PICKLE).end()
    @JvmField
    val lightVisionLens = lens(id("light_vision_lens"), NeoColor.RGB(249, 212, 156), Items.GLASS, Items.GLOWSTONE_DUST).end()
    @JvmField
    val nightVisionLens = lens(id("night_vision_lens"), NeoColor.RGB(124, 178, 71), Items.TINTED_GLASS, Items.GOLDEN_CARROT).end()
    @JvmField
    val pyroVisionLens = lens(id("pyro_vision_lens"), NeoColor.RGB(230, 152, 54), Items.TINTED_GLASS, Items.GLOWSTONE_DUST).end()
    @JvmField
    val thermalLens = lens(id("thermal_lens"), NeoColor.RGB(255, 174, 0), Items.GLASS, Items.MAGMA_CREAM).end()

    init {
        addClientListener { bus ->
            bus.register(ModelLoadingEvent { out ->
                out.register(
                    ModelTemplates.TWO_LAYERED_ITEM,
                    id("item/empty_spyglass"),
                    TextureMapping.layered(
                        TextureMapping.getItemTexture(Items.SPYGLASS),
                        id("item/empty_spyglass")
                    )
                )

                BuiltInRegistries.ITEM.entrySet().forEach { (key, item) ->
                    if (item is LensItem && item != basicLens.get()) {
                        out.register(
                            ModelTemplates.TWO_LAYERED_ITEM,
                            item,
                            "_in_spyglass",
                            TextureMapping.layered(
                                TextureMapping.getItemTexture(Items.SPYGLASS),
                                TextureMapping.getItemTexture(item, "_in_spyglass")
                            )
                        )
                    }
                }
            })
        }
    }

    override fun onInitialize(bus: NeoEventBus) {
        bus.register(ModifyDefaultItemComponentsEvent { out ->
            out.modify(Items.SPYGLASS) { out ->
                out.set(spyglassDataComponent.get(), SpyglassData.DEFAULT)
            }
        })
    }
}