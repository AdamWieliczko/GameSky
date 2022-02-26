package com.shop.routes

import com.shop.models.User
import com.shop.tables.UserTable
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

fun isElementInUserTable(nickname: String): Boolean {
    var isElementInTable = true
    transaction {
        if(UserTable.select(UserTable.nickname eq nickname).empty()) {
            isElementInTable = false
        }
    }
    return isElementInTable
}

fun Application.registerUserRoutes() {
    routing {
        userRouting()
    }
}

fun Route.userRouting() {
    route("/user") {
        get {
            val result = mutableListOf<User>()
            transaction {
                UserTable.selectAll().forEach() {
                    result.add(
                        User(
                            it[UserTable.nickname],
                            it[UserTable.name],
                            it[UserTable.surname],
                            it[UserTable.password]
                        )
                    )
                }
            }
            call.respond(result)
        }
        get("/{nickname}") {
            val nickname = call.parameters["nickname"] ?: return@get call.respondText("Missing nickname", status = HttpStatusCode.BadRequest)
            var result = mutableListOf<User>()
            if(isElementInUserTable(nickname)) {
                transaction {
                    var row = UserTable.select({ UserTable.nickname eq nickname }).first()
                    result.add(
                        User(
                            row[UserTable.nickname],
                            row[UserTable.name],
                            row[UserTable.surname],
                            row[UserTable.password]
                        )
                    )
                }
                return@get call.respond(result)
            }
            return@get call.respondText("No user found with given nickname: $nickname", status = HttpStatusCode.NotFound)
        }
        put {
            val user = call.receive(User::class)
            if (isElementInUserTable(user.nickname)) {
                transaction {
                    UserTable.update({ UserTable.nickname eq user.nickname }) {
                        it[nickname] = user.nickname
                        it[name] = user.name
                        it[surname] = user.surname
                        it[password] = user.password
                    }
                }
            } else {
                transaction {
                    UserTable.insert {
                        it[nickname] = user.nickname
                        it[name] = user.name
                        it[surname] = user.surname
                        it[password] = user.password
                    }
                }
            }
            call.respondText("User with nickname: ${user.nickname} updated correctly", status = HttpStatusCode.Accepted)
        }
        post {
            val user = call.receive(User::class)
            if(isElementInUserTable(user.nickname)) {
                return@post call.respondText("User with nickname: ${user.nickname} was already added into database", status = HttpStatusCode.BadRequest)
            }
            transaction {
                UserTable.insert {
                    it[nickname] = user.nickname
                    it[name] = user.name
                    it[surname] = user.surname
                    it[password] = user.password
                }
            }
            return@post call.respondText("User with nickname: ${user.nickname} posted correctly", status = HttpStatusCode.Created)
        }
        delete("/{nickname}") {
            val nickname = call.parameters["nickname"] ?: return@delete call.respondText("Missing nickname", status = HttpStatusCode.BadRequest)

            if(isElementInUserTable(nickname)) {
                transaction {
                    UserTable.deleteWhere {
                        UserTable.nickname eq nickname
                    }
                }
                call.respondText("User with nickname: $nickname deleted correctly", status = HttpStatusCode.Accepted)
            }
            else {
                call.respondText("No user found with given nickname: $nickname", status = HttpStatusCode.NotFound)
            }
        }
    }
}