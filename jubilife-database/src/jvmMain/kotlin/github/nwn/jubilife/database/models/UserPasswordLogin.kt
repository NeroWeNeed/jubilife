package github.nwn.jubilife.database.models

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object UserPasswordLogin : Table() {
    val userId = reference("id", User, onDelete = ReferenceOption.CASCADE)
    val value = binary("value", 64)
    override val primaryKey = PrimaryKey(firstColumn = userId)
}