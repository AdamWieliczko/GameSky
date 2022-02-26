package com.shop.routes

import com.shop.models.Category
import com.shop.tables.CategoryTable
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

fun isElementInCategoryTable(name: String): Boolean {
    var isElementInTable = true
    transaction {
        if(CategoryTable.select(CategoryTable.categoryName eq name).empty()) {
            isElementInTable = false
        }
    }
    return isElementInTable
}

fun Application.registerCategoryRoutes() {
    routing {
        categoryRouting()
    }
}

fun Route.categoryRouting() {
    route("/category") {
        get {
            val result = mutableListOf<Category>()
            transaction {
                CategoryTable.selectAll().forEach() {
                    result.add(
                        Category(
                            it[CategoryTable.categoryName],
                            it[CategoryTable.productID]
                        )
                    )
                }
            }
            call.respond(result)
        }
        get("/{categoryName}") {
            val categoryName = call.parameters["categoryName"] ?: return@get call.respondText("Missing categoryName", status = HttpStatusCode.BadRequest)
            val result = mutableListOf<Category>()
            if(isElementInCategoryTable(categoryName)) {
                transaction {
                    CategoryTable.select({CategoryTable.categoryName eq categoryName}).forEach(){
                        result.add(
                            Category(
                                it[CategoryTable.categoryName],
                                it[CategoryTable.productID]
                            )
                        )
                    }
                }
                return@get call.respond(result)
            }
            return@get call.respondText("No category found with given categoryName: $categoryName", status = HttpStatusCode.NotFound)
        }
        put {
            val category = call.receive(Category::class)
            if (isElementInCategoryTable(category.categoryName)) {
                transaction {
                    CategoryTable.update({ CategoryTable.categoryName eq category.categoryName }) {
                        it[categoryName] = category.categoryName
                        it[productID] = category.productID
                    }
                }
            } else {
                transaction {
                    CategoryTable.insert {
                        it[categoryName] = category.categoryName
                        it[productID] = category.productID
                    }
                }
            }
            call.respondText("Category with categoryName: ${category.categoryName} updated correctly", status = HttpStatusCode.Accepted)
        }
        post {
            val category = call.receive(Category::class)
            if(isElementInCategoryTable(category.categoryName)) {
                return@post call.respondText("Category with categoryName: ${category.categoryName} was already added into database", status = HttpStatusCode.BadRequest)
            }
            transaction {
                CategoryTable.insert {
                    it[categoryName] = category.categoryName
                    it[productID] = category.productID
                }
            }
            return@post call.respondText("Category with categoryName: ${category.categoryName} posted correctly", status = HttpStatusCode.Created)
        }
        delete("/{categoryName}") {
            val categoryName = call.parameters["categoryName"] ?: return@delete call.respondText("Missing categoryName", status = HttpStatusCode.BadRequest)

            if(isElementInCategoryTable(categoryName)) {
                transaction {
                    CategoryTable.deleteWhere {
                        CategoryTable.categoryName eq categoryName
                    }
                }
                call.respondText("Category with categoryName: $categoryName deleted correctly", status = HttpStatusCode.Accepted)
            }
            else {
                call.respondText("No category found with given categoryName: $categoryName", status = HttpStatusCode.NotFound)
            }
        }
    }
}