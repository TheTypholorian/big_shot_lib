package net.typho.big_shot_lib.impl.neoforge

import net.neoforged.neoforgespi.language.IConfigurable
import net.neoforged.neoforgespi.language.IModFileInfo
import net.neoforged.neoforgespi.language.IModInfo
import net.neoforged.neoforgespi.locating.IModFile
import net.typho.big_shot_lib.api.BigShotLib
import net.typho.big_shot_lib.common.loading.BigShotModInfo
import net.typho.big_shot_lib.common.loading.LoadingConstants
import java.util.Optional

class ModFileInfoImpl(
    private val file: IModFile,
    @JvmField
    val info: BigShotModInfo
) : IModFileInfo {
    private val mods = listOf(ModInfoImpl(this))
    private val languages = listOf(IModFileInfo.LanguageSpec(BigShotLib.MOD_ID, null))

    override fun getMods() = mods

    override fun requiredLanguageLoaders() = languages

    override fun showAsResourcePack() = false

    override fun showAsDataPack() = false

    override fun getFileProperties(): Map<String, Any> {
        TODO("Support custom file properties for neoforge, cus idk what types the values should be")
    }

    override fun getLicense() = info.license ?: ""

    override fun versionString() = info.version

    override fun usesServices(): List<String> = listOf()

    override fun getFile() = file

    /**
     * NeoForge, you were doing so well until this method.
     *
     * Because mixins, access transformers, and the issue tracker URL are loaded from this method instead of just MAKING THEM SEPARATE METHODS IN THIS CLASS, I have to make this mess (note that we have our own AT/AW/CT system, so we return nothing)
     */
    override fun getConfig(): IConfigurable {
        return object : IConfigurable {
            @Suppress("UNCHECKED_CAST")
            override fun <T : Any> getConfigElement(vararg key: String): Optional<T> {
                return if (key.contentEquals(arrayOf("issueTrackerURL"))) {
                    Optional.ofNullable(info.contact[LoadingConstants.CONTACT_ISSUES]) as Optional<T>
                } else {
                    Optional.empty()
                }
            }

            override fun getConfigList(vararg key: String): List<IConfigurable> {
                return if (key.contentEquals(arrayOf("mixins"))) {
                    info.mixins.map { entry -> // TODO test environment
                        object : IConfigurable {
                            @Suppress("UNCHECKED_CAST")
                            override fun <T : Any> getConfigElement(vararg key: String): Optional<T> {
                                return if (key.contentEquals(arrayOf("config"))) Optional.of(entry.path) as Optional<T> else Optional.empty()
                            }

                            override fun getConfigList(vararg key: String): List<IConfigurable> {
                                return listOf()
                            }
                        }
                    }
                } else {
                    listOf()
                }
            }
        }
    }
}