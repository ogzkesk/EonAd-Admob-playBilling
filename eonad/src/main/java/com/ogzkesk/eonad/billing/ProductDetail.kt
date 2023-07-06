package com.ogzkesk.eonad.billing

data class ProductDetail(
    val productId: String,
    val name: String,
    val description: String,
    val formattedPrice: String,
    val price: Long,
    val type: String
) {
    companion object {
        const val TYPE_SUBSCRIPTION = "subscription"
        const val TYPE_PRODUCT = "product"
    }
}
