package net.typho.big_shot_lib.api.services

import net.minecraft.server.packs.PackResources
import net.minecraft.server.packs.resources.Resource
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.util.*
import java.util.function.Predicate
import java.util.stream.Stream

interface NeoResourceManager {
    fun getNamespaces(): MutableSet<String>

    fun getResourceStack(location: ResourceIdentifier): MutableList<Resource>

    fun listResources(
        folder: String,
        predicate: Predicate<ResourceIdentifier>
    ): MutableMap<ResourceIdentifier, Resource>

    fun listResourceStacks(
        folder: String,
        predicate: Predicate<ResourceIdentifier>
    ): MutableMap<ResourceIdentifier, MutableList<Resource>>

    fun listPacks(): Stream<PackResources>

    fun getResource(location: ResourceIdentifier): Optional<Resource>

    @Throws(FileNotFoundException::class)
    fun getResourceOrThrow(location: ResourceIdentifier): Resource {
        return getResource(location)
            .orElseThrow { FileNotFoundException(location.toString()) }
    }

    @Throws(IOException::class)
    fun open(location: ResourceIdentifier): InputStream {
        return getResourceOrThrow(location).open()
    }

    @Throws(IOException::class)
    fun openAsReader(location: ResourceIdentifier): BufferedReader {
        return getResourceOrThrow(location).openAsReader()
    }
}