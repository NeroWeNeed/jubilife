package github.nwn.jubilife.tools.psra.protocol.commands.major

import github.nwn.jubilife.tools.psra.protocol.*
import github.nwn.jubilife.tools.psra.protocol.Details
import github.nwn.jubilife.tools.psra.protocol.PokemonStatus
import github.nwn.jubilife.tools.psra.protocol.Position

class DetailsChangeCommand private constructor(
    override val value: String,
    val position: Position,
    val name: String,
    val details: Details,
    val health: Float,
    val status: PokemonStatus
) : IBattleCommand {
    companion object : IBattleCommandFactory {
        override val commandName: String = "detailschange"
        override fun create(value: String, command: String, tokens: List<String>): DetailsChangeCommand {
            val (position, name) = getPokemonPosition(tokens.first())
            val details = Details.parse(tokens[1])
            val (health, status) = tokens.getOrNull(2)?.let { getPokemonHealthStatus(it) }
                ?: (1f to PokemonStatus.UNKNOWN)
            return DetailsChangeCommand(
                value, position, name, details, health, status
            )
        }
    }

    override fun invoke(replay: MutableReplay) {
        replay.update(replay.battle[position]) {
            it.copy(
                species = details.species,
                shiny = details.shiny,
                level = details.level,
                gender = details.gender,
                health = health,
                status = status
            )
        }
    }

}