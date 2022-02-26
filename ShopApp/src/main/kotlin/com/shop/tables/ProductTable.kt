package com.shop.tables

import org.jetbrains.exposed.sql.Table

object ProductTable : Table("products") {
    val id = integer("id").uniqueIndex()
    val name = varchar("name", 50)
    val description = varchar("description", 250)
    val price = integer("price")
    val isAvailable = bool("isAvailable")
}