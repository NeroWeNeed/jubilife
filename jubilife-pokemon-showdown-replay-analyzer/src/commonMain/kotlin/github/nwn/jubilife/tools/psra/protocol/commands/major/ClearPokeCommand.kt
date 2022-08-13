package github.nwn.jubilife.tools.psra.protocol.commands.major

import github.nwn.jubilife.tools.psra.protocol.IBattleCommand
import github.nwn.jubilife.tools.psra.protocol.IBattleCommandFactory
import github.nwn.jubilife.tools.psra.protocol.MutableReplay
import github.nwn.jubilife.tools.psra.protocol.Pokemon

class ClearPokeCommand private constructor(
    override val value: String
) :
    IBattleCommand {
    companion object : IBattleCommandFactory {
        override val commandName: String = "clearpoke"
        override fun create(value: String, command: String, tokens: List<String>) = ClearPokeCommand(value)
    }

    override fun invoke(replay: MutableReplay) {
        replay.players.forEach { player ->
            for (i in player.team.indices) {
                player.team[i] = Pokemon("")
            }
        }
    }

}