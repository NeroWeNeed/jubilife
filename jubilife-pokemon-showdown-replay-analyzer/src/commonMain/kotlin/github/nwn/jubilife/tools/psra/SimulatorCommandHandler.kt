package github.nwn.jubilife.tools.psra

/*
import github.nwn.jubilife.tools.psra.protocol.Details
import github.nwn.jubilife.tools.psra.protocol.PokemonStatus
import github.nwn.jubilife.tools.psra.protocol.Position
import org.jsoup.Jsoup
import java.io.File
import java.io.StringReader


object SimulatorCommandHandler : ICommandHandlerSet<SimulationState> {

    override fun handle(message: String, state: SimulationState) {
        SIMULATOR_COMMAND_HANDLER.handle(message, state)
    }


    fun handle(message: String): SimulationState {
        val state = SimulationState()
        handle(message, state)
        return state
    }

    fun handle(messages: Sequence<String>): SimulationState {
        val state = SimulationState()
        handle(messages, state)
        return state
    }

}

fun ICommandHandlerSet<SimulationState>.handle(replay: File): SimulationState {
    val state = SimulationState()
    val content = Jsoup.parse(replay).select("script.battle-log-data[type=text/plain]").first()?.data() ?: ""
    if (content.isEmpty()) {
        throw IllegalArgumentException("Invalid Replay File")
    }
    StringReader(content).useLines { lines ->
        SimulatorCommandHandler.handle(lines, state)
    }
    return state
}

private val SIMULATOR_COMMAND_HANDLER = Commands<SimulationState> {
    command<PlayerState>("player") {
        transformer { data, _ ->
            PlayerState(data)
        }
        command { data, state ->
            state.players[data.identifier] = data
        }
    }

    command<Pair<String, Int>>("teamsize") {
        transformer { data, _ ->
            data.first() to data[1].toInt()
        }
        command { (identifier, total), state ->
            state.players[identifier]?.apply {
                team = arrayOfNulls<PokemonState?>(total).toMutableList()
            }
        }
    }
    command<GameFormat>("gametype") {
        transformer { data, _ ->
            GameFormat.getGameType(data.first())
        }
        command { gametype, state ->
            state.gameFormat = gametype
        }
    }
    command<String>("gen") {
        transformer { data, _ ->
            data.first()
        }
        command { generation, state ->
            state.generation = generation
        }
    }
*/
/*    command("tier") { data, state ->
        //TODO: Tier Command
    }
    command("rule") { data, state ->
        //TODO: Rule Command
    }*//*

    commandRaw("clearpoke") { _, state ->
        state.players.forEach { (_, player) ->
            player.team.fill(null)
        }
    }
    command<String>("gen") {
        transformer { data, _ ->
            data.first()
        }
        command { generation, state ->
            state.generation = generation
        }
    }
    command<Triple<String, Details, String>>("poke") {
        transformer { data, _ ->
            Triple(data.first(), Details.parse(data[1]), data.getOrElse(2) { "" })
        }
        command { (identifier, details, item), state ->
            state.players[identifier]?.apply {
                val index = team.indexOfFirst { it == null }
                if (index == -1)
                    team.add(PokemonState(details, item = item))
                else
                    team[index] = PokemonState(details, item = item)
            }
        }
    }
    command<SwitchInfo>("switch") {
        transformer { data, _ ->
            SwitchInfo.create(data)
        }
        command { (position, name, details, health, status), state ->
            state.gameFormat[position] =
                state.players.getValue(position.player).team.searchIndex(name, details, health, status)

            state.players.getValue(position.player).team[state.gameFormat[position]]?.apply {
                if (this.name.isEmpty())
                    this.name = name
                if (this.details.unknownForm || this.details.shiny != details.shiny || this.details.level != details.level) {
                    this.details =
                        this.details.copy(species = details.species, shiny = details.shiny, level = details.level)
                }


            }

        }
    }
    command<SwitchInfo>("replace") {
        transformer { data, _ ->
            SwitchInfo.create(data)
        }
        command { (position, name, details, health, status), state ->
            val current: Pair<Float, PokemonStatus>? =
                state.players.getValue(position.player).team[state.gameFormat[position]]?.run {
                    val current = this.health to this.status
                    this.health = 1f
                    this.status = PokemonStatus.UNKNOWN
                    current
                }

            state.gameFormat[position] =
                state.players.getValue(position.player).team.searchIndex(name, details, health, status)
            state.players.getValue(position.player).team[state.gameFormat[position]]?.apply {
                if (this.name.isEmpty())
                    this.name = name
                if (this.details.unknownForm || this.details.shiny != details.shiny || this.details.level != details.level) {
                    this.details =
                        this.details.copy(species = details.species, shiny = details.shiny, level = details.level)
                }
                current?.let { (currentHealth, currentStatus) ->
                    this.health = currentHealth
                    this.status = currentStatus
                }
            }
        }
    }
    command<SwitchInfo>("detailschange") {
        transformer { data, _ ->
            SwitchInfo.create(data)
        }
        command { (position, _, details, health, status), state ->

            state.players.getValue(position.player).team[state.gameFormat[position]]?.apply {
                this.details = details
                this.health = health
                this.status = status
            }
        }
    }


    command<DamageInfo>("-damage") {
        transformer { data, _ ->
            DamageInfo.create(data)
        }
        command { (position, name, health, status), state ->
            state.players.getValue(position.player).team[state.gameFormat[position]]?.apply {
                this.health = health
                this.status = if (health == 0f) PokemonStatus.FAINTED else status
            }
        }
    }
    command<DamageInfo>("-heal") {
        transformer { data, _ ->
            DamageInfo.create(data)
        }
        command { (position, name, health, status), state ->
            state.players.getValue(position.player).team[state.gameFormat[position]]?.apply {
                this.health = health
                this.status = if (health == 0f) PokemonStatus.FAINTED else status
            }
        }
    }
    command<Triple<Position, String, PokemonStatus>>("-status") {
        transformer { data, _ ->
            getPokemonPosition(data.first()).let { (position, name) ->
                Triple(position, name, PokemonStatus.parse(data[1]))
            }
        }
        command { (position, name, status), state ->
            state.players.getValue(position.player).team[state.gameFormat[position]]?.apply {
                this.status = status
            }
        }
    }

    command<Triple<Position, String, PokemonStatus>>("-curestatus") {
        transformer { data, _ ->
            getPokemonPosition(data.first()).let { (position, name) ->
                Triple(position, name, PokemonStatus.parse(data[1]))
            }
        }
        command { (position, name, status), state ->
            state.players.getValue(position.player).team[state.gameFormat[position]]?.apply {
                if (this.status == status) {
                    this.status = PokemonStatus.NONE
                }
            }
        }
    }

    command<Pair<Position, String>>("-cureteam") {
        transformer { data, _ ->
            getPokemonPosition(data.first())
        }
        command { (position, name), state ->
            state.players.getValue(position.player).team.forEach {
                if (it != null && it.status != PokemonStatus.FAINTED) {
                    it.status = PokemonStatus.NONE
                }

            }
        }
    }
    command<Triple<Position, String, String>>("-item") {
        transformer { data, _ ->
            getPokemonPosition(data.first()).let { (position, name) ->
                Triple(position, name, data[1])
            }
        }
        command { (position, name, item), state ->
            state.players.getValue(position.player).team[state.gameFormat[position]]?.apply {
                this.item = item
            }
        }
    }

    command<Triple<Position, String, String>>("-enditem") {
        transformer { data, _ ->
            getPokemonPosition(data.first()).let { (position, name) ->
                Triple(position, name, data[1])
            }
        }
        command { (position, name, item), state ->
            state.players.getValue(position.player).team[state.gameFormat[position]]?.apply {
                this.item = ""
            }
        }
    }
    command<Pair<Position, String>>("faint") {
        transformer { data, _ ->
            getPokemonPosition(data.first())
        }
        command { (position, name), state ->
            state.players.getValue(position.player).team[state.gameFormat[position]]?.apply {
                this.health = 0f
                this.status = PokemonStatus.FAINTED
            }
        }
    }
    command<String>("win") {
        transformer { data, _ ->
            data.first()
        }
        command { winner, state ->
            state.players.forEach { (_, player) ->
                println("${player.username}'s Team:")
                player.team.forEach { pokemon ->
                    println(" - $pokemon")
                }
            }
        }
    }
    command<Int>("turn") {
        transformer { data, _ ->
            data.first().toInt()
        }
        command { turn, state ->
            state.turn = turn
        }
    }

}*/
