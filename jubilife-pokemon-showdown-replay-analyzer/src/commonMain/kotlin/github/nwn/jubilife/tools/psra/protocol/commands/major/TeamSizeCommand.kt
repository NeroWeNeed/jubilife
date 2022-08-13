package github.nwn.jubilife.tools.psra.protocol.commands.major

import github.nwn.jubilife.tools.psra.protocol.*
import github.nwn.jubilife.tools.psra.protocol.PlayerIdentifier

class TeamSizeCommand private constructor(
    override val value: String,
    val identifier: PlayerIdentifier,
    val size: Int
) :
    IBattleCommand {
    companion object : IBattleCommandFactory {
        override val commandName: String = "teamsize"
        override fun create(value: String, command: String, tokens: List<String>) =
            TeamSizeCommand(
                value,
                PlayerIdentifier(tokens[0]),
                tokens[1].toInt()
            )
    }

    override fun invoke(replay: MutableReplay) {
        replay[identifier].apply {
            team.clear()
            repeat(size) {
                team.add(Pokemon.UNKNOWN)
            }
        }
    }

}