package com.ogzkesk.eonad.billing.listener

import com.ogzkesk.eonad.billing.IapResult
import com.ogzkesk.eonad.billing.PurchaseHistory


fun interface PurchaseHistoryListener {

    fun onCheckPurchaseHistory(result: IapResult, history: List<PurchaseHistory>)

}