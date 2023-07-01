package com.ogzkesk.eonad.billing

import android.app.Activity
import android.app.Application
import android.util.Log
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingFlowParams.ProductDetailsParams
import com.android.billingclient.api.QueryProductDetailsParams.Product
import com.google.common.collect.ImmutableList

private const val TAG = "Iap"

class Iap {

    private lateinit var application: Application
    private lateinit var billingClient: BillingClient
    private lateinit var products: List<PurchaseItem>
    private var subscriptions: List<ProductDetail> = emptyList()
    private var inAppProducts: List<ProductDetail> = emptyList()
    private var subscriptionsBase: List<ProductDetails> = emptyList()
    private var inAppProductsBase: List<ProductDetails> = emptyList()
    private lateinit var listener: PurchasesUpdatedListener

    // todo purchase listener olarak değiştirilebilir.
    // todo ayrıca gelmiyor amk . loglar gelmiyor.
    // todo onResult'tada purchase item dönmesi lazım errorun dısında.
    fun listener(onResult: (error: String?) -> Unit) {
        listener = PurchasesUpdatedListener { result, purchases ->
            if (result.checkResponse()) {
                onResult.invoke(null)
                verifyPurchase()
            } else {
                onResult.invoke(result.debugMessage)
            }
        }
    }

    fun init(application: Application, products: List<PurchaseItem>) {
        this.application = application
        this.products = products
        initBillingClient()
    }

    fun getSubscriptions(): List<ProductDetail> {
        return subscriptions
    }

    fun getInAppProducts(): List<ProductDetail> {
        return inAppProducts
    }

    private fun initBillingClient() {
        billingClient = BillingClient.newBuilder(application.applicationContext)
            .enablePendingPurchases()
            .setListener(listener)
            .build()

        startConnection()
    }



    fun startConnection() {

        if(billingClient.checkConnection().not()) {
            initBillingClient()
            return
        }

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                Log.d(TAG, "onServiceDisconnected")
//                startConnection()
            }

            override fun onBillingSetupFinished(p0: BillingResult) {
                Log.d(TAG, "onBillingSetupFinished()")
                getSubs()
                getProducts()
            }
        })
    }

    private fun getSubs() {
        val productList = products.filter {
            it.type == PurchaseItem.TYPE_SUBSCRIPTION
        }.map {
            Product.newBuilder()
                .setProductId(it.id)
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        }

        Log.d(TAG,"querying subs :: $productList")
        if(productList.isEmpty()) return

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient.queryProductDetailsAsync(params) { result, prodDetailsList ->
            if (result.checkResponse()) {
                this.subscriptionsBase = prodDetailsList
                this.subscriptions = prodDetailsList.mapToProductDetail()
            } else {
                Log.d(TAG, "getSubs() ${result.debugMessage}")
            }
        }
    }

    private fun getProducts() {
        val productList = products.filter {
            it.type == PurchaseItem.TYPE_PRODUCT
        }.map {
            Product.newBuilder()
                .setProductId(it.id)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        }

        Log.d(TAG,"querying inapp products :: $productList")
        if(productList.isEmpty()) return

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient.queryProductDetailsAsync(params) { result, prodDetailsList ->
            if (result.checkResponse()) {
                this.inAppProductsBase = prodDetailsList
                this.inAppProducts = prodDetailsList.mapToProductDetail()
            } else {
                Log.d(TAG, "getProducts() ${result.debugMessage}")
            }
        }
    }

    fun launchPurchaseFlow(productId: String, activity: Activity) = apply {

        val productDetails = getProductDetailById(productId) ?: return@apply

        Log.d(TAG,"launchPurchase,, product is not null billingFlow starting..")

        val productDetailsParamsList = getProductDetailParams(productDetails)

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        billingClient.launchBillingFlow(
            activity,
            billingFlowParams
        )
    }

    private fun verifyPurchase() {
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build()
        ) { billingResult: BillingResult, list: List<Purchase> ->
            if (billingResult.checkResponse()) {
                for (purchase in list) {
                    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
                        verifySubPurchase(purchase)
                    }
                }
            }
        }
    }

    private fun verifySubPurchase(purchases: Purchase) {
        val acknowledgePurchaseParams = AcknowledgePurchaseParams
            .newBuilder()
            .setPurchaseToken(purchases.purchaseToken)
            .build()

        billingClient.acknowledgePurchase(acknowledgePurchaseParams) { result ->
            if (result.checkResponse()) {
                // success subscribed.
                return@acknowledgePurchase
            }
        }
        Log.d(TAG, "Purchase Token: " + purchases.purchaseToken)
        Log.d(TAG, "Purchase Time: " + purchases.purchaseTime)
        Log.d(TAG, "Purchase OrderID: " + purchases.orderId)
    }


    fun release() {
        // TODO End connection actually work but not sending logs in onDisconnected.. why?
        billingClient.endConnection()
    }

    fun checkSubscription() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()
        billingClient.queryPurchasesAsync(params) { result, purchases ->
            if (result.checkResponse()) {
                println("paymentHelper --> checkSubscription > queryPurchase response : OK -> running")
                if (purchases.isNotEmpty()) {
                    for ((i, purchase) in purchases.withIndex()) {
                        //Here you can manage each product, if you have multiple subscription
                        Log.d("testOffer", purchase.originalJson)
                        Log.d("testOffer", " index$i")
                    }
                } else {

                }
            }
        }
    }

    private fun getProductDetailParams(details: ProductDetails) : List<ProductDetailsParams> {
        return if(details.productType == BillingClient.ProductType.SUBS){
            if(details.subscriptionOfferDetails != null){
                ImmutableList.of(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(details)
                        .setOfferToken(details.subscriptionOfferDetails!!.first().offerToken)
                        .build()
                )
            } else {
                emptyList()
            }
        } else {
            ImmutableList.of(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(details)
                    .build()
            )
        }
    }

    private fun getProductDetailById(productId: String) : ProductDetails? {

        val productList = mutableListOf<ProductDetails>()
        productList.addAll(subscriptionsBase)
        productList.addAll(inAppProductsBase)

        return if (productList.isNotEmpty()) {
            productList.filter { it.productId == productId }.first()
        } else {
            null
        }
    }

    fun restoreSubscription() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()
        billingClient.queryPurchasesAsync(params) { result, purchases ->
            if (result.checkResponse()) {
                Log.d(TAG, purchases.size.toString() + " size")
                if (purchases.isNotEmpty()) {
                    for ((i, purchase) in purchases.withIndex()) {
                        Log.d("testOffer", purchase.originalJson)
                        Log.d("testOffer", " index$i")
                    }
                } else {
                    Log.d(TAG, "No purchase found :: ${result.debugMessage}")
                }
            }
        }
    }


    companion object {
        @Volatile
        private var instance: Iap? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: Iap().also { instance = it }
        }
    }

    init {
        listener { }
    }
}

private class MListener : PurchasesUpdatedListener {

    override fun onPurchasesUpdated(result: BillingResult, purchases: MutableList<Purchase>?) {

    }
}
