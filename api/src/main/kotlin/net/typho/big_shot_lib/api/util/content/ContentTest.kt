package net.typho.big_shot_lib.api.util.content

import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.critereon.InventoryChangeTrigger
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
    val blockSetTypes = BlockSetTypeContentFactory()
    val creativeTabs = CreativeTabContentFactory()

    val tab = creativeTabs.begin(id("tab"))
        .end()

    val testItem = items.begin(id("test_item"))
        .properties {
            it.stacksTo(99)
                .rarity(Rarity.RARE)
                .food(FoodProperties.Builder()
                    .fast()
                    .nutrition(2000)
                    .alwaysEdible()
                    .saturationModifier(200f)
                    .effect(MobEffectInstance(MobEffects.WITHER, 200, 200), 1f)
                    .usingConvertsTo { Items.MUD }
                    .build())
        }
        .recipe {
            ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, it)
                .unlockedBy("has_mud", RecipeProvider.has(Items.MUD))
                .define('M', Items.MUD)
                .pattern("M M")
                .pattern(" M ")
                .pattern("M M")
        }
        .tags(ItemTags.HORSE_TEMPT_ITEMS, ItemTags.ARROWS)
        .end()

    val testBlockSetType = blockSetTypes.begin(id("test_block_set"))
        .end()

    val testBlock1 = blocks.begin(id("test_block"))
        .properties {
            it.instabreak()
                .ignitedByLava()
        }
        .item {
            it.properties {
                it.stacksTo(99)
                    .rarity(Rarity.RARE)
            }
        }
        .tabs(tab)
        .end()
    val testBlock2 = blocks.begin(id("test_block_b"))
        .properties {
            it.emissiveRendering { state, getter, pos -> true }
                .lightLevel { 15 }
                .sound(SoundType.MUD)
        }
        .tabs(tab)
        .end()
    val testStairs = blocks.beginStairs(id("test_stairs"), testBlock1).tabs(tab).end()
    val testSlab = blocks.beginSlab(id("test_slab"), testBlock1).tabs(tab).end()
    val testDoor = blocks.beginDoor(id("test_door"), testBlockSetType).tabs(tab).end()
    val testTrapdoor = blocks.beginTrapdoor(id("test_trapdoor"), testBlockSetType, true).tabs(tab).end()
    val testPressurePlate = blocks.beginPressurePlate(id("test_pressure_plate"), testBlockSetType, testBlock1).tabs(tab).end()
    val testWall = blocks.beginWall(id("test_wall"), testBlock1).tabs(tab).end()
    val testFence = blocks.beginFence(id("test_fence"), testBlock1).tabs(tab).end()
    // TODO fence gate
    val testButton = blocks.beginButton(id("test_button"), 50, testBlockSetType, testBlock1).tabs(tab).end()

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
        blockSetTypes.end(bus)
        creativeTabs.end(bus)
    }

    object Client : BigShotClientEntrypoint {
        override val modId: String = ContentTest.modId

        override fun onInitializeClient(bus: NeoClientEventBus) {
            blocks.endClient(bus)
            items.endClient(bus)
            advancements.endClient(bus)
            loot.endClient(bus)
            blockSetTypes.endClient(bus)
        }
    }
}