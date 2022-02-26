package pl.edu.uj.wieliczko.shopapplication.tables

import org.jetbrains.exposed.sql.Table

object UserTable : Table("users") {
    val nickname = varchar("nickname", 50).uniqueIndex()
    val name = varchar("name", 50)
    val surname = varchar("surname", 50)
    val password = varchar("password", 50)
}