package github.nwn.jubilife.tools.psra.protocol

import github.nwn.jubilife.tools.psra.protocol.Details

fun List<Pokemon>.search(name: String, details: Details, health: Float, status: PokemonStatus): Pokemon? {
    return this.getOrNull(searchIndex(name, details, health, status))
}
private val UNKNOWN_FORM_REGEX = Regex("(.*?)-\\*")
private val KNOWN_FORM_REGEX = Regex("(.*?)-(.*?)")
fun List<Pokemon>.searchIndex(name: String, details: Details, health: Float, status: PokemonStatus): Int {
    val predicateSpeciesForm = KNOWN_FORM_REGEX.matchEntire(details.species)
    return this.search(
        {
            it.name == name
        },
        {
            if (predicateSpeciesForm == null) {
                it.species == details.species
            } else {
                val targetSpeciesForm = UNKNOWN_FORM_REGEX.matchEntire(it.species ?: "")
                if (targetSpeciesForm == null) {
                    it.species == details.species
                }
                else {
                    predicateSpeciesForm.groupValues[1] == targetSpeciesForm.groupValues[1]
                }
            }

        },
        {
            it.level == details.level
        },
        {
            it.gender == details.gender
        },
        {
            it.shiny == details.shiny
        },
        {
            it.health == health
        },
        {
            it.status == status
        }
    )
}

fun <T> List<T>.search(vararg ops: (T) -> Boolean): Int {
    val indexList = this.indices.toList()
    var indices = indexList
    for (op in ops) {
        indices = indices.filter {
            op(this[it])
        }
        if (indices.size == 1) {
            return indices.first()
        }
        if (indices.isEmpty()) {
            indices = indexList
        }
    }
    return -1
}

fun getPokemonPosition(pokemonPosition: String) = pokemonPosition.split(':').let { (pos, name) ->
    Pair(Position(pos), name.trim())
}

private val healthRegex = Regex("(\\d*)(?:\\\\\\/(\\d*))?")
fun getPokemonHealthStatus(healthStatus: String): Pair<Float, PokemonStatus> = healthStatus.split(' ').let {
    val health = it.first()
    val status = it.getOrNull(1) ?: ""
    Pair(
        healthRegex.matchEntire(health)!!.groupValues.let { (_, l, r) ->
            if (r.isNotEmpty()) l.toFloat() / r.toFloat() else l.toFloat()
        },
        PokemonStatus.parse(status)
    )
}
fun getPokemonHealth(health: String): Float = healthRegex.matchEntire(health)!!.groupValues.let { (_, l, r) ->
    if (r.isNotEmpty()) l.toFloat() / r.toFloat() else l.toFloat()
}