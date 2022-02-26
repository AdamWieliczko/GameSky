package com.shop.models

import kotlinx.serialization.Serializable

@Serializable
data class ShoppingCart(
    val user: String,
    val product: Int
    )