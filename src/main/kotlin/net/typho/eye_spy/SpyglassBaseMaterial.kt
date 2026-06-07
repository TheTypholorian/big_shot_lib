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

data class SpyglassBaseMaterial(
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
        val REGISTRY = hashMapOf<Identifier, SpyglassBaseMaterial>()
        @JvmField
        val CODEC: Codec<SpyglassBaseMaterial> = Identifier.CODEC.xmap(
            { REGISTRY[it] ?: DARK_OAK },
            SpyglassBaseMaterial::location
        )
        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, SpyglassBaseMaterial> = Identifier.STREAM_CODEC.map(
            { REGISTRY[it] ?: DARK_OAK },
            SpyglassBaseMaterial::location
        )

        @JvmStatic
        fun minecraft(name: String, item: Supplier<out Item>) = SpyglassBaseMaterial(Identifier.minecraft(name), EyeSpy.id("item/spyglass_${name}_base"), item)

        @JvmField
        val ACACIA = minecraft("acacia") { Items.ACACIA_PLANKS }
        @JvmField
        val BAMBOO = minecraft("bamboo") { Items.BAMBOO_PLANKS }
        @JvmField
        val BIRCH = minecraft("birch") { Items.BIRCH_PLANKS }
        @JvmField
        val CHERRY = minecraft("cherry") { Items.CHERRY_PLANKS }
        @JvmField
        val CRIMSON = minecraft("crimson") { Items.CRIMSON_PLANKS }
        @JvmField
        val DARK_OAK = minecraft("dark_oak") { Items.DARK_OAK_PLANKS }
        @JvmField
        val JUNGLE = minecraft("jungle") { Items.JUNGLE_PLANKS }
        @JvmField
        val MANGROVE = minecraft("mangrove") { Items.MANGROVE_PLANKS }
        @JvmField
        val OAK = minecraft("oak") { Items.OAK_PLANKS }
        @JvmField
        val WARPED = minecraft("warped") { Items.WARPED_PLANKS }
    }
}
