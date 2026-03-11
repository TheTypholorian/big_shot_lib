package net.typho.big_shot_lib.api.errors

open class GlBindException : RuntimeException {
    constructor() : super()

    constructor(result: GlBindResult) : super(result.toString())

    constructor(message: String?) : super(message)

    constructor(message: String?, cause: Throwable?) : super(message, cause)

    constructor(cause: Throwable?) : super(cause)

    constructor(message: String?, cause: Throwable?, enableSuppression: Boolean, writableStackTrace: Boolean) : super(
        message,
        cause,
        enableSuppression,
        writableStackTrace
    )
}