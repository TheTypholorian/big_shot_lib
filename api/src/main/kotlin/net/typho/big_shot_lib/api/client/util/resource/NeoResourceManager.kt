package net.typho.big_shot_lib.api.client.util.resource

import net.minecraft.server.packs.PackResources
import net.minecraft.server.packs.resources.Resource
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.util.*
import java.util.function.Predicate
import java.util.stream.Stream

interface NeoResourceManager {
    fun getNamespaces(): MutableSet<String>

    fun getResourceStack(location: NeoIdentifier): MutableList<Resource>

    fun listResources(
        folder: String,
        predicate: Predicate<NeoIdentifier>
    ): MutableMap<NeoIdentifier, Resource>

    fun listResourceStacks(
        folder: String,
        predicate: Predicate<NeoIdentifier>
    ): MutableMap<NeoIdentifier, MutableList<Resource>>

    fun listPacks(): Stream<PackResources>

    fun getResource(location: NeoIdentifier): Optional<Resource>

    @Throws(FileNotFoundException::class)
    fun getResourceOrThrow(location: NeoIdentifier): Resource {
        return getResource(location)
            .orElseThrow { FileNotFoundException(location.toString()) }
    }

    @Throws(IOException::class)
    fun open(location: NeoIdentifier): InputStream {
        return getResourceOrThrow(location).open()
    }

    @Throws(IOException::class)
    fun openAsReader(location: NeoIdentifier): BufferedReader {
        return getResourceOrThrow(location).openAsReader()
    }
}