package com.ogzkesk.eonad.billing

import com.android.billingclient.api.BillingResult

class IapResult(private val billingResult: BillingResult) {

    val debugMessage: String
        get() = billingResult.debugMessage
    val responseCode: Int
        get() = billingResult.responseCode

    @Retention(AnnotationRetention.SOURCE)
    annotation class BillingResponseCode {
        companion object {

            @Deprecated("deprecated")
            var SERVICE_TIMEOUT = -3
            var FEATURE_NOT_SUPPORTED = -2
            var SERVICE_DISCONNECTED = -1
            var OK = 0
            var USER_CANCELED = 1
            var SERVICE_UNAVAILABLE = 2
            var BILLING_UNAVAILABLE = 3
            var ITEM_UNAVAILABLE = 4
            var DEVELOPER_ERROR = 5
            var ERROR = 6
            var ITEM_ALREADY_OWNED = 7
            var ITEM_NOT_OWNED = 8
            var NETWORK_ERROR = 12
        }
    }
}