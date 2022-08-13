package github.nwn.jubilife.database.models

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Table

object User : UUIDTable() {
    val email = varchar("email", 320)

}