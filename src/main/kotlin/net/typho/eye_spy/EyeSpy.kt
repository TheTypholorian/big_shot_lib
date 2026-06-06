package net.typho.eye_spy

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.models.model.DelegatedModel
import net.minecraft.data.models.model.ModelLocationUtils
import net.minecraft.data.models.model.ModelTemplate
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.data.models.model.TextureSlot
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
import java.util.Optional

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
    val creeperLens = lens(id("creeper_lens"), NeoColor.RGB(77, 186, 76), Items.GLASS, Items.GUNPOWDER).end()
    @JvmField
    val endermanLens = lens(id("enderman_lens"), NeoColor.RGB(224, 121, 250), Items.GLASS, Items.ENDER_PEARL).end()
    @JvmField
    val dissipationLens = lens(id("dissipation_lens"), NeoColor.RGB(178, 8, 8), Items.GLASS, Items.QUARTZ).end()
    @JvmField
    val nightVisionLens = lens(id("night_vision_lens"), NeoColor.RGB(124, 178, 71), Items.TINTED_GLASS, Items.GOLDEN_CARROT).end()

    init {
        addClientListener { bus ->
            bus.register(ModelLoadingEvent { out ->
                val spyglassInHandTemplate = ModelTemplate(
                    Optional.of(id("item/spyglass_in_hand")),
                    Optional.empty(),
                    TextureSlot.LAYER1
                )

                out.register(
                    ModelTemplates.TWO_LAYERED_ITEM,
                    id("item/empty_spyglass"),
                    TextureMapping.layered(
                        TextureMapping.getItemTexture(Items.SPYGLASS),
                        id("item/empty_spyglass")
                    )
                )
                out.register(
                    spyglassInHandTemplate,
                    id("item/empty_spyglass_in_hand"),
                    TextureMapping().put(TextureSlot.LAYER1, id("item/empty_spyglass_model"))
                )

                BuiltInRegistries.ITEM.entrySet().forEach { (key, item) ->
                    if (item is LensItem) {
                        out.register(
                            ModelTemplates.TWO_LAYERED_ITEM,
                            item,
                            "_in_spyglass",
                            TextureMapping.layered(
                                TextureMapping.getItemTexture(Items.SPYGLASS),
                                TextureMapping.getItemTexture(item, "_in_spyglass")
                            )
                        )
                        out.register(
                            spyglassInHandTemplate,
                            item,
                            "_in_spyglass_in_hand",
                            TextureMapping().put(TextureSlot.LAYER1, TextureMapping.getItemTexture(item, "_in_spyglass_model"))
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