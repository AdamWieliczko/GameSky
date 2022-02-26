package pl.edu.uj.wieliczko.shopapplication.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val nickname: String,
    val name: String,
    val surname: String,
    val password: String
)