package com.shop.tables

import org.jetbrains.exposed.sql.Table

object WishlistTable: Table("wishlistTable") {
    val user = varchar("user", 50).references(UserTable.nickname)
    val product = integer("productID").references(ProductTable.id)
}