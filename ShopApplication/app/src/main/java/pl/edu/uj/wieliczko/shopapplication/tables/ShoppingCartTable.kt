package pl.edu.uj.wieliczko.shopapplication.tables

import org.jetbrains.exposed.sql.Table

object ShoppingCartTable : Table("shoppingCarts") {
    val user = varchar("user", 50).references(UserTable.nickname)
    val product = integer("productID").references(ProductTable.id)
}