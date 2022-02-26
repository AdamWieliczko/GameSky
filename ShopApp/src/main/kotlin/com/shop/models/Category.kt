package com.shop.models

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val categoryName: String,
    val productID: Int
)
