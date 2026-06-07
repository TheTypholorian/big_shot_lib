package net.typho.eye_spy

import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.data.models.model.ModelTemplate
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.data.models.model.TextureSlot
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.typho.big_shot_lib.api.BigShotLib.toShortString
import net.typho.big_shot_lib.api.NeoCommonInitializer
import net.typho.big_shot_lib.api.client.event.ModelLoadingEvent
import net.typho.big_shot_lib.api.event.ModifyDefaultItemComponentsEvent
import net.typho.big_shot_lib.api.event.NeoEventBus
import net.typho.big_shot_lib.api.event.RegisterDynamicRecipesEvent
import net.typho.big_shot_lib.api.event.RemoveAdvancementsEvent
import net.typho.big_shot_lib.api.event.RemoveRecipesEvent
import net.typho.big_shot_lib.api.util.NeoColor
import net.typho.big_shot_lib.api.util.content.ItemComponentFactory
import net.typho.big_shot_lib.api.util.content.ItemFactory
import net.typho.big_shot_lib.api.util.content.RecipeTypeFactory
import java.util.Optional

object EyeSpy : NeoCommonInitializer {
    override val modId: String = "big_shot_lib" // TODO

    @JvmStatic
    fun id(path: String): Identifier = Identifier.of(modId, path)

    const val MAX_ATTACHMENTS = 2

    @JvmField
    val items = ItemFactory(this)
    @JvmField
    val itemComponents = ItemComponentFactory(this)
    @JvmField
    val recipes = RecipeTypeFactory(this)

    @JvmField
    val spyglassRecipe = recipes.create(id("spyglass_recipe"), SpyglassRecipe.serializer)

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
                    .unlockedBy("has_${BuiltInRegistries.ITEM.getKey(centerItem.asItem()).toShortString('_')}", RecipeProvider.has(centerItem))
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
    // TODO spelunker lens?

    @JvmStatic
    fun attachment(id: Identifier): ItemFactory.Builder<AttachmentItem, *> {
        return items.beginComplex(id) { AttachmentItem(it) }
            .properties { properties ->
                properties.stacksTo(1)
            }
    }

    override fun onInitialize(bus: NeoEventBus) {
        bus.register(ModifyDefaultItemComponentsEvent { out ->
            out.modify(Items.SPYGLASS) { out ->
                out.set(spyglassDataComponent.get(), SpyglassData.DEFAULT)
            }
        })

        val recipeId = Identifier.minecraft("spyglass")
        val advancementId = recipeId.withPrefix("recipes/tools/")
        bus.register(RemoveAdvancementsEvent { advancement ->
            advancement.id == advancementId
        })
        bus.register(RemoveRecipesEvent { recipe ->
            recipe.id == recipeId
        })
        bus.register(RegisterDynamicRecipesEvent { out, registries ->
            out.register { out ->
                out.accept(
                    recipeId,
                    SpyglassRecipe,
                    out.advancement()
                        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
                        .rewards(AdvancementRewards.Builder.recipe(recipeId))
                        .addCriterion("has_amethyst", RecipeProvider.has(Items.AMETHYST_SHARD))
                        .build(advancementId)
                )
            }
        })
    }
}