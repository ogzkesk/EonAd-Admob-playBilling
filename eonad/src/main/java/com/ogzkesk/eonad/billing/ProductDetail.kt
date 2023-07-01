package com.ogzkesk.eonad.billing

data class ProductDetail(
    val productId: String,
    val name: String,
    val description : String,
    val formattedPrice: String
)
