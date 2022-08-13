package github.nwn.jubilife.tools.psra.protocol.commands.major

import github.nwn.jubilife.tools.psra.protocol.*
import github.nwn.jubilife.tools.psra.protocol.Details
import github.nwn.jubilife.tools.psra.protocol.PokemonStatus
import github.nwn.jubilife.tools.psra.protocol.Position

class SwitchCommand private constructor(
    override val value: String,
    val position: Position,
    val name: String,
    val details: Details,
    val health: Float,
    val status: PokemonStatus
) : IBattleCommand {
    companion object : IBattleCommandFactory {
        override val commandName: String = "switch"
        override fun create(value: String, command: String, tokens: List<String>): SwitchCommand {
            val (position, name) = getPokemonPosition(tokens.first())
            val details = Details.parse(tokens[1])
            val (health, status) = tokens.getOrNull(2)?.let { getPokemonHealthStatus(it) }
                ?: (1f to PokemonStatus.UNKNOWN)
            return SwitchCommand(
                value, position, name, details, health, status
            )
        }
    }

    override fun invoke(replay: MutableReplay) {
        val teamIndex = replay[position.player].team.searchIndex(name, details, health, status).let { teamIndex ->
            if (teamIndex < 0)
                replay[position.player].team.indexOfFirst { it == Pokemon.UNKNOWN }
            else
                teamIndex
        }

        replay.battle[position] = replay.battle[position].copy(
            pokemonIndex = teamIndex,
            playerIdentifier = position.player
        )
        replay.update(replay.battle[position]) {
            it.copy(
                name = it.name.ifEmpty { name },
                species = if (it.species != details.species) details.species else it.species,
                shiny = if (it.shiny != details.shiny) details.shiny else it.shiny,
                level = if (it.level != details.level) details.level else it.level
            )
        }
    }

}