package github.nwn.jubilife.tools.psra.protocol.commands.major

import github.nwn.jubilife.tools.psra.protocol.*

class ReplaceCommand private constructor(
    override val value: String,
    val position: Position,
    val name: String,
    val details: Details,
    val health: Float,
    val status: PokemonStatus
) : IBattleCommand {
    companion object : IBattleCommandFactory {
        override val commandName: String = "replace"
        override fun create(value: String, command: String, tokens: List<String>): ReplaceCommand {
            val (position, name) = getPokemonPosition(tokens.first())
            val details = Details.parse(tokens[1])
            val (health, status) = tokens.getOrNull(2)?.let { getPokemonHealthStatus(it) }
                ?: (1f to PokemonStatus.UNKNOWN)
            return ReplaceCommand(
                value, position, name, details, health, status
            )
        }
    }

    override fun invoke(replay: MutableReplay) {
        val (currentHealth, currentStatus) = replay.battle[position].let { activePokemon ->
            replay[activePokemon.playerIdentifier].team[activePokemon.pokemonIndex].let { it.health to it.status }
        }
        replay.update(replay.battle[position]) {
            it.copy(
                health = 1f,
                status = PokemonStatus.UNKNOWN
            )
        }

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
                level = if (it.level != details.level) details.level else it.level,
                health = currentHealth,
                status = currentStatus
            )
        }
    }

}