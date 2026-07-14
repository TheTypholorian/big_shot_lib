package net.typho.big_shot_lib.plugin

enum class MCVersion(
    @JvmField
    val neoforgePrefix: String?,
    @JvmField
    val parchmentVersion: String?,
    @JvmField
    val versions: Array<String>
) {
    MC1_16_1(neoforgePrefix = null, parchmentVersion = "1.16.5:2022.03.06", versions = arrayOf("1.16.1")),
    MC1_16_5(neoforgePrefix = null, parchmentVersion = "1.16.5:2022.03.06", versions = arrayOf("1.16.5")),

    MC1_17_1(neoforgePrefix = null, parchmentVersion = "1.17.1:2021.12.12", versions = arrayOf("1.17.1")),

    MC1_18_2(neoforgePrefix = null, parchmentVersion = "1.18.2:2022.11.06", versions = arrayOf("1.18.2")),

    MC1_19_2(neoforgePrefix = null, parchmentVersion = "1.19.2:2022.11.27", versions = arrayOf("1.19.2")),
    MC1_19_3(neoforgePrefix = null, parchmentVersion = "1.19.3:2023.06.25", versions = arrayOf("1.19.3")),
    MC1_19_4(neoforgePrefix = null, parchmentVersion = "1.19.4:2023.06.26", versions = arrayOf("1.19.4")),

    MC1_20_1(neoforgePrefix = null, parchmentVersion = "1.20.1:2023.09.03", versions = arrayOf("1.20", "1.20.1")),
    MC1_20_2(neoforgePrefix = null, parchmentVersion = "1.20.2:2023.12.10", versions = arrayOf("1.20.2")),
    MC1_20_3(neoforgePrefix = null, parchmentVersion = "1.20.3:2023.12.31", versions = arrayOf("1.20.3")),
    MC1_20_4(neoforgePrefix = null, parchmentVersion = "1.20.4:2024.04.14", versions = arrayOf("1.20.4")),
    MC1_20_5(neoforgePrefix = null, parchmentVersion = "1.20.6:2024.06.16", versions = arrayOf("1.20.5")),
    MC1_20_6(neoforgePrefix = null, parchmentVersion = "1.20.6:2024.06.16", versions = arrayOf("1.20.6")),

    MC1_21_1(neoforgePrefix = "21.1", parchmentVersion = "1.21.1:2024.11.17", versions = arrayOf("1.21", "1.21.1")),
    MC1_21_2(neoforgePrefix = "21.2", parchmentVersion = "1.21.3:2024.12.07", versions = arrayOf("1.21.2")),
    MC1_21_3(neoforgePrefix = "21.3", parchmentVersion = "1.21.3:2024.12.07", versions = arrayOf("1.21.3")),
    MC1_21_4(neoforgePrefix = "21.4", parchmentVersion = "1.21.4:2025.03.23", versions = arrayOf("1.21.4")),
    MC1_21_5(neoforgePrefix = "21.5", parchmentVersion = "1.21.5:2025.06.15", versions = arrayOf("1.21.5")),
    MC1_21_6(neoforgePrefix = "21.6", parchmentVersion = "1.21.6:2025.06.29", versions = arrayOf("1.21.6")),
    MC1_21_7(neoforgePrefix = "21.7", parchmentVersion = "1.21.7:2025.07.18", versions = arrayOf("1.21.7")),
    MC1_21_8(neoforgePrefix = "21.8", parchmentVersion = "1.21.8:2025.09.14", versions = arrayOf("1.21.8")),
    MC1_21_9(neoforgePrefix = "21.9", parchmentVersion = "1.21.9:2025.10.05", versions = arrayOf("1.21.9")),
    MC1_21_10(neoforgePrefix = "21.10", parchmentVersion = "1.21.10:2025.10.12", versions = arrayOf("1.21.10")),
    MC1_21_11(neoforgePrefix = "21.11", parchmentVersion = "1.21.11:2025.12.20", versions = arrayOf("1.21.11")),

    MC26_1(neoforgePrefix = "26.1.2", parchmentVersion = null, versions = arrayOf("26.1", "26.1.1", "26.1.2")),
    MC26_2(neoforgePrefix = "26.2", parchmentVersion = null, versions = arrayOf("26.2"));

    @JvmField
    val fabricVersionRange: String = if (versions.size == 1) versions.first() else ">=${versions.first()} <=${versions.last()}"
    @JvmField
    val neoVersionRange: String = if (versions.size == 1) versions.first() else "[${versions.first()}, ${versions.last()}]"
    @JvmField
    val primaryVersion = versions.last()
    @JvmField
    val additionalVersions: List<String> = versions.toMutableList().apply { removeLast() }

    fun getVersionRange(loader: ModLoader) = when (loader) {
        ModLoader.FABRIC -> fabricVersionRange
        ModLoader.NEOFORGE -> neoVersionRange
        else -> null
    }

    companion object {
        @JvmStatic
        operator fun get(key: String) = MCVersion.entries.first { it.versions.contains(key) }
    }
}