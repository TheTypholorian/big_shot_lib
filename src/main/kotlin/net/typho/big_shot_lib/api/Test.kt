package net.typho.big_shot_lib.api

import net.typho.big_shot_lib.api.BigShotApi.loadService

interface IFace {
    companion object {
        @JvmStatic
        fun provider() = this
    }
}

object Test : IFace {
    @JvmStatic
    fun main(args: Array<String>) {
        println(IFace::class.loadService())
    }
}