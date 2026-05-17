package net.typho.big_shot_lib.plugin

enum class MCVersion(
    @JvmField
    val matches: Array<String>
) {
    MC1_20(arrayOf("1.20", "1.20.1")),
    MC1_20_2(arrayOf("1.20.2", "1.20.3", "1.20.4")),
    MC1_20_5(arrayOf("1.20.5", "1.20.6")),
    MC1_21(arrayOf("1.21", "1.21.1")),
    MC1_21_2(arrayOf("1.21.2", "1.21.3", "1.21.4")),
    MC1_21_5(arrayOf("1.21.5")),
    MC1_21_6(arrayOf("1.21.6", "1.21.7", "1.21.8")),
    MC1_21_9(arrayOf("1.21.9")),
    MC1_21_10(arrayOf("1.21.10")),
    MC1_21_11(arrayOf("1.21.11")),
    MC26_1(arrayOf("26.1")),
    MC26_2(arrayOf("26.2"));

    companion object {
        @JvmStatic
        operator fun get(key: String) = MCVersion.entries.firstOrNull { it.matches.contains(key) }

        @JvmField
        val CURRENT = MC1_21
    }
}