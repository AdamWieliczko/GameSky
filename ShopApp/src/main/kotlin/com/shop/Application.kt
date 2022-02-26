package com.shop

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.shop.plugins.*
import com.shop.routes.*

import com.shop.tables.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {

    Database.connect("jdbc:sqlite:database.sqlite", "org.sqlite.JDBC")

    transaction {
        SchemaUtils.create (ProductTable)
        SchemaUtils.create (UserTable)
        SchemaUtils.create (ShoppingCartTable)
        SchemaUtils.create (CategoryTable)
        SchemaUtils.create (WishlistTable)
    }

    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureSerialization()
        registerProductRoutes()
        registerUserRoutes()
        registerShoppingCartRoutes()
        registerCategoryRoutes()
        registerWishlistRoutes()
    }.start(wait = true)
}