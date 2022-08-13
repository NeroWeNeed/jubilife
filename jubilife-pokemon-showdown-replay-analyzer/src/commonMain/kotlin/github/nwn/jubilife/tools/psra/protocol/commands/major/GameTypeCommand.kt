package github.nwn.jubilife.tools.psra.protocol.commands.major

import github.nwn.jubilife.tools.psra.protocol.Battle
import github.nwn.jubilife.tools.psra.protocol.IBattleCommand
import github.nwn.jubilife.tools.psra.protocol.IBattleCommandFactory
import github.nwn.jubilife.tools.psra.protocol.MutableReplay

class GameTypeCommand private constructor(
    override val value: String,
    val gameType: String
) :
    IBattleCommand {
    companion object : IBattleCommandFactory {
        override val commandName: String = "gametype"
        override fun create(value: String, command: String, tokens: List<String>) =
            GameTypeCommand(
                value,
                tokens.first()
            )
    }

    override fun invoke(replay: MutableReplay) {
        replay.battle = Battle.create(gameType)
    }

}