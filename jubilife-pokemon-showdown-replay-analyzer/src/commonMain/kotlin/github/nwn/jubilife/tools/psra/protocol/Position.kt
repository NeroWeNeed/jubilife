package github.nwn.jubilife.tools.psra.protocol

@JvmInline
value class Position(val identifier: String) {
    val player: PlayerIdentifier
        get() = PlayerIdentifier(identifier.substring(0..1))
    val slot: String
        get() = identifier.substring(2)
}