package net.typho.big_shot_lib.api.ext

import net.minecraft.world.item.Item
import net.typho.big_shot_lib.api.BigShotLib
import net.typho.big_shot_lib.api.content.NeoItem
import net.typho.big_shot_lib.api.plugin.Prefix
import net.typho.big_shot_lib.api.util.Extension

@Prefix(BigShotLib.MOD_ID)
interface ItemExtension : Extension<Item> {
    /**
     * The stored [NeoItem] instance.
     * If there isn't one, a default value is returned, with methods forwarded to the [Item] instance.
     * If you need to know if this item is managed by Big Shot Lib, use [neoItemNullable]
     */
    var neoItem: NeoItem
    /**
     * @see [neoItem]
     */
    var neoItemNullable: NeoItem?
}