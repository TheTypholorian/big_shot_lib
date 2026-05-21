package net.typho.big_shot_lib.impl.util

//? if <1.21 {
/*import net.minecraft.ResourceLocationException
import net.minecraft.resources.Identifier

object OldIdentifierUtil {
    @JvmStatic
    fun fromNamespaceAndPath(namespace: String, path: String): Identifier = Identifier(namespace, path)

    @JvmStatic
    fun parse(id: String): Identifier = Identifier(id)

    @JvmStatic
    fun withDefaultNamespace(path: String): Identifier = Identifier(Identifier.DEFAULT_NAMESPACE, path)

    @JvmStatic
    fun bySeparator(id: String, c: Char): Identifier = Identifier.of(id, c)

    @JvmStatic
    fun tryBySeparator(id: String, c: Char): Identifier? {
        return try {
            bySeparator(id, c)
        } catch (e: ResourceLocationException) {
            null
        }
    }
}
*///? }