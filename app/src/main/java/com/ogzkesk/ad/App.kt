package com.ogzkesk.ad

import android.app.Application
import com.ogzkesk.eonad.*
import com.ogzkesk.eonad.billing.Iap
import com.ogzkesk.eonad.billing.PurchaseItem
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {


    override fun onCreate() {
        super.onCreate()

        val eonAd = EonAd.getInstance()
        eonAd.init(this)
        eonAd.setResumeAd(BuildConfig.ad_resume_id)

        val purchaseItems = listOf(
            PurchaseItem("id_sub_1",PurchaseItem.TYPE_SUBSCRIPTION),
            PurchaseItem("id_sub_2",PurchaseItem.TYPE_SUBSCRIPTION),
            PurchaseItem("id_sub_3",PurchaseItem.TYPE_SUBSCRIPTION),
            PurchaseItem("id_product_1",PurchaseItem.TYPE_PRODUCT),
        )

        val iap = Iap.getInstance()
        iap.init(this, purchaseItems)

        val subs = iap.getSubs()
        val products = iap.getInAppProducts()

        println("subs :::::::::: $subs")
        println("products :::::::::: $products")

    }

}