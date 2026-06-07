package net.typho.eye_spy

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.typho.big_shot_lib.api.util.resource.NamedResource
import java.util.function.Supplier

data class SpyglassTrimMaterial(
    override val location: Identifier,
    @JvmField
    val textureLocation: Identifier,
    @JvmField
    val item: Supplier<out Item>
) : NamedResource {
    init {
        REGISTRY[location] = this
    }

    companion object {
        @JvmField
        val REGISTRY = hashMapOf<Identifier, SpyglassTrimMaterial>()
        @JvmField
        val CODEC: Codec<SpyglassTrimMaterial> = Identifier.CODEC.xmap(
            { REGISTRY[it] ?: GOLD },
            SpyglassTrimMaterial::location
        )
        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, SpyglassTrimMaterial> = Identifier.STREAM_CODEC.map(
            { REGISTRY[it] ?: GOLD },
            SpyglassTrimMaterial::location
        )

        @JvmStatic
        fun minecraft(name: String, item: Supplier<out Item>) = SpyglassTrimMaterial(Identifier.minecraft(name), EyeSpy.id("item/spyglass_${name}_trim"), item)

        @JvmField
        val COPPER = minecraft("copper") { Items.COPPER_INGOT }
        @JvmField
        val GOLD = minecraft("gold") { Items.GOLD_INGOT }
        @JvmField
        val IRON = minecraft("iron") { Items.IRON_INGOT }
    }
}
