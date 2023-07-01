package com.ogzkesk.eonad.billing
//
//import android.app.Application
//import android.content.Context
//import android.os.Handler
//import android.os.Looper
//import android.util.Log
//import androidx.activity.ComponentActivity
//import com.android.billingclient.api.*
//import com.android.billingclient.api.BillingFlowParams.ProductDetailsParams
//import com.google.common.collect.ImmutableList
//import kotlinx.coroutines.channels.awaitClose
//import kotlinx.coroutines.flow.*
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//
//class PaymentHelper constructor() {
//
//    val TAG = "PaymentHelper"
//
//    private lateinit var billingClient : BillingClient
//
//    private fun init() {
//        println("paymentHelper -- > init running")
//        billingClient = BillingClient.newBuilder(context)
//            .enablePendingPurchases()
//            .setListener { result, purchases ->
//                println("paymentHelper -- > init listener running")
//                if (result.checkResponse() && purchases != null) {
//                    purchases.forEach { _ ->
//                        scope.launch {
//                            useCases.appStore.readShouldAddToken.collectLatest {
//                                if (it) {
//                                    verifyPurchase()
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            .build()
//
//
//        startConnection()
//    }
//
//    fun startConnection() {
//        println("paymentHelper -- > startConnection running")
//        billingClient.startConnection(object : BillingClientStateListener {
//            override fun onBillingServiceDisconnected() {
//                Log.d(TAG, "onServiceDisconnected")
//                startConnection()
//            }
//
//            override fun onBillingSetupFinished(p0: BillingResult) {
//                Log.d(TAG, "onSetupFinished -> showProducts()")
//                println("paymentHelper -- > startConnection listener setupFinished running")
//            }
//        })
//    }
//
//
//    fun fetchProducts() = callbackFlow<Resource<List<ProductDetails>>> {
//        println("paymentHelper -- > showProducts running")
//        trySend(Resource.Loading())
//        val productList: ImmutableList<QueryProductDetailsParams.Product> = ImmutableList.of(
//            QueryProductDetailsParams.Product.newBuilder()
//                .setProductId(Constants.SUB_KEY_WEEK)
//                .setProductType(BillingClient.ProductType.SUBS)
//                .build(),
//            QueryProductDetailsParams.Product.newBuilder()
//                .setProductId(Constants.SUB_KEY_MONTH)
//                .setProductType(BillingClient.ProductType.SUBS)
//                .build(),
//            QueryProductDetailsParams.Product.newBuilder()
//                .setProductId(Constants.SUB_KEY_THREE_MONTH)
//                .setProductType(BillingClient.ProductType.SUBS)
//                .build()
//        )
//
//        val params = QueryProductDetailsParams.newBuilder()
//            .setProductList(productList)
//            .build()
//
//        billingClient.queryProductDetailsAsync(params) { result, prodDetailsList ->
//            if (result.checkResponse()) {
//                trySend(Resource.Success(prodDetailsList))
//            } else {
//                trySend(Resource.Error(result.debugMessage))
//            }
//        }
//
//        awaitClose()
//    }
//
//
//    fun launchPurchaseFlow(productDetails: ProductDetails, activity: ComponentActivity) {
//        println("paymentHelper --> launchPurchaseFlow running")
//        if (productDetails.subscriptionOfferDetails == null) return
//        println("paymentHelper --> launchPurchaseFlow > subsList not null -> running")
//        val productDetailsParamsList = ImmutableList.of(
//            ProductDetailsParams.newBuilder()
//                .setProductDetails(productDetails)
//                .setOfferToken(productDetails.subscriptionOfferDetails!!.first().offerToken)
//                .build()
//        )
//
//        val billingFlowParams = BillingFlowParams.newBuilder()
//            .setProductDetailsParamsList(productDetailsParamsList)
//            .build()
//
//        billingClient.launchBillingFlow(
//            activity,
//            billingFlowParams
//        )
//    }
//
//    private fun verifyPurchase() {
//        billingClient.queryPurchasesAsync(
//            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build()
//        ) { billingResult: BillingResult, list: List<Purchase> ->
//            if (billingResult.checkResponse()) {
//                for (purchase in list) {
//                    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
//                        verifySubPurchase(purchase)
//                    }
//                }
//            }
//        }
//    }
//
//    private fun verifySubPurchase(purchases: Purchase) {
//        println("paymentHelper --> veryifySubPurchase running")
//        val acknowledgePurchaseParams = AcknowledgePurchaseParams
//            .newBuilder()
//            .setPurchaseToken(purchases.purchaseToken)
//            .build()
//
//        billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult: BillingResult ->
//            if (billingResult.checkResponse()) {
//                println("paymentHelper --> verifySubPurchase --> acknowledge response : OK - > running")
//
//                scope.launch {
//                    println("paymentHelper --> scope.launch girdi")
//                    useCases.appStore.apply {
//                        writeIsUserPro(true)
//                        val currToken = readRemainingToken.first()
//                        println("paymentHelper --> currentToken :: $currToken")
//                        writeRemainingToken(currToken.copy(currToken.remainingToken + 150))
//                        println("paymentHelper --> writeRemainingToken done :: $currToken")
//                        writeShouldAddToken(false)
//                        println("paymentHelper --> writeShouldAddToken done :: $")
//                        val shouldAddToken = readShouldAddToken.first()
//                        println("paymentHelper --> writeShouldAddToken queryShouldAdd :: $shouldAddToken")
//                    }
//                }
//
//                context.showToast("Subscription activated, Enjoy!")
//            }
//        }
//
//        Log.d(TAG, "Purchase Token: " + purchases.purchaseToken)
//        Log.d(TAG, "Purchase Time: " + purchases.purchaseTime)
//        Log.d(TAG, "Purchase OrderID: " + purchases.orderId)
//    }
//
//
//    fun checkSubscription() {
//        println("paymentHelper --> checkSubscription running")
//        val finalBillingClient = BillingClient.newBuilder(context)
//            .enablePendingPurchases()
//            .setListener { billingResult, purchases -> }
//            .build()
//
//        finalBillingClient.startConnection(object : BillingClientStateListener {
//            override fun onBillingServiceDisconnected() {}
//            override fun onBillingSetupFinished(billingResult: BillingResult) {
//                if (billingResult.checkResponse()) {
//                    println("paymentHelper --> checkSubscription > listener response : OK -> running")
//                    finalBillingClient.queryPurchasesAsync(
//                        QueryPurchasesParams.newBuilder()
//                            .setProductType(BillingClient.ProductType.SUBS)
//                            .build()
//                    ) { billingResult1: BillingResult, list: List<Purchase> ->
//                        println("paymentHelper --> checkSubscription > queryPurchase running")
//                        if (billingResult1.checkResponse()) {
//                            println("paymentHelper --> checkSubscription > queryPurchase response : OK -> running")
//                            if (list.isNotEmpty()) {
//                                println("paymentHelper --> checkSubscription > queryPurchase list NOT EMPTY :: USER PRO = TRUE")
//                                scope.launch { useCases.appStore.writeIsUserPro(true) }
//                                for ((i, purchase) in list.withIndex()) {
//                                    //Here you can manage each product, if you have multiple subscription
//                                    Log.d("testOffer", purchase.originalJson)
//                                    Log.d("testOffer", " index$i")
//                                }
//                            } else {
//                                scope.launch {
//                                    useCases.appStore.writeIsUserPro(false)
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        })
//    }
//
//    fun restoreSubscription() {
//        println("paymentHelper --> restoreSubscription running")
//        val finalBillingClient = BillingClient.newBuilder(context)
//            .enablePendingPurchases()
//            .setListener { billingResult, purchases -> }
//            .build()
//
//        finalBillingClient.startConnection(object : BillingClientStateListener {
//            override fun onBillingServiceDisconnected() {}
//            override fun onBillingSetupFinished(billingResult: BillingResult) {
//                if (billingResult.checkResponse()) {
//                    println("paymentHelper --> restoreSubscription > listener response : OK - > running")
//                    finalBillingClient.queryPurchasesAsync(
//                        QueryPurchasesParams.newBuilder()
//                            .setProductType(BillingClient.ProductType.SUBS)
//                            .build()
//                    ) { billingResult1: BillingResult, list: List<Purchase> ->
//                        if (billingResult1.checkResponse()) {
//                            println("paymentHelper --> restoreSubscription > queryPurchase response : OK - > running")
//                            Log.d("testOffer", list.size.toString() + " size")
//                            if (list.isNotEmpty()) {
//                                println("paymentHelper --> restoreSubscription > queryPurchase list is NOT EMPTY :: USER PRO = TRUE")
//                                scope.launch { useCases.appStore.writeIsUserPro(true) }
//                                for ((i, purchase) in list.withIndex()) {
//                                    //Here you can manage each product, if you have multiple subscription
//                                    Log.d("testOffer", purchase.originalJson)
//                                    Log.d("testOffer", " index$i")
//                                }
//                            } else {
//                                scope.launch {
//                                    useCases.appStore.writeIsUserPro(false)
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        })
//    }
//}