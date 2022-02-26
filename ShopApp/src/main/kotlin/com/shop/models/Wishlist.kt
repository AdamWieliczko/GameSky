package com.shop.models

import kotlinx.serialization.Serializable

@Serializable
data class Wishlist(
    val user: String,
    val product: Int
)
