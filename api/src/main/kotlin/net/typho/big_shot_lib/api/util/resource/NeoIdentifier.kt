package net.typho.big_shot_lib.api.util.resource

import com.mojang.serialization.Codec

@JvmRecord
data class NeoIdentifier(
    @JvmField
    val namespace: String,
    @JvmField
    val path: String
) {
    constructor(pair: Pair<String, String>) : this(pair.first, pair.second)

    constructor(key: String, delimiter: Char) : this(splitKey(key, delimiter))

    constructor(key: String) : this(splitKey(key, DEFAULT_DELIMITER))

    init {
        ensureStringLegal(namespace) { "Invalid character ${namespace[it]} at index $it in namespace of $this" }
        ensureStringLegal(path) { "Invalid character ${path[it]} at index $it in path of $this" }
    }

    override fun toString(): String {
        return namespace + DEFAULT_DELIMITER + path
    }

    fun toPair(): Pair<String, String> {
        return namespace to path
    }

    fun toLanguageKey() = "$namespace.$path"

    fun toShortLanguageKey() = if (namespace == DEFAULT_NAMESPACE) path else toLanguageKey()

    fun toLanguageKey(prefix: String) = "$prefix.${toLanguageKey()}"

    fun toLanguageKey(prefix: String, suffix: String) = "$prefix.${toLanguageKey()}.$suffix"

    fun withPath(path: String) = NeoIdentifier(namespace, path)

    fun withPath(path: (path: String) -> String) = NeoIdentifier(namespace, path(this.path))

    fun withPrefix(prefix: String) = NeoIdentifier(namespace, prefix + path)

    fun withSuffix(suffix: String) = NeoIdentifier(namespace, path + suffix)

    fun equals(pair: Pair<String, String>): Boolean {
        return toPair() == pair
    }

    fun equals(key: String): Boolean {
        return toPair() == splitKey(key, DEFAULT_DELIMITER)
    }

    fun equals(namespace: String, path: String): Boolean {
        return this.namespace == namespace && this.path == path
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NeoIdentifier

        if (namespace != other.namespace) return false
        if (path != other.path) return false

        return true
    }

    override fun hashCode(): Int {
        var result = namespace.hashCode()
        result = 31 * result + path.hashCode()
        return result
    }

    companion object {
        @JvmField
        val CODEC: Codec<NeoIdentifier> = Codec.STRING.xmap(
            ::NeoIdentifier,
            NeoIdentifier::toString
        )
        /*
        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, NeoIdentifier> = ByteBufCodecs.STRING_UTF8.map(
            ::NeoIdentifier,
            NeoIdentifier::toString
        )
         */
        const val DEFAULT_NAMESPACE = "minecraft"
        const val DEFAULT_DELIMITER = ':'

        @JvmStatic
        fun splitKey(key: String, delimiter: Char): Pair<String, String> {
            val index = key.indexOf(delimiter)

            return if (index == -1) {
                DEFAULT_NAMESPACE to key
            } else {
                key.substring(0, index) to key.substring(index + 1)
            }
        }

        @JvmStatic
        fun ensureStringLegal(s: String, error: (index: Int) -> String) {
            s.forEachIndexed { i, c ->
                if (!isLegalChar(c)) {
                    throw IllegalArgumentException(error(i))
                }
            }
        }

        @JvmStatic
        fun isLegalChar(c: Char): Boolean {
            return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || c == '_' || c == '/' || c == '.' || c == '-'
        }
    }
}
