package github.nwn.jubilife.tools.psra.protocol.commands.minor

import github.nwn.jubilife.tools.psra.protocol.*
import github.nwn.jubilife.tools.psra.protocol.PokemonStatus
import github.nwn.jubilife.tools.psra.protocol.Position

class CureStatusCommand private constructor(
    override val value: String,
    val position: Position,
    val name: String,
    val status: PokemonStatus
) :
    IBattleCommand {
    companion object : IBattleCommandFactory {
        override val commandName: String = "-curestatus"
        override fun create(value: String, command: String, tokens: List<String>): CureStatusCommand {
            val (position, name) = getPokemonPosition(tokens.first())
            return CureStatusCommand(value, position, name, PokemonStatus.parse(tokens[1]))
        }
    }

    override fun invoke(replay: MutableReplay) {
        replay.update(replay.battle[position]) {
            if (it.status == status) {
                it.copy(status = PokemonStatus.NONE)
            } else
                it
        }
    }
}