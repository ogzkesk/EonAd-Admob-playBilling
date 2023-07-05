package com.ogzkesk.eonad.billing

import com.android.billingclient.api.Purchase


class PurchaseIap(private val purchase: Purchase) {

    val developerPayload: String
        get() = purchase.developerPayload
    val isAcknowledged: Boolean
        get() = purchase.isAcknowledged
    val isAutoRenewing: Boolean
        get() = purchase.isAutoRenewing
    val orderId: String?
        get() = purchase.orderId
    val packageName: String
        get() = purchase.packageName
    val originalJson: String
        get() = purchase.originalJson
    val products: List<String>
        get() = purchase.products

    @PurchaseState
    val purchaseState: Int
        get() = purchase.purchaseState
    val purchaseTime: Long
        get() = purchase.purchaseTime
    val purchaseToken: String
        get() = purchase.purchaseToken
    val quantity: Int
        get() = purchase.quantity
    val signature: String
        get() = purchase.signature

    val accountIdentifiers: AccountIdentifier
        get() = AccountIdentifier(purchase.accountIdentifiers)

    @Retention(AnnotationRetention.SOURCE)
    annotation class PurchaseState {
        companion object {
            const val UNSPECIFIED_STATE = 0
            const val PURCHASED = 1
            const val PENDING = 2
        }
    }
}