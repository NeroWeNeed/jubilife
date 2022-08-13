package github.nwn.jubilife.database.models

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object DraftPools : Table() {
    val league = reference("league", League.id, onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE)
    val poolNumber = byte("pool_number")
    val poolBudget = integer("pool_budget")
    val poolMinItems = integer("pool_min_items")
    val poolMaxItems = integer("pool_min_items")
    val useFullBudget = bool("use_full_budget")
}

object DraftPoolItems : Table() {
    val league = reference("league", League.id, onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE)
    val poolNumber = byte("pool_number")
    val itemName = varchar("item_name",64)
    val itemPrice = integer("item_price")
}