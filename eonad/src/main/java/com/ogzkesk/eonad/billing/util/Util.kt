package com.ogzkesk.eonad.billing.util

import com.android.billingclient.api.*
import com.ogzkesk.eonad.billing.IapResult
import com.ogzkesk.eonad.billing.ProductDetail
import com.ogzkesk.eonad.billing.PurchaseHistory
import com.ogzkesk.eonad.billing.PurchaseIap
import com.ogzkesk.eonad.billing.util.Constants.EMPTY_PRICE

internal fun BillingResult.checkResponse() : Boolean {
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

internal fun ProductDetails.mapToProductDetail() : ProductDetail {
    val formattedPrice = this.subscriptionOfferDetails?.map { details ->
        details.pricingPhases.pricingPhaseList.map {
            it.formattedPrice
        }.first()
    }?.first() ?: this.oneTimePurchaseOfferDetails?.formattedPrice ?: EMPTY_PRICE
    val price = this.subscriptionOfferDetails?.map { details ->
        details.pricingPhases.pricingPhaseList.map {
            it.priceAmountMicros
        }.first()
    }?.first() ?: this.oneTimePurchaseOfferDetails?.priceAmountMicros ?: 0


    val productId = this.productId
    val name = this.name
    val description = this.description
    val type = if(this.productType == BillingClient.ProductType.SUBS){
        ProductDetail.TYPE_PRODUCT
    } else {
        ProductDetail.TYPE_SUBSCRIPTION
    }

    return ProductDetail(productId,name,description,formattedPrice,price,type)

}

internal fun List<ProductDetails>.mapToProductDetail(): List<ProductDetail> {
    return this.map { it.mapToProductDetail() }
}

internal fun Purchase.mapToPurchaseIap() : PurchaseIap {
    return PurchaseIap(this)
}

internal fun List<Purchase>.mapToPurchaseIap() : List<PurchaseIap> {
    return this.map { it.mapToPurchaseIap() }
}

internal fun PurchaseHistoryRecord.toPurchaseHistory() : PurchaseHistory {
    return PurchaseHistory(this)
}

internal fun List<PurchaseHistoryRecord>.toPurchaseHistory() : List<PurchaseHistory> {
    return this.map { it.toPurchaseHistory() }
}

internal fun BillingResult.toIapResult() : IapResult {
    return IapResult(this)
}

internal fun <T: Any> List<T>?.toNonNull(): List<T> {
    return this ?: emptyList()
}

internal fun mapPurchaseTypeOf(
    base: List<ProductDetails>,
    purchases: List<Purchase>?,
) : Boolean {

    if(purchases.isNullOrEmpty() || base.isEmpty()) return false

    val products = purchases.first().products

    if(products.isEmpty()) return false

    return base.any { b ->
        purchases.any { purchase ->
            purchase.products.any { id ->
                id == b.productId
            }
        }
    }

}