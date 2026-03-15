package net.typho.big_shot_lib.api.util.resource

import com.google.gson.JsonElement
import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.PropertyMap
import com.mojang.serialization.Codec
import net.minecraft.util.ExtraCodecs
import net.typho.big_shot_lib.api.util.NeoColor
import org.joml.*
import java.nio.ByteBuffer
import java.util.*
import java.util.stream.IntStream
import java.util.stream.LongStream

object NeoCodecs {
    private val codecs = HashMap<Class<*>, Codec<*>>()

    init {
        codecs[Boolean::class.java] = Codec.BOOL
        codecs[Byte::class.java] = Codec.BYTE
        codecs[Short::class.java] = Codec.SHORT
        codecs[Int::class.java] = Codec.INT
        codecs[Long::class.java] = Codec.LONG
        codecs[Float::class.java] = Codec.FLOAT
        codecs[Double::class.java] = Codec.DOUBLE
        codecs[String::class.java] = Codec.STRING
        codecs[ByteBuffer::class.java] = Codec.BYTE_BUFFER
        codecs[IntStream::class.java] = Codec.INT_STREAM
        codecs[LongStream::class.java] = Codec.LONG_STREAM

        codecs[JsonElement::class.java] = ExtraCodecs.JSON
        codecs[Vector3f::class.java] = ExtraCodecs.VECTOR3F
        codecs[Vector4f::class.java] = ExtraCodecs.VECTOR4F
        codecs[Quaternionf::class.java] = ExtraCodecs.QUATERNIONF
        codecs[AxisAngle4f::class.java] = ExtraCodecs.AXISANGLE4F
        codecs[Matrix4f::class.java] = ExtraCodecs.MATRIX4F
        codecs[BitSet::class.java] = ExtraCodecs.BIT_SET
        codecs[PropertyMap::class.java] = ExtraCodecs.PROPERTY_MAP
        codecs[GameProfile::class.java] = ExtraCodecs.GAME_PROFILE

        codecs[NeoColor::class.java] = NeoColor.CODEC_ANY
    }

    @JvmStatic
    fun <V, T> createList(size: Int, codec: Codec<T>, to: (list: List<T>) -> V, from: (value: V) -> List<T>): Codec<V> {
        return codec.listOf(size, size).xmap(to, from)
    }

    @JvmStatic
    inline fun <reified E : Enum<E>> enumCodec(): Codec<E> {
        return Codec.STRING.xmap(
            { key -> enumValueOf<E>(key) },
            { entry -> entry.name }
        )
    }
}