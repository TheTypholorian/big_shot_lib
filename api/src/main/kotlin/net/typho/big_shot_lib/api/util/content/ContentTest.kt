package net.typho.big_shot_lib.api.util.content

import net.minecraft.resources.Identifier
import net.minecraft.world.item.Rarity
import net.typho.big_shot_lib.api.event.NeoEventBus
import net.typho.big_shot_lib.api.util.BigShotCommonEntrypoint

object ContentTest : BigShotCommonEntrypoint {
    override val modId: String = "test"

    @JvmStatic
    fun id(path: String): Identifier = Identifier.of(modId, path)

    val items = ItemContentFactory()

    val testItem = items.begin(id("test_item"))
        .properties { it.stacksTo(31).rarity(Rarity.RARE) }
        .end()

    val blocks = BlockContentFactory()

    val testBlock1 = blocks.begin(id("test_block"))
        .properties { it.instabreak().ignitedByLava() }
        .end()
    val testBlock2 = blocks.begin(id("test_block_b"))
        .properties { it.air() }
        .end()

    override fun onInitialize(bus: NeoEventBus) {
        items.end(bus)
        blocks.end(bus)
    }
}