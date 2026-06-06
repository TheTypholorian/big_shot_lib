package net.typho.eye_spy

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack

data class SpyglassData(
    @JvmField
    val lens: ItemStack,
    @JvmField
    val attachments: List<ItemStack>
) {
    companion object {
        @JvmField
        val CODEC: MapCodec<SpyglassData> = RecordCodecBuilder.mapCodec {
            it.group(
                ItemStack.OPTIONAL_CODEC.optionalFieldOf("lens", ItemStack.EMPTY).forGetter(SpyglassData::lens),
                ItemStack.CODEC.listOf().optionalFieldOf("attachments", listOf()).forGetter(SpyglassData::attachments)
            ).apply(it, ::SpyglassData)
        }
        @JvmField
        val STREAM_CODEC: StreamCodec<in RegistryFriendlyByteBuf, SpyglassData> = StreamCodec.composite(
            ItemStack.OPTIONAL_STREAM_CODEC, SpyglassData::lens,
            ByteBufCodecs.list<RegistryFriendlyByteBuf, ItemStack>().apply(ItemStack.STREAM_CODEC), SpyglassData::attachments,
            ::SpyglassData
        )
        @JvmStatic
        @get:JvmName("getLens")
        val DEFAULT by lazy {
            SpyglassData(ItemStack(EyeSpy.basicLens.get()), listOf())
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SpyglassData) return false

        if (lens != other.lens) return false

        return true
    }

    override fun hashCode(): Int {
        return lens.hashCode()
    }
}
