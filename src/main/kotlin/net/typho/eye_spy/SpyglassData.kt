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
    val attachments: List<ItemStack>,
    @JvmField
    val base: SpyglassBaseMaterial,
    @JvmField
    val trim: SpyglassTrimMaterial,
) {
    constructor(
        from: SpyglassData?,
        lens: ItemStack
    ) : this(lens, from?.attachments ?: listOf(), from?.base ?: SpyglassBaseMaterial.DARK_OAK, from?.trim ?: SpyglassTrimMaterial.GOLD)

    constructor(
        from: SpyglassData?,
        attachments: List<ItemStack>
    ) : this(from?.lens ?: ItemStack.EMPTY, attachments, from?.base ?: SpyglassBaseMaterial.DARK_OAK, from?.trim ?: SpyglassTrimMaterial.GOLD)

    constructor(
        from: SpyglassData?,
        base: SpyglassBaseMaterial
    ) : this(from?.lens ?: ItemStack.EMPTY, from?.attachments ?: listOf(), base, from?.trim ?: SpyglassTrimMaterial.GOLD)

    constructor(
        from: SpyglassData?,
        trim: SpyglassTrimMaterial
    ) : this(from?.lens ?: ItemStack.EMPTY, from?.attachments ?: listOf(), from?.base ?: SpyglassBaseMaterial.DARK_OAK, trim)

    companion object {
        @JvmField
        val CODEC: MapCodec<SpyglassData> = RecordCodecBuilder.mapCodec {
            it.group(
                ItemStack.OPTIONAL_CODEC.optionalFieldOf("lens", ItemStack.EMPTY).forGetter(SpyglassData::lens),
                ItemStack.CODEC.listOf().optionalFieldOf("attachments", listOf()).forGetter(SpyglassData::attachments),
                SpyglassBaseMaterial.CODEC.optionalFieldOf("base", SpyglassBaseMaterial.DARK_OAK).forGetter(SpyglassData::base),
                SpyglassTrimMaterial.CODEC.optionalFieldOf("trim", SpyglassTrimMaterial.GOLD).forGetter(SpyglassData::trim)
            ).apply(it, ::SpyglassData)
        }
        @JvmField
        val STREAM_CODEC: StreamCodec<in RegistryFriendlyByteBuf, SpyglassData> = StreamCodec.composite(
            ItemStack.OPTIONAL_STREAM_CODEC, SpyglassData::lens,
            ByteBufCodecs.list<RegistryFriendlyByteBuf, ItemStack>().apply(ItemStack.STREAM_CODEC), SpyglassData::attachments,
            SpyglassBaseMaterial.STREAM_CODEC, SpyglassData::base,
            SpyglassTrimMaterial.STREAM_CODEC, SpyglassData::trim,
            ::SpyglassData
        )
        @JvmStatic
        @get:JvmName("getDefault")
        val DEFAULT by lazy {
            SpyglassData(ItemStack(EyeSpy.basicLens.get()), listOf(), SpyglassBaseMaterial.DARK_OAK, SpyglassTrimMaterial.GOLD)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SpyglassData) return false

        if (lens != other.lens) return false
        if (attachments != other.attachments) return false
        if (base != other.base) return false
        if (trim != other.trim) return false

        return true
    }

    override fun hashCode(): Int {
        var result = lens.hashCode()
        result = 31 * result + attachments.hashCode()
        result = 31 * result + base.hashCode()
        result = 31 * result + trim.hashCode()
        return result
    }
}
