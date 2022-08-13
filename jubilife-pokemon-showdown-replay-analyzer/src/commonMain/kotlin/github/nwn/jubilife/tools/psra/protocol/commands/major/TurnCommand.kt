package github.nwn.jubilife.tools.psra.protocol.commands.major

import github.nwn.jubilife.tools.psra.protocol.IBattleCommand
import github.nwn.jubilife.tools.psra.protocol.IBattleCommandFactory
import github.nwn.jubilife.tools.psra.protocol.MutableReplay

class TurnCommand private constructor(
    override val value: String,
    val turn: Int
) : IBattleCommand {
    companion object : IBattleCommandFactory {
        override val commandName: String = "turn"
        override fun create(value: String, command: String, tokens: List<String>) =
            TurnCommand(value, tokens.first().toInt())
    }

    override fun invoke(replay: MutableReplay) {
        replay.turn = turn
    }

}