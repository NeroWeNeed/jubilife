package github.nwn.jubilife.tools.psra.protocol.commands.major

import github.nwn.jubilife.tools.psra.protocol.*
import github.nwn.jubilife.tools.psra.protocol.Details
import github.nwn.jubilife.tools.psra.protocol.PlayerIdentifier

class PokeCommand private constructor(
    override val value: String,
    val identifier: PlayerIdentifier,
    val details: Details,
    val item: String
) :
    IBattleCommand {
    companion object : IBattleCommandFactory {
        override val commandName: String = "poke"
        override fun create(value: String, command: String, tokens: List<String>) =
            PokeCommand(
                value,
                PlayerIdentifier(tokens[0]),
                Details.parse(tokens[1]),
                tokens.getOrNull(2) ?: ""
            )
    }

    override fun invoke(replay: MutableReplay) {
        replay.ensureCapacity(identifier.index)
        replay[identifier].apply {
            val index = this.team.indexOfFirst { it == Pokemon.UNKNOWN }
            if (index != -1)
                this.team[index] = Pokemon(details)
            else
                this.team.add(Pokemon(details))
        }
    }

}