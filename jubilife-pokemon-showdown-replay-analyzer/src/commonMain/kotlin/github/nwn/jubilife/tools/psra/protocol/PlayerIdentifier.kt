package github.nwn.jubilife.tools.psra.protocol

@JvmInline
value class PlayerIdentifier(val identifier: String) {
    companion object {
        val NONE = PlayerIdentifier("p0")
    }
    val index: Int
        get() = identifier.substring(1).toInt()
}