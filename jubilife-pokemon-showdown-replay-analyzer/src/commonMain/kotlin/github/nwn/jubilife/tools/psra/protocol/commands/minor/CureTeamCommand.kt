package github.nwn.jubilife.tools.psra.protocol.commands.minor

import github.nwn.jubilife.tools.psra.protocol.*
import github.nwn.jubilife.tools.psra.protocol.PokemonStatus
import github.nwn.jubilife.tools.psra.protocol.Position

class CureTeamCommand private constructor(
    override val value: String,
    val position: Position,
    val name: String,
) :
    IBattleCommand {
    companion object : IBattleCommandFactory {
        override val commandName: String = "-cureteam"
        override fun create(value: String, command: String, tokens: List<String>): CureTeamCommand {
            val (position, name) = getPokemonPosition(tokens.first())
            return CureTeamCommand(value, position, name)
        }
    }

    override fun invoke(replay: MutableReplay) {
        for (i in replay[position.player].team.indices) {
            replay[position.player].team[i] = replay[position.player].team[i].let {
                if (it.status != PokemonStatus.FAINTED)
                    it.copy(status = PokemonStatus.NONE)
                else
                    it
            }
        }
    }
}