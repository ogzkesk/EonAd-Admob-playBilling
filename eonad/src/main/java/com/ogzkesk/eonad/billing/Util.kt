package com.ogzkesk.eonad.billing

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.ogzkesk.eonad.billing.Constants.EMPTY_PRICE

internal fun BillingResult.checkResponse() : Boolean{
    return this.responseCode == BillingClient.BillingResponseCode.OK
}

data class PurchaseItem(
    val id: String,
    val type: String
) {
    companion object{
        const val TYPE_SUBSCRIPTION = "subscription"
        const val TYPE_PRODUCT = "product"
    }
}

internal fun ProductDetails.mapToProductDetail() : ProductDetail{
    val price = this.subscriptionOfferDetails?.map { details ->
        details.pricingPhases.pricingPhaseList.map {
            it.formattedPrice
        }.first()
    }?.first() ?: EMPTY_PRICE

    val productId = this.productId
    val name = this.name
    val description = this.description

    return ProductDetail(productId,name,description,price)

}

internal fun List<ProductDetails>.mapToProductDetail(): List<ProductDetail> {
    return this.map { it.mapToProductDetail() }
}

fun BillingClient.checkConnection() : Boolean {
    return !(this.connectionState != BillingClient.ConnectionState.CONNECTED ||
            this.connectionState != BillingClient.ConnectionState.CONNECTING)
}