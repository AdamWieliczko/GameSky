package com.shop.routes

import com.google.gson.Gson
import com.shop.models.ShoppingCart
import com.shop.stripeSK
import com.shop.tables.ShoppingCartTable
import com.shop.tables.ShoppingCartTable.product
import com.shop.tables.ShoppingCartTable.user
import com.stripe.Stripe
import com.stripe.model.PaymentIntent
import com.stripe.model.terminal.ConnectionToken
import com.stripe.param.PaymentIntentCreateParams
import com.stripe.param.terminal.ConnectionTokenCreateParams
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction


fun isElementInShoppingCartTable(user: String): Boolean {
    var isElementInTable = true
    transaction {
        if (ShoppingCartTable.select(ShoppingCartTable.user eq user).empty()) {
            isElementInTable = false
        }
    }
    return isElementInTable
}

fun Application.registerShoppingCartRoutes() {
    routing {
        shoppingCartRouting()
    }
}

fun Route.shoppingCartRouting() {
    route("/shoppingCart") {
        get {
            val result = mutableListOf<ShoppingCart>()
            transaction {
                ShoppingCartTable.selectAll().forEach() {
                    result.add(
                        ShoppingCart(
                            it[user],
                            it[product]
                        )
                    )
                }
            }
            call.respond(result)
        }
        get("/{user}") {
            val user = call.parameters["user"] ?: return@get call.respondText("Missing user", status = HttpStatusCode.BadRequest)
            val result = mutableListOf<ShoppingCart>()
            if(isElementInShoppingCartTable(user)) {
                transaction {
                    ShoppingCartTable.select({ ShoppingCartTable.user eq user}).forEach(){
                        result.add(
                            ShoppingCart(
                                it[ShoppingCartTable.user],
                                it[ShoppingCartTable.product]
                            )
                        )
                    }
                }
                return@get call.respond(result)
            }
            return@get call.respondText("No shoppingCart found for given user: $user", status = HttpStatusCode.NotFound)
        }
        put {
            val shoppingCart = call.receive(ShoppingCart::class)
            if (isElementInShoppingCartTable(shoppingCart.user)) {
                transaction {
                    ShoppingCartTable.update({ ShoppingCartTable.user eq shoppingCart.user }) {
                        it[user] = shoppingCart.user
                        it[product] = shoppingCart.product
                    }
                }
            } else {
                transaction {
                    ShoppingCartTable.insert {
                        it[user] = shoppingCart.user
                        it[product] = shoppingCart.product
                    }
                }
            }
            call.respondText("ShoppingCart with given user: ${shoppingCart.user} updated correctly", status = HttpStatusCode.Accepted)
        }
        post {
            val shoppingCart = call.receive(ShoppingCart::class)
            if(isElementInShoppingCartTable(shoppingCart.user)) {
                return@post call.respondText("ShoppingCart with given user: ${shoppingCart.user} was already added into database", status = HttpStatusCode.BadRequest)
            }
            transaction {
                ShoppingCartTable.insert {
                    it[user] = shoppingCart.user
                    it[product] = shoppingCart.product
                }
            }
            return@post call.respondText("ShoppingCart with given user: ${shoppingCart.user} posted correctly", status = HttpStatusCode.Created)
        }
        delete("/{user}") {
            val user = call.parameters["user"] ?: return@delete call.respondText("Missing user", status = HttpStatusCode.BadRequest)

            if(isElementInShoppingCartTable(user)) {
                transaction {
                    ShoppingCartTable.deleteWhere {
                        ShoppingCartTable.user eq user
                    }
                }
                call.respondText("ShoppingCart with given user: $user deleted correctly", status = HttpStatusCode.Accepted)
            }
            else {
                call.respondText("No shoppingCart found with given user: $user", status = HttpStatusCode.NotFound)
            }
        }



        post("/connection_token") {
            val params = ConnectionTokenCreateParams.builder()
                .build()

            val connectionToken = ConnectionToken.create(params)

            val map = HashMap<String, String>()

            map["secret"] = connectionToken.secret

            call.respond(Gson().toJson(map))
        }
        get("/create_payment_intent/{nickname}") {
            val nickname = call.parameters["nickname"] ?: return@get call.respondText("Missing nickname", status = HttpStatusCode.BadRequest)

            var howMuchToPay = 500L

            /*val products = mutableListOf<Product>()
            if(isElementInShoppingCartTable(nickname)) {
                transaction {
                    ShoppingCartTable.select({ ShoppingCartTable.user eq user}).forEach(){
                        if(isElementInProductTable(Integer.parseInt(id))) {
                            transaction {
                                var row = ProductTable.select({ ProductTable.id eq Integer.parseInt(id) }).first()
                                products.add(
                                    Product(
                                        row[ProductTable.id],
                                        row[ProductTable.name],
                                        row[ProductTable.description],
                                        row[price],
                                        row[ProductTable.isAvailable]
                                    )
                                )
                            }
                        }
                    }
                }
            }

            products.forEach(){
                howMuchToPay += it.price
            }"*/

            Stripe.apiKey = stripeSK


            val params = PaymentIntentCreateParams.builder()
                .setCurrency("pln")
                .setAmount(howMuchToPay)
                .setCaptureMethod(PaymentIntentCreateParams.CaptureMethod.MANUAL)
                .build()
            val secret = PaymentIntent.create(params).clientSecret

            call.respond(Gson().toJson(secret))
        }
    }
}