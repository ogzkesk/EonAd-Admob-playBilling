package com.ogzkesk.eonad.billing

import android.app.Activity
import android.app.Application
import android.util.Log
import com.android.billingclient.api.*
import com.ogzkesk.eonad.billing.listener.ConnectionListener
import com.ogzkesk.eonad.billing.listener.PurchaseHistoryListener
import com.ogzkesk.eonad.billing.util.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

private const val TAG = "Iap"


class Iap {

    private lateinit var application: Application
    private lateinit var billingClient: BillingClient
    private lateinit var products: List<PurchaseItem>
    private lateinit var connectionListener: BillingClientStateListener
    private lateinit var mIap: MIap
    private val result = Channel<PurchaseResult>()
    private var subscriptions: List<ProductDetail> = emptyList()
    private var inAppProducts: List<ProductDetail> = emptyList()
    private var subscriptionsBase: List<ProductDetails> = emptyList()
    private var inAppProductsBase: List<ProductDetails> = emptyList()
    private var autoConsumeEnabled = true
    private var isConnected = false


    private val purchaseListener = PurchasesUpdatedListener { billingResult, purchases ->
        if (billingResult.checkResponse()) {

            setSubscription(purchases, billingResult)
            setPurchase(purchases, billingResult)

        } else {
            Log.d(TAG, "listen() ${billingResult.debugMessage}")
            this@Iap.result.trySend(
                PurchaseResult(billingResult.toIapResult(), emptyList())
            )
        }
    }


    fun init(application: Application, products: List<PurchaseItem>) = apply {
        this.application = application
        this.products = products
    }

    fun isConnected(): Boolean {
        return isConnected
    }

    fun getSubscriptions(): List<ProductDetail> {
        return subscriptions
    }


    fun getInAppProducts(): List<ProductDetail> {
        return inAppProducts
    }

    suspend fun listen(purchaseResult: suspend (PurchaseResult) -> Unit) {
        result.receiveAsFlow().collect(purchaseResult::invoke)
    }

    fun disableAutoConsume() {
        autoConsumeEnabled = false
    }

    fun consume(purchase: PurchaseIap, onConsume: (error: String?) -> Unit) {
        mIap.consume(purchase){ error -> onConsume.invoke(error) }
    }


    fun connect(listener: ConnectionListener? = null) = apply {

        billingClient = BillingClient.newBuilder(application.applicationContext)
            .setListener(purchaseListener)
            .enablePendingPurchases()
            .build()

        connectionListener(listener)
        billingClient.startConnection(connectionListener)
        setMIap()
    }


    fun release() {
        Log.d(TAG, "released()")
        billingClient.endConnection()
    }


    fun subscribe(activity: Activity, productId: String) = apply {

        val productDetails = mIap.getSubscriptionDetailsById(productId) ?: return@apply
        val productDetailsParamsList = mIap.getProductDetailParams(productDetails)

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        Log.d(TAG, "launchingBillingFlow()")
        billingClient.launchBillingFlow(activity, billingFlowParams)
    }

    fun purchase(activity: Activity, productId: String) = apply {

        val productDetails = mIap.getProductDetailById(productId) ?: return@apply
        val productDetailsParamsList = mIap.getProductDetailParams(productDetails)

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        Log.d(TAG, "launchingBillingFlow()")
        billingClient.launchBillingFlow(activity, billingFlowParams)

    }


    fun checkSubscription(available: (isAvailable: Boolean) -> Unit) {

        if (isConnected.not()) Log.d(TAG, "Iap not connected")

        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()

        billingClient.queryPurchasesAsync(params) { result, purchases ->
            if (result.checkResponse()) {
                if(purchases.isNotEmpty()) available.invoke(true)
                return@queryPurchasesAsync
            }
            available.invoke(false)
        }
    }


    fun checkPurchaseHistory(listener: PurchaseHistoryListener) {

        val params = QueryPurchaseHistoryParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .setProductType(BillingClient.ProductType.SUBS)
            .build()

        billingClient.queryPurchaseHistoryAsync(params) { result, history ->
            if (result.checkResponse()) {
                listener.onCheckPurchaseHistory(
                    result.toIapResult(),
                    history.toNonNull().toPurchaseHistory()
                )
                return@queryPurchaseHistoryAsync
            }
            listener.onCheckPurchaseHistory(result.toIapResult(), emptyList())
        }
    }


    private fun connectionListener(listener: ConnectionListener?) {
        connectionListener = object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                Log.d(TAG, "onDisconnected")
                listener?.onConnectionState(false, true)
            }

            override fun onBillingSetupFinished(result: BillingResult) {
                Log.d(TAG, "onConnected")
                isConnected = true
                listener?.onConnectionState(true, false)
                if (result.checkResponse()) {
                    mIap.getSubs { prodDetailsList ->
                        this@Iap.subscriptionsBase = prodDetailsList
                        this@Iap.subscriptions = prodDetailsList.mapToProductDetail()
                        setMIap()
                    }
                    mIap.getProducts { prodDetailsList ->
                        this@Iap.inAppProductsBase = prodDetailsList
                        this@Iap.inAppProducts = prodDetailsList.mapToProductDetail()
                        setMIap()
                    }
                }
            }
        }
    }

    private fun setMIap() {
        this.mIap = MIap(
            billingClient,
            products,
            subscriptionsBase,
            inAppProductsBase
        )
    }


    private fun setSubscription(purchases: List<Purchase>?, result: BillingResult) {
        mIap.checkSubAndVerify(purchases, result) { error, purchase ->
            if (error != null) {
                this@Iap.result.trySend(PurchaseResult(result.toIapResult(), emptyList()))
                return@checkSubAndVerify
            }
            this@Iap.result.trySend(
                purchase
            )
        }
    }

    private fun setPurchase(purchases: List<Purchase>?, result: BillingResult) {
        if (autoConsumeEnabled) {
            mIap.checkPurchaseAndConsume(purchases, result) { error, purchase ->
                if (error != null) {
                    this@Iap.result.trySend(PurchaseResult(result.toIapResult(), emptyList()))
                    return@checkPurchaseAndConsume
                }

                this@Iap.result.trySend(purchase)
            }
        } else {
            this@Iap.result.trySend(
                PurchaseResult(result.toIapResult(), emptyList())
            )
        }
    }


    companion object {
        @Volatile
        private var instance: Iap? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: Iap().also { instance = it }
        }
    }
}

