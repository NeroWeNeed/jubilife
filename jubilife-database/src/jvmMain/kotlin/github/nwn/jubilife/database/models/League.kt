package github.nwn.jubilife.database.models

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

enum class BracketFormat {
    ROUND_ROBIN,
    SWISS,
    SINGLE_ELIMINATION,
    DOUBLE_ELIMINATION
}

object League : UUIDTable() {
    val owner = reference("owner", User.id)
    val name = varchar("name", 32)
}

object LeagueEntrants : Table() {
    val league = reference("league", League.id, onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE)
    val user = reference("user", User.id, onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE)
    val name = varchar("name", 32)
}

object LeaguePhases : Table() {
    val league = reference("league", League.id, onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE)
    val phaseNumber = byte("phase_number")
    val phaseFormat = enumeration<BracketFormat>("phase_format")
    val phaseCutOff = integer("phase_cutoff")
    val phaseRounds = byte("phase_rounds")

}

