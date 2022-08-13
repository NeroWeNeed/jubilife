package github.nwn.jubilife.tools.psra.protocol.commands.major

import github.nwn.jubilife.tools.psra.protocol.GameState
import github.nwn.jubilife.tools.psra.protocol.IBattleCommand
import github.nwn.jubilife.tools.psra.protocol.IBattleCommandFactory
import github.nwn.jubilife.tools.psra.protocol.MutableReplay

class TieCommand private constructor(
    override val value: String
) : IBattleCommand {
    companion object : IBattleCommandFactory {
        override val commandName: String = "tie"
        override fun create(value: String, command: String, tokens: List<String>) = TieCommand(value)
    }

    override val terminate: Boolean = true

    override fun invoke(replay: MutableReplay) {
        replay.gameState = GameState.Inactive(winner = null)
    }

}