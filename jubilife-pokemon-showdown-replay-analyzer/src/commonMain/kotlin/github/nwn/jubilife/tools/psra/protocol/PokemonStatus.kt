package github.nwn.jubilife.tools.psra.protocol

enum class PokemonStatus {
    NONE, POISONED, BADLY_POISONED, BURNED, PARALYZED, FROZEN, ASLEEP, FAINTED,UNKNOWN;

    companion object {
        fun parse(input: String) = when (input) {
            "slp" -> ASLEEP
            "par" -> PARALYZED
            "tox" -> BADLY_POISONED
            "psn" -> POISONED
            "frz" -> FROZEN
            "brn" -> BURNED
            "fnt" -> FAINTED
            else -> NONE
        }
    }
}