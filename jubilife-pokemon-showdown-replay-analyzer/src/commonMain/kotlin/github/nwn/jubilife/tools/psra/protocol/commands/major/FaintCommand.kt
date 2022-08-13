package github.nwn.jubilife.tools.psra.protocol.commands.major

import github.nwn.jubilife.tools.psra.protocol.*
import github.nwn.jubilife.tools.psra.protocol.PokemonStatus
import github.nwn.jubilife.tools.psra.protocol.Position

class FaintCommand private constructor(
    override val value: String,
    val position: Position,
    val name: String
) : IBattleCommand {
    companion object : IBattleCommandFactory {
        override val commandName: String = "faint"
        override fun create(value: String, command: String, tokens: List<String>): FaintCommand {
            val (position, name) = getPokemonPosition(tokens.first())
            return FaintCommand(value, position, name)
        }
    }

    override fun invoke(replay: MutableReplay) {
        replay.update(replay.battle[position]) {
            it.copy(
                health = 0f,
                status = PokemonStatus.FAINTED
            )
        }
    }

}