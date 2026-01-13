package net.typho.big_shot_lib.error

import org.lwjgl.util.spvc.Spvc.spvc_context_get_last_error_string

class SPVCException : RuntimeException {
    constructor() : super()

    @OptIn(ExperimentalStdlibApi::class)
    constructor(code: Int, context: Long) : super("SPVC error code 0x${code.toHexString()}, message: ${spvc_context_get_last_error_string(context)}")

    constructor(message: String?) : super(message)

    constructor(message: String?, cause: Throwable?) : super(message, cause)

    constructor(cause: Throwable?) : super(cause)

    constructor(
        message: String?,
        cause: Throwable?,
        enableSuppression: Boolean,
        writableStackTrace: Boolean
    ) : super(message, cause, enableSuppression, writableStackTrace)
}