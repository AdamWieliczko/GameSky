package pl.edu.uj.wieliczko.shopapplication.tables

import org.jetbrains.exposed.sql.Table

object CategoryTable: Table("categoryTable") {
    val categoryName = varchar("categoryName", 50)
    val productID = integer("productID").references(ProductTable.id)
}