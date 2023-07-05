package com.ogzkesk.eonad.billing

import android.util.Log
import com.android.billingclient.api.*
import com.google.common.collect.ImmutableList
import com.ogzkesk.eonad.billing.util.*

private const val TAG = "Iap"

internal class MIap(
    private val client: BillingClient,
    private val products: List<PurchaseItem>,
    private val subscriptionsBase: List<ProductDetails>,
    private val inAppProductsBase: List<ProductDetails>
) {


    fun getProducts(onResult: (List<ProductDetails>) -> Unit) {

        val productList = products.filter {
            it.type == PurchaseItem.TYPE_PRODUCT
        }.map {
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(it.id)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        }

        if (productList.isEmpty()) return
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        Log.d(TAG, "Querying inApp Products :: $productList")
        client.queryProductDetailsAsync(params) { result, prodDetailsList ->
            if (result.checkResponse()) {
                onResult.invoke(prodDetailsList)
            } else {
                Log.d(TAG, "getProducts() ${result.debugMessage}")
            }
        }
    }


    fun getSubs(onResult: (List<ProductDetails>) -> Unit) {

        val productList = products.filter {
            it.type == PurchaseItem.TYPE_SUBSCRIPTION
        }.map {
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(it.id)
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        }

        if (productList.isEmpty()) return

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        Log.d(TAG, "Querying Subs :: $productList")
        client.queryProductDetailsAsync(params) { result, prodDetailsList ->
            if (result.checkResponse()) {
                onResult.invoke(prodDetailsList)
            } else {
                Log.d(TAG, "getSubs() ${result.debugMessage}")
            }
        }
    }

    private fun verifyPurchase(onAcknowledged: () -> Unit) {

        val queryPurchasesParams = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()

        client.queryPurchasesAsync(queryPurchasesParams) { result, purchases ->
            if (result.checkResponse()) {
                for (purchase in purchases) {
                    if (
                        purchase.purchaseState == Purchase.PurchaseState.PURCHASED &&
                        !purchase.isAcknowledged
                    ) {
                        Log.d(TAG, "Subscription purchased!")
                        acknowledgePurchase(purchase, onAcknowledged)
                    }
                }
            }
        }
    }


    private fun acknowledgePurchase(purchases: Purchase, onAcknowledged: () -> Unit) {
        val acknowledgePurchaseParams = AcknowledgePurchaseParams
            .newBuilder()
            .setPurchaseToken(purchases.purchaseToken)
            .build()

        client.acknowledgePurchase(acknowledgePurchaseParams) { result ->
            if (result.checkResponse()) {
                onAcknowledged.invoke()
                Log.d(TAG, "Subscription verified!")
            } else {
                Log.d(TAG, "Subscription not Acknowledged! :: ${result.debugMessage}")
            }
        }
    }

    fun getProductDetailParams(details: ProductDetails): List<BillingFlowParams.ProductDetailsParams> {
        return if (details.productType == BillingClient.ProductType.SUBS) {

            val offerDetails = details.subscriptionOfferDetails ?: return emptyList()
            ImmutableList.of(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(details)
                    .setOfferToken(offerDetails.first().offerToken)
                    .build()
            )

        } else {
            ImmutableList.of(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(details)
                    .build()
            )
        }
    }

    fun getSubscriptionDetailsById(productId: String): ProductDetails? {

        if (inAppProductsBase.any { it.productId == productId }) {
            throw RuntimeException(
                "You are using Inapp Product Id for subscribe()," +
                        " please use purchase() function for that"
            )
        }

        return if (subscriptionsBase.isNotEmpty()) {
            val product = subscriptionsBase.filter { it.productId == productId }
            if (product.isNotEmpty()) product.first() else null
        } else {
            null
        }
    }

    fun getProductDetailById(productId: String): ProductDetails? {

        if (subscriptionsBase.any { it.productId == productId }) {
            throw RuntimeException(
                "You are using subscribe Id for purchase()," +
                        " please use subscribe() function for that"
            )
        }

        return if (inAppProductsBase.isNotEmpty()) {
            val product = inAppProductsBase.filter { it.productId == productId }
            if (product.isNotEmpty()) product.first() else null
        } else {
            null
        }
    }

    fun consume(
        purchase: PurchaseIap,
        onConsume: (error: String?) -> Unit
    ) {

        val consumeParams = ConsumeParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        client.consumeAsync(consumeParams) { result, _ ->

            if (result.checkResponse()) {
                Log.d(TAG, "purchase consumed!")
                onConsume.invoke(null)
                return@consumeAsync
            }

            Log.d(TAG, "purchase couldn't consumed! :: ${result.debugMessage}")
            onConsume.invoke(result.debugMessage)
        }
    }

    internal fun checkSubAndVerify(
        purchases: List<Purchase>?,
        result: BillingResult,
        onVerified: (error: String?, purchase: PurchaseResult) -> Unit,
    ) {
        if (mapPurchaseTypeOf(subscriptionsBase, purchases)) {

            verifyPurchase {
                onVerified.invoke(
                    null,
                    PurchaseResult(
                        result.toIapResult(),
                        purchases.toNonNull().mapToPurchaseIap()
                    )
                )
            }
        } else {
            onVerified.invoke(
                result.debugMessage,
                PurchaseResult(result.toIapResult(), emptyList())
            )
        }
    }

    fun checkPurchaseAndConsume(
        purchases: List<Purchase>?,
        result: BillingResult,
        onConsume: (error: String?, purchase: PurchaseResult) -> Unit
    ) {
        if (mapPurchaseTypeOf(inAppProductsBase, purchases)) {

            if (purchases.isNullOrEmpty()) return

            consume(purchases.first().mapToPurchaseIap()) { error ->

                if (error == null) {
                    onConsume.invoke(
                        null,
                        PurchaseResult(
                            result.toIapResult(),
                            purchases.mapToPurchaseIap()
                        )
                    )
                    return@consume
                }

                onConsume.invoke(
                    error,
                    PurchaseResult(result.toIapResult(), emptyList())
                )
            }
        } else {
            onConsume.invoke(
                result.debugMessage,
                PurchaseResult(result.toIapResult(), emptyList())
            )
        }
    }
}