package net.typho.eye_spy

import net.minecraft.world.item.Item
import net.typho.big_shot_lib.api.util.NeoColor

open class LensItem(
    properties: Properties,
    @JvmField
    val color: NeoColor?
) : Item(properties)