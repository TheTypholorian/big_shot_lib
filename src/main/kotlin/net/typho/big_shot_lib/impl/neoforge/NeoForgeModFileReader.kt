package net.typho.big_shot_lib.impl.neoforge

import net.neoforged.fml.jarcontents.JarContents
import net.neoforged.fml.loading.moddiscovery.ModFile
import net.neoforged.fml.loading.moddiscovery.ModJarMetadata
import net.neoforged.neoforgespi.language.IModFileInfo
import net.neoforged.neoforgespi.locating.IModFile
import net.neoforged.neoforgespi.locating.IModFileReader
import net.neoforged.neoforgespi.locating.ModFileDiscoveryAttributes
import net.typho.big_shot_lib.common.loading.BigShotModInfo
import org.slf4j.LoggerFactory

class NeoForgeModFileReader : IModFileReader {
    companion object {
        @JvmField
        val LOGGER = LoggerFactory.getLogger("Big Shot Mod Loader")

        @JvmStatic
        fun modJsonParser(file: IModFile): IModFileInfo? {
            val json = file.contents.get(BigShotModInfo.FILE_NAME)
            
            if (json == null) {
                LOGGER.warn("Mod ${file.filePath} is missing a ${BigShotModInfo.FILE_NAME} file")
                return null
            }

            return json.open().use { ModFileInfoImpl(file, BigShotModInfo.GSON.fromJson(it.reader(), BigShotModInfo::class.java)) }
        }
    }

    override fun read(
        jar: JarContents,
        attributes: ModFileDiscoveryAttributes
    ): IModFile? {
        return if (jar.containsFile(BigShotModInfo.FILE_NAME)) {
            val metadata = ModJarMetadata()
            ModFile(jar, metadata, ::modJsonParser, attributes.withReader(this)).also { metadata.setModFile(it) }
        } else null

        /*
        return jar.openFile("big_shot.mod.json")?.let { json ->
            try {
                val attributes = attributes.withReader(this)
                val json = JsonParser.parseReader(json.reader()).asJsonObject
            } catch (e: Exception) {
                throw IllegalStateException("Unable to load Big Shot Lib mod ${jar.primaryPath}. Please report this to the mod author.", e)
            }

            TODO()
        }
         */
    }

}