package com.shop.routes

import com.shop.models.Wishlist
import com.shop.tables.WishlistTable
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

fun isElementInWishlistTable(user: String): Boolean {
    var isElementInTable = true
    transaction {
        if (WishlistTable.select(WishlistTable.user eq user).empty()) {
            isElementInTable = false
        }
    }

    return isElementInTable
}

fun Application.registerWishlistRoutes() {
    routing {
        wishlistRouting()
    }
}

fun Route.wishlistRouting() {
    route("/wishlist") {
        get {
            val result = mutableListOf<Wishlist>()
            transaction {
                WishlistTable.selectAll().forEach() {
                    result.add(
                        Wishlist(
                            it[WishlistTable.user],
                            it[WishlistTable.product]
                        )
                    )
                }
            }
            call.respond(result)
        }
        get("/{user}") {
            val user = call.parameters["user"] ?: return@get call.respondText("Missing user", status = HttpStatusCode.BadRequest)
            val result = mutableListOf<Wishlist>()
            if(isElementInWishlistTable(user)) {
                transaction {
                    WishlistTable.select({ WishlistTable.user eq user}).forEach(){
                        result.add(
                            Wishlist(
                                it[WishlistTable.user],
                                it[WishlistTable.product]
                            )
                        )
                    }
                }
                return@get call.respond(result)
            }
            return@get call.respondText("No wishlist found for given user: $user", status = HttpStatusCode.NotFound)
        }
        put {
            val wishlist = call.receive(Wishlist::class)
            if (isElementInWishlistTable(wishlist.user)) {
                transaction {
                    WishlistTable.update({ WishlistTable.user eq wishlist.user }) {
                        it[user] = wishlist.user
                        it[product] = wishlist.product
                    }
                }
            } else {
                transaction {
                    WishlistTable.insert {
                        it[user] = wishlist.user
                        it[product] = wishlist.product
                    }
                }
            }
            call.respondText("Wishlist with given user: ${wishlist.user} updated correctly", status = HttpStatusCode.Accepted)
        }
        post {
            val wishlist = call.receive(Wishlist::class)
            if(isElementInWishlistTable(wishlist.user)) {
                return@post call.respondText("Wishlist with given user: ${wishlist.user} was already added into database", status = HttpStatusCode.BadRequest)
            }
            transaction {
                WishlistTable.insert {
                    it[user] = wishlist.user
                    it[product] = wishlist.product
                }
            }
            return@post call.respondText("Wishlist with given user: ${wishlist.user} posted correctly", status = HttpStatusCode.Created)
        }
        delete("/{user}") {
            val user = call.parameters["user"] ?: return@delete call.respondText("Missing user", status = HttpStatusCode.BadRequest)

            if(isElementInWishlistTable(user)) {
                transaction {
                    WishlistTable.deleteWhere {
                        WishlistTable.user eq user
                    }
                }
                call.respondText("Wishlist with given user: $user deleted correctly", status = HttpStatusCode.Accepted)
            }
            else {
                call.respondText("No wishlist found with given user: $user", status = HttpStatusCode.NotFound)
            }
        }
    }
}