package github.nwn.jubilife.tools.psra.protocol



interface Details {
    val species: String
    val level: Int
    val gender: Gender
    val shiny: Boolean
    val unknownForm: Boolean
        get() = species.endsWith("-*")

    companion object {
        private val regex = Regex("(.*?)(?:,\\s*L(\\d{1,2}))?(?:,\\s*([MF]))?(?:,\\s*(shiny))?\$")
        fun parse(details: String): Details {
            return regex.matchEntire(details)!!.groupValues.let { (input, species, level, gender, shiny) ->
                Impl(
                    species,
                    if (level.isNotEmpty()) level.toInt() else 100,
                    Gender.parse(gender),
                    shiny.isNotEmpty()
                )

            }
        }

        fun <T : Details> parse(
            details: String,
            builder: (species: String, level: Int, gender: Gender, shiny: Boolean) -> T
        ): T {
            return regex.matchEntire(details)!!.groupValues.let { (input, species, level, gender, shiny) ->
                builder(species,
                    if (level.isNotEmpty()) level.toInt() else 100,
                    Gender.parse(gender),
                    shiny.isNotEmpty())
            }
        }
    }

    data class Impl(
        override val species: String,
        override val level: Int,
        override val gender: Gender,
        override val shiny: Boolean
    ) : Details

    enum class Gender {
        MALE, FEMALE, GENDERLESS;

        companion object {
            fun parse(input: String) = when (input) {
                "M" -> MALE
                "F" -> FEMALE
                else -> GENDERLESS
            }
        }

    }
}