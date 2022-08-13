package github.nwn.jubilife.tools.psra

open class PSRAException : Exception {
    constructor() : super()
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

class PSRACommandException(val command: String, cause: Throwable?) : PSRAException("Error with Command: $command", cause) {
}

class PSRAParseException(val command: String,val line: Int, cause: Throwable?) : PSRAException("Error with Command: $command [$line]", cause) {
}