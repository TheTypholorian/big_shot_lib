package net.typho.big_shot_lib.api.util

data class Stopwatch(
    @JvmField
    val start: Long = System.currentTimeMillis(),
    @JvmField
    var time: Long? = null
) {
    fun stop(): Long {
        val time = System.currentTimeMillis() - start
        this.time = time
        return time
    }

    override fun toString(): String {
        return (time ?: throw IllegalStateException("Forgot to stop stopwatch")).toString()
    }
}