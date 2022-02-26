package com.shop.routes

import com.shop.models.Product
import com.shop.tables.ProductTable
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

fun isElementInProductTable(id: Int): Boolean {
    var isElementInTable = true
    transaction {
        if(ProductTable.select(ProductTable.id eq id).empty()) {
            isElementInTable = false
        }
    }
    return isElementInTable
}

fun Application.registerProductRoutes() {
    routing {
        productRouting()
    }
}

fun Route.productRouting() {
    route("/product") {
        get {
            val result = mutableListOf<Product>()
            transaction {
                ProductTable.selectAll().forEach() {
                    result.add(
                        Product(
                            it[ProductTable.id],
                            it[ProductTable.name],
                            it[ProductTable.description],
                            it[ProductTable.price],
                            it[ProductTable.isAvailable]
                        )
                    )
                }
            }
            call.respond(result)
        }
        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText("Missing ID", status = HttpStatusCode.BadRequest)
            var result = mutableListOf<Product>()
            if(isElementInProductTable(Integer.parseInt(id))) {
                transaction {
                    var row = ProductTable.select({ ProductTable.id eq Integer.parseInt(id) }).first()
                    result.add(
                        Product(
                            row[ProductTable.id],
                            row[ProductTable.name],
                            row[ProductTable.description],
                            row[ProductTable.price],
                            row[ProductTable.isAvailable]
                        )
                    )
                }
                return@get call.respond(result)
            }
            return@get call.respondText("No product found with given id: $id", status = HttpStatusCode.NotFound)
        }
        put {
            val product = call.receive(Product::class)
            if (isElementInProductTable(product.id)) {
                transaction {
                    ProductTable.update({ ProductTable.id eq product.id }) {
                        it[id] = product.id
                        it[description] = product.description
                        it[name] = product.name
                        it[price] = product.price
                        it[isAvailable] = product.isAvailable
                    }
                }
            } else {
                transaction {
                    ProductTable.insert {
                        it[id] = product.id
                        it[description] = product.description
                        it[name] = product.name
                        it[price] = product.price
                        it[isAvailable] = product.isAvailable
                    }
                }

            }
            call.respondText("Product with id: ${product.id} updated correctly", status = HttpStatusCode.Accepted)
        }
        post {
            val product = call.receive(Product::class)
            if(isElementInProductTable(product.id)) {
                return@post call.respondText("Product with id: ${product.id} was already added into database", status = HttpStatusCode.BadRequest)
            }
            transaction {
                ProductTable.insert {
                    it[id] = product.id
                    it[description] = product.description
                    it[name] = product.name
                    it[price] = product.price
                    it[isAvailable] = product.isAvailable
                }
            }
            return@post call.respondText("Product with id: ${product.id} posted correctly", status = HttpStatusCode.Created)
        }
        delete("/{id}") {
            val id = call.parameters["id"] ?: return@delete call.respondText("Missing ID", status = HttpStatusCode.BadRequest)

            if(isElementInProductTable(Integer.parseInt(id))) {
                transaction {
                    ProductTable.deleteWhere {
                        ProductTable.id eq Integer.parseInt(id)
                    }
                }
                call.respondText("Product with id: $id deleted correctly", status = HttpStatusCode.Accepted)
            }
            else {
                call.respondText("No product found with given id: $id", status = HttpStatusCode.NotFound)
            }
        }
    }
}