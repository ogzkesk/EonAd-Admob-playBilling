package com.ogzkesk.eonad.billing.util

import com.ogzkesk.eonad.billing.IapResult
import com.ogzkesk.eonad.billing.PurchaseIap

data class PurchaseResult(
    val result: IapResult,
    val purchases: List<PurchaseIap>
)