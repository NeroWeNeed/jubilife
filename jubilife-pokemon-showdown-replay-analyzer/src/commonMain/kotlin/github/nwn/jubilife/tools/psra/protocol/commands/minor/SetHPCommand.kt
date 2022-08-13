package github.nwn.jubilife.tools.psra.protocol.commands.minor

import github.nwn.jubilife.tools.psra.protocol.*
import github.nwn.jubilife.tools.psra.protocol.Position

class SetHPCommand private constructor(
    override val value: String,
    val position: Position,
    val name: String,
    val health: Float
) :
    IBattleCommand {
    companion object : IBattleCommandFactory {
        override val commandName: String = "-sethp"
        override fun create(value: String, command: String, tokens: List<String>): SetHPCommand {
            val (position, name) = getPokemonPosition(tokens.first())
            return SetHPCommand(value, position, name, getPokemonHealth(tokens[1]))
        }
    }

    override fun invoke(replay: MutableReplay) {
        replay.update(replay.battle[position]) {
            it.copy(
                health = health
            )
        }
    }
}