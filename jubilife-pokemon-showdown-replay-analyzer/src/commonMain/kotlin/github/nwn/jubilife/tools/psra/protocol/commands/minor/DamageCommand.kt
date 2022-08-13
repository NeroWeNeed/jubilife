package github.nwn.jubilife.tools.psra.protocol.commands.minor

import github.nwn.jubilife.tools.psra.protocol.*
import github.nwn.jubilife.tools.psra.protocol.PokemonStatus
import github.nwn.jubilife.tools.psra.protocol.Position

open class DamageCommand private constructor(
    override val value: String,
    override val position: Position,
    override val name: String,
    override val health: Float,
    override val status: PokemonStatus
) :
    IBaseDamageCommand {
    companion object : IBattleCommandFactory {
        override val commandName: String = "-damage"
        override fun create(value: String, command: String, tokens: List<String>): DamageCommand {
            val (position, name) = getPokemonPosition(tokens.first())
            val (health, status) = getPokemonHealthStatus(tokens[1])
            return DamageCommand(value, position, name, health, status)
        }
    }
}