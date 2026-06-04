package net.typho.big_shot_lib.api.util.content

import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.blockstates.BlockStateGenerator
import net.minecraft.data.models.blockstates.MultiVariantGenerator
import net.minecraft.data.models.blockstates.Variant
import net.minecraft.data.models.blockstates.VariantProperties
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.data.models.model.TextureSlot
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.resources.Identifier
import net.minecraft.tags.ItemTags
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.Items
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.block.SoundType
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.event.BlockModelLoadingEvent
import net.typho.big_shot_lib.api.client.event.NeoClientEventBus
import net.typho.big_shot_lib.api.client.util.BigShotClientEntrypoint
import net.typho.big_shot_lib.api.event.NeoEventBus
import net.typho.big_shot_lib.api.util.BigShotCommonEntrypoint

object ContentTest : BigShotCommonEntrypoint {
    override val modId: String = BigShotApi.MOD_ID

    @JvmStatic
    fun id(path: String): Identifier = Identifier.of(modId, path)

    val loot = LootTableContentFactory()
    val advancements = AdvancementContentFactory()
    val items = ItemContentFactory()
    val blocks = BlockContentFactory(items, loot)

    val testItem = items.begin(id("test_item"))
        .properties {
            stacksTo(31)
            rarity(Rarity.RARE)
            food(FoodProperties.Builder()
                .fast()
                .nutrition(2000)
                .alwaysEdible()
                .saturationModifier(200f)
                .effect(MobEffectInstance(MobEffects.WITHER, 200, 200), 1f)
                .usingConvertsTo { Items.MUD }
                .build())
        }
        .recipe { item ->
            ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, item)
                .unlockedBy("has_mud", RecipeProvider.has(Items.MUD))
                .define('M', Items.MUD)
                .pattern("M M")
                .pattern(" M ")
                .pattern("M M")
        }
        .tags(ItemTags.HORSE_TEMPT_ITEMS, ItemTags.ARROWS)
        .end()


    val testBlock1 = blocks.begin(id("test_block"))
        .properties {
            instabreak()
            ignitedByLava()
        }
        .end()
    val testBlock2 = blocks.begin(id("test_block_b"))
        .properties {
            emissiveRendering { state, getter, pos -> true }
            lightLevel { 15 }
            sound(SoundType.MUD)
        }
        .end()
    val testStairs = blocks.beginStairs(testBlock1, id("test_stairs"))
        .properties {
            sound(SoundType.WOOL)
        }
        .end()

    val testAdvancement = advancements.begin(id("test_advancement"))
        .parent(Identifier.minecraft("adventure/trade"))
        .display(testItem)
        .rewards(AdvancementRewards.Builder.experience(100000))
        .addCriterion("test_item") { InventoryChangeTrigger.TriggerInstance.hasItems(testItem) }
        .end()

    override fun onInitialize(bus: NeoEventBus) {
        blocks.end(bus)
        items.end(bus)
        advancements.end(bus)
        loot.end(bus)
    }

    object Client : BigShotClientEntrypoint {
        override val modId: String = ContentTest.modId

        override fun onInitializeClient(bus: NeoClientEventBus) {
            bus.register(object : BlockModelLoadingEvent {
                override fun loadModels(out: BlockModelLoadingEvent.ModelOutput) {
                    out.register(ModelTemplates.CUBE_ALL, testBlock1) {
                        TextureMapping().put(TextureSlot.ALL, TextureMapping.getBlockTexture(testBlock1.get()))
                    }
                }

                override fun loadStates(out: BlockModelLoadingEvent.StateOutput) {
                    out.register(testBlock1.get()) {
                        MultiVariantGenerator.multiVariant(testBlock1.get(), Variant.variant().with(VariantProperties.MODEL, ModelTemplates.CUBE_ALL.getDefaultModelLocation(testBlock1.get())))
                        // TODO
                        //BlockModelGenerators.createSimpleBlock()
                    }
                }
            })
        }
    }
}