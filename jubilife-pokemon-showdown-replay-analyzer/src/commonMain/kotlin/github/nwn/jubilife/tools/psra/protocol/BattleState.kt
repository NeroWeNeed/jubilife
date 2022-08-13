package github.nwn.jubilife.tools.psra.protocol

import github.nwn.jubilife.tools.psra.protocol.Details

data class Replay(
    val players: List<Player>,
    val battle: Battle,
    val turn: Int = 0,
    val gameState: GameState
)

sealed interface GameState {
    object Active : GameState
    @JvmInline
    value class Inactive(val winner: String?) : GameState
}

data class MutableReplay(
    val players: MutableList<MutablePlayer> = ArrayList(),
    var battle: MutableBattle = MutableSingles.Impl(),
    var turn: Int = 0,
    var gameState: GameState = GameState.Active
) {
    fun ensureCapacity(size: Int) {
        if (players.size < size) {
            repeat(size - players.size) {
                players.add(MutablePlayer.Impl())
            }
        }
    }

    operator fun get(identifier: PlayerIdentifier): MutablePlayer {
        return players[identifier.index - 1]
    }

    operator fun set(identifier: PlayerIdentifier, player: MutablePlayer) {
        players[identifier.index - 1] = player
    }

    operator fun set(identifier: PlayerIdentifier, player: Player) {
        players[identifier.index - 1] = player.toMutable()
    }

    operator fun get(activePokemon: ActivePokemon): Pokemon {
        return this[activePokemon.playerIdentifier].team[activePokemon.pokemonIndex]
    }

    operator fun set(activePokemon: ActivePokemon, pokemon: Pokemon) {
        this[activePokemon.playerIdentifier].team[activePokemon.pokemonIndex] = pokemon
    }

    fun update(activePokemon: ActivePokemon, update: (Pokemon) -> Pokemon) {
        this[activePokemon.playerIdentifier].team[activePokemon.pokemonIndex] =
            update(this[activePokemon.playerIdentifier].team[activePokemon.pokemonIndex])
    }
}

sealed interface Battle {
    companion object {
        internal val PLAYER_1_A = Position("p1a")
        internal val PLAYER_2_A = Position("p2a")
        internal val PLAYER_3_B = Position("p3b")
        internal val PLAYER_4_B = Position("p4b")
        internal val PLAYER_1_B = Position("p1b")
        internal val PLAYER_2_B = Position("p2b")
        internal val PLAYER_1_C = Position("p1c")
        internal val PLAYER_2_C = Position("p2c")

        fun create(format: String) = when (format) {
            "singles" -> MutableSingles.Impl()
            "doubles" -> MutableDoubles.Impl()
            "triples" -> MutableTriples.Impl()
            "multi" -> MutableMulti.Impl()
            "freeforall" -> MutableFreeForAll.Impl()
            else -> throw IllegalArgumentException("Invalid Battle format: $format")
        }
    }


    data class Triples(
        val player1A: ActivePokemon,
        val player1B: ActivePokemon,
        val player1C: ActivePokemon,
        val player2A: ActivePokemon,
        val player2B: ActivePokemon,
        val player2C: ActivePokemon
    ) : Battle {
        override fun get(position: Position): ActivePokemon {
            return when (position) {
                PLAYER_1_A -> player1A
                PLAYER_1_B -> player1B
                PLAYER_1_C -> player1C
                PLAYER_2_A -> player2A
                PLAYER_2_B -> player2B
                PLAYER_2_C -> player2C
                else -> throw IllegalArgumentException("Invalid Position: $position")
            }
        }
    }

    data class Multi(
        val player1A: ActivePokemon,
        val player2A: ActivePokemon,
        val player3B: ActivePokemon,
        val player4B: ActivePokemon
    ) : Battle {
        override fun get(position: Position): ActivePokemon {
            return when (position) {
                PLAYER_1_A -> player1A
                PLAYER_2_A -> player2A
                PLAYER_3_B -> player3B
                PLAYER_4_B -> player4B
                else -> throw IllegalArgumentException("Invalid Position: $position")
            }
        }
    }

    data class FreeForAll(
        val player1A: ActivePokemon,
        val player2A: ActivePokemon,
        val player3B: ActivePokemon,
        val player4B: ActivePokemon
    ) : Battle {
        override fun get(position: Position): ActivePokemon {
            return when (position) {
                PLAYER_1_A -> player1A
                PLAYER_2_A -> player2A
                PLAYER_3_B -> player3B
                PLAYER_4_B -> player4B
                else -> throw IllegalArgumentException("Invalid Position: $position")
            }
        }
    }

    abstract operator fun get(position: Position): ActivePokemon
}

sealed interface MutableBattle : Battle {
    abstract operator fun set(position: Position, activePokemon: ActivePokemon)
}

interface Singles : Battle {
    val player1: ActivePokemon
    val player2: ActivePokemon
    fun toImmutable() = Impl(player1, player2)
    fun toMutable() = MutableSingles.Impl(player1, player2)
    data class Impl(
        override val player1: ActivePokemon = ActivePokemon(),
        override val player2: ActivePokemon = ActivePokemon()
    ) : Singles {
        override fun get(position: Position): ActivePokemon {
            return when (position) {
                Battle.PLAYER_1_A -> player1
                Battle.PLAYER_2_A -> player2
                else -> throw IllegalArgumentException("Invalid Position: $position")
            }
        }
    }
}

interface MutableSingles : Singles, MutableBattle {
    override var player1: ActivePokemon
    override var player2: ActivePokemon

    data class Impl(
        override var player1: ActivePokemon = ActivePokemon(),
        override var player2: ActivePokemon = ActivePokemon()
    ) : MutableSingles {
        override fun get(position: Position): ActivePokemon {
            return when (position) {
                Battle.PLAYER_1_A -> player1
                Battle.PLAYER_2_A -> player2
                else -> throw IllegalArgumentException("Invalid Position: $position")
            }
        }

        override fun set(position: Position, activePokemon: ActivePokemon) {
            when (position) {
                Battle.PLAYER_1_A -> player1 = activePokemon
                Battle.PLAYER_2_A -> player2 = activePokemon
                else -> throw IllegalArgumentException("Invalid Position: $position")
            }
        }
    }
}

interface Doubles : Battle {
    val player1A: ActivePokemon
    val player1B: ActivePokemon
    val player2A: ActivePokemon
    val player2B: ActivePokemon
    fun toImmutable() = Impl(player1A, player1B, player2A, player2B)
    fun toMutable() = MutableDoubles.Impl(player1A, player1B, player2A, player2B)
    data class Impl(
        override val player1A: ActivePokemon = ActivePokemon(),
        override val player1B: ActivePokemon = ActivePokemon(),
        override val player2A: ActivePokemon = ActivePokemon(),
        override val player2B: ActivePokemon = ActivePokemon()
    ) : Doubles {
        override fun get(position: Position): ActivePokemon {
            return when (position) {
                Battle.PLAYER_1_A -> player1A
                Battle.PLAYER_1_B -> player1B
                Battle.PLAYER_2_A -> player2A
                Battle.PLAYER_2_B -> player2B
                else -> throw IllegalArgumentException("Invalid Position: $position")
            }
        }
    }
}

interface MutableDoubles : Doubles, MutableBattle {
    override var player1A: ActivePokemon
    override var player1B: ActivePokemon
    override var player2A: ActivePokemon
    override var player2B: ActivePokemon

    data class Impl(
        override var player1A: ActivePokemon = ActivePokemon(),
        override var player1B: ActivePokemon = ActivePokemon(),
        override var player2A: ActivePokemon = ActivePokemon(),
        override var player2B: ActivePokemon = ActivePokemon()
    ) : MutableDoubles {
        override fun get(position: Position): ActivePokemon {
            return when (position) {
                Battle.PLAYER_1_A -> player1A
                Battle.PLAYER_1_B -> player1B
                Battle.PLAYER_2_A -> player2A
                Battle.PLAYER_2_B -> player2B
                else -> throw IllegalArgumentException("Invalid Position: $position")
            }
        }

        override fun set(position: Position, activePokemon: ActivePokemon) {
            when (position) {
                Battle.PLAYER_1_A -> player1A = activePokemon
                Battle.PLAYER_1_B -> player1B = activePokemon
                Battle.PLAYER_2_A -> player2A = activePokemon
                Battle.PLAYER_2_B -> player2B = activePokemon
                else -> throw IllegalArgumentException("Invalid Position: $position")
            }
        }
    }
}

interface Triples : Battle {
    val player1A: ActivePokemon
    val player1B: ActivePokemon
    val player1C: ActivePokemon
    val player2A: ActivePokemon
    val player2B: ActivePokemon
    val player2C: ActivePokemon
    fun toImmutable() = Impl(player1A, player1B, player1C, player2A, player2B, player2C)
    fun toMutable() = MutableTriples.Impl(player1A, player1B, player1C, player2A, player2B, player2C)
    data class Impl(
        override val player1A: ActivePokemon = ActivePokemon(),
        override val player1B: ActivePokemon = ActivePokemon(),
        override val player1C: ActivePokemon = ActivePokemon(),
        override val player2A: ActivePokemon = ActivePokemon(),
        override val player2B: ActivePokemon = ActivePokemon(),
        override val player2C: ActivePokemon = ActivePokemon()
    ) : Triples {
        override fun get(position: Position): ActivePokemon {
            return when (position) {
                Battle.PLAYER_1_A -> player1A
                Battle.PLAYER_1_B -> player1B
                Battle.PLAYER_1_C -> player1C
                Battle.PLAYER_2_A -> player2A
                Battle.PLAYER_2_B -> player2B
                Battle.PLAYER_2_C -> player2C
                else -> throw IllegalArgumentException("Invalid Position: $position")
            }
        }
    }
}

interface MutableTriples : Triples, MutableBattle {
    override var player1A: ActivePokemon
    override var player1B: ActivePokemon
    override var player1C: ActivePokemon
    override var player2A: ActivePokemon
    override var player2B: ActivePokemon
    override var player2C: ActivePokemon

    data class Impl(
        override var player1A: ActivePokemon = ActivePokemon(),
        override var player1B: ActivePokemon = ActivePokemon(),
        override var player1C: ActivePokemon = ActivePokemon(),
        override var player2A: ActivePokemon = ActivePokemon(),
        override var player2B: ActivePokemon = ActivePokemon(),
        override var player2C: ActivePokemon = ActivePokemon()
    ) : MutableTriples {
        override fun get(position: Position): ActivePokemon {
            return when (position) {
                Battle.PLAYER_1_A -> player1A
                Battle.PLAYER_1_B -> player1B
                Battle.PLAYER_1_C -> player1C
                Battle.PLAYER_2_A -> player2A
                Battle.PLAYER_2_B -> player2B
                Battle.PLAYER_2_C -> player2C
                else -> throw IllegalArgumentException("Invalid Position: $position")
            }
        }

        override fun set(position: Position, activePokemon: ActivePokemon) {
            when (position) {
                Battle.PLAYER_1_A -> player1A = activePokemon
                Battle.PLAYER_1_B -> player1B = activePokemon
                Battle.PLAYER_1_C -> player1C = activePokemon
                Battle.PLAYER_2_A -> player2A = activePokemon
                Battle.PLAYER_2_B -> player2B = activePokemon
                Battle.PLAYER_2_C -> player2C = activePokemon
                else -> throw IllegalArgumentException("Invalid Position: $position")
            }
        }
    }
}

interface Multi : Battle {
    val player1A: ActivePokemon
    val player3B: ActivePokemon
    val player2A: ActivePokemon
    val player4B: ActivePokemon
    fun toImmutable() = Impl(player1A, player3B, player2A, player4B)
    fun toMutable() = MutableMulti.Impl(player1A, player3B, player2A, player4B)
    data class Impl(
        override val player1A: ActivePokemon = ActivePokemon(),
        override val player3B: ActivePokemon = ActivePokemon(),
        override val player2A: ActivePokemon = ActivePokemon(),
        override val player4B: ActivePokemon = ActivePokemon()
    ) : Multi {
        override fun get(position: Position): ActivePokemon {
            return when (position) {
                Battle.PLAYER_1_A -> player1A
                Battle.PLAYER_3_B -> player3B
                Battle.PLAYER_2_A -> player2A
                Battle.PLAYER_4_B -> player4B
                else -> throw IllegalArgumentException("Invalid Position: $position")
            }
        }
    }
}

interface MutableMulti : Multi, MutableBattle {
    override var player1A: ActivePokemon
    override var player3B: ActivePokemon
    override var player2A: ActivePokemon
    override var player4B: ActivePokemon

    data class Impl(
        override var player1A: ActivePokemon = ActivePokemon(),
        override var player3B: ActivePokemon = ActivePokemon(),
        override var player2A: ActivePokemon = ActivePokemon(),
        override var player4B: ActivePokemon = ActivePokemon()
    ) : MutableMulti {
        override fun get(position: Position): ActivePokemon {
            return when (position) {
                Battle.PLAYER_1_A -> player1A
                Battle.PLAYER_3_B -> player3B
                Battle.PLAYER_2_A -> player2A
                Battle.PLAYER_4_B -> player4B
                else -> throw IllegalArgumentException("Invalid Position: $position")
            }
        }

        override fun set(position: Position, activePokemon: ActivePokemon) {
            when (position) {
                Battle.PLAYER_1_A -> player1A = activePokemon
                Battle.PLAYER_3_B -> player3B = activePokemon
                Battle.PLAYER_2_A -> player2A = activePokemon
                Battle.PLAYER_4_B -> player4B = activePokemon
                else -> throw IllegalArgumentException("Invalid Position: $position")
            }
        }

    }
}

interface FreeForAll : Battle {
    val player1A: ActivePokemon
    val player3B: ActivePokemon
    val player2A: ActivePokemon
    val player4B: ActivePokemon
    fun toImmutable() = Impl(player1A, player3B, player2A, player4B)
    fun toMutable() = MutableFreeForAll.Impl(player1A, player3B, player2A, player4B)
    data class Impl(
        override val player1A: ActivePokemon = ActivePokemon(),
        override val player3B: ActivePokemon = ActivePokemon(),
        override val player2A: ActivePokemon = ActivePokemon(),
        override val player4B: ActivePokemon = ActivePokemon()
    ) : FreeForAll {
        override fun get(position: Position): ActivePokemon {
            return when (position) {
                Battle.PLAYER_1_A -> player1A
                Battle.PLAYER_3_B -> player3B
                Battle.PLAYER_2_A -> player2A
                Battle.PLAYER_4_B -> player4B
                else -> throw IllegalArgumentException("Invalid Position: $position")
            }
        }
    }
}

interface MutableFreeForAll : FreeForAll, MutableBattle {
    override var player1A: ActivePokemon
    override var player3B: ActivePokemon
    override var player2A: ActivePokemon
    override var player4B: ActivePokemon

    data class Impl(
        override var player1A: ActivePokemon = ActivePokemon(),
        override var player3B: ActivePokemon = ActivePokemon(),
        override var player2A: ActivePokemon = ActivePokemon(),
        override var player4B: ActivePokemon = ActivePokemon()
    ) : MutableFreeForAll {
        override fun get(position: Position): ActivePokemon {
            return when (position) {
                Battle.PLAYER_1_A -> player1A
                Battle.PLAYER_3_B -> player3B
                Battle.PLAYER_2_A -> player2A
                Battle.PLAYER_4_B -> player4B
                else -> throw IllegalArgumentException("Invalid Position: $position")
            }
        }

        override fun set(position: Position, activePokemon: ActivePokemon) {
            when (position) {
                Battle.PLAYER_1_A -> player1A = activePokemon
                Battle.PLAYER_3_B -> player3B = activePokemon
                Battle.PLAYER_2_A -> player2A = activePokemon
                Battle.PLAYER_4_B -> player4B = activePokemon
                else -> throw IllegalArgumentException("Invalid Position: $position")
            }
        }

    }
}


data class ActivePokemon(
    val pokemonIndex: Int = -1,
    val playerIdentifier: PlayerIdentifier = PlayerIdentifier.NONE
)


interface Player {
    val username: String
    val rating: Int
    val avatar: String
    val team: List<Pokemon>
    fun toImmutable() = Impl(username, rating, avatar, team)
    fun toMutable() = MutablePlayer.Impl(username, rating, avatar, team.toMutableList())
    data class Impl(
        override val username: String = "",
        override val rating: Int = -1,
        override val avatar: String = "",
        override val team: List<Pokemon> = listOf(
            Pokemon.UNKNOWN,
            Pokemon.UNKNOWN,
            Pokemon.UNKNOWN,
            Pokemon.UNKNOWN,
            Pokemon.UNKNOWN,
            Pokemon.UNKNOWN
        )
    ) : Player
}

interface MutablePlayer : Player {
    override var username: String
    override var rating: Int
    override var avatar: String
    override val team: MutableList<Pokemon>

    data class Impl(
        override var username: String = "",
        override var rating: Int = -1,
        override var avatar: String = "",
        override val team: MutableList<Pokemon> = arrayListOf(
            Pokemon.UNKNOWN,
            Pokemon.UNKNOWN,
            Pokemon.UNKNOWN,
            Pokemon.UNKNOWN,
            Pokemon.UNKNOWN,
            Pokemon.UNKNOWN
        )
    ) : MutablePlayer
}

data class Pokemon(
    override val species: String,
    override val level: Int = 100,
    override val gender: Details.Gender = Details.Gender.GENDERLESS,
    override val shiny: Boolean = false,
    val health: Float = 1f,
    val status: PokemonStatus = PokemonStatus.NONE,
    val item: String = "",
    val name: String = ""
) : Details {
    constructor(
        details: Details,
        health: Float = 1f,
        status: PokemonStatus = PokemonStatus.NONE,
        item: String = "",
        name: String = ""
    ) : this(details.species, details.level, details.gender, details.shiny, health, status, item, name)

    companion object {
        val UNKNOWN = Pokemon("")
    }
}