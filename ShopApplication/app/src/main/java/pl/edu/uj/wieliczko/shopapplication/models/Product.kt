package pl.edu.uj.wieliczko.shopapplication.models

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Int,
    val isAvailable: Boolean
)