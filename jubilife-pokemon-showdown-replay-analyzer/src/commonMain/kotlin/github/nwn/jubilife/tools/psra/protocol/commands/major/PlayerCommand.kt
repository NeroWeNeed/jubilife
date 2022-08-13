package github.nwn.jubilife.tools.psra.protocol.commands.major

import github.nwn.jubilife.tools.psra.protocol.IBattleCommand
import github.nwn.jubilife.tools.psra.protocol.IBattleCommandFactory
import github.nwn.jubilife.tools.psra.protocol.MutableReplay
import github.nwn.jubilife.tools.psra.protocol.PlayerIdentifier

class PlayerCommand private constructor(
    override val value: String,
    val identifier: PlayerIdentifier,
    val username: String,
    val avatar: String,
    val rating: Int
) :
    IBattleCommand {
    companion object : IBattleCommandFactory {
        override val commandName: String = "player"
        override fun create(value: String, command: String, tokens: List<String>) =
            PlayerCommand(
                value,
                PlayerIdentifier(tokens[0]),
                tokens.getOrNull(1) ?: "",
                tokens.getOrNull(2) ?: "",
                tokens.getOrNull(3)?.toIntOrNull() ?: -1
            )
    }

    override fun invoke(replay: MutableReplay) {
        replay.ensureCapacity(identifier.index)
        replay[identifier].apply {
            this.avatar = this@PlayerCommand.avatar
            this.username = this@PlayerCommand.username
            this.rating = this@PlayerCommand.rating
        }
    }

}