package github.nwn.jubilife.tools.psra.protocol.commands.minor

import github.nwn.jubilife.tools.psra.protocol.*
import github.nwn.jubilife.tools.psra.protocol.Position

class EndItemCommand private constructor(
    override val value: String,
    val position: Position,
    val name: String,
    val item: String
) :
    IBattleCommand {
    companion object : IBattleCommandFactory {
        override val commandName: String = "-enditem"
        override fun create(value: String, command: String, tokens: List<String>): EndItemCommand {
            val (position, name) = getPokemonPosition(tokens.first())
            return EndItemCommand(value, position, name, tokens[1])
        }
    }

    override fun invoke(replay: MutableReplay) {
        replay.update(replay.battle[position]) {
            it.copy(
                item = ""
            )
        }
    }
}