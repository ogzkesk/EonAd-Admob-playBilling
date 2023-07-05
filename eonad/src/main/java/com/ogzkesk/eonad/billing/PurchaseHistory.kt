package com.ogzkesk.eonad.billing

import com.android.billingclient.api.PurchaseHistoryRecord

class PurchaseHistory(private val purchaseHistoryRecord: PurchaseHistoryRecord) {

    val developerPayload: String
    get() = purchaseHistoryRecord.developerPayload
    val originalJson: String
    get() = purchaseHistoryRecord.originalJson
    val purchaseToken: String
    get() = purchaseHistoryRecord.purchaseToken
    val signature: String
    get() = purchaseHistoryRecord.signature
    val products: List<String>
    get() = purchaseHistoryRecord.products
    val purchaseTime: Long
    get() = purchaseHistoryRecord.purchaseTime
    val quantity: Int
    get() = purchaseHistoryRecord.quantity
    @Deprecated(message = "deprecated")
    val skus: List<String>
    get() = purchaseHistoryRecord.skus

}