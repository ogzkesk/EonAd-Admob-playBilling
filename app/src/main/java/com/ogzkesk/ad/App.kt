package com.ogzkesk.ad

import android.app.Application
import com.ogzkesk.eonad.*
import com.ogzkesk.eonad.billing.*
import com.ogzkesk.eonad.billing.listener.PurchaseHistoryListener
import com.ogzkesk.eonad.billing.util.PurchaseItem
import com.orhanobut.hawk.Hawk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {


    override fun onCreate() {
        super.onCreate()

        Hawk.init(this).build()

        val eonAd = EonAd.getInstance()
        eonAd.init(this)
        eonAd.setResumeAd(BuildConfig.ad_resume_id)


        val purchaseItems = listOf(
            PurchaseItem("id_sub_1", PurchaseItem.TYPE_SUBSCRIPTION),
            PurchaseItem("id_sub_2", PurchaseItem.TYPE_SUBSCRIPTION),
            PurchaseItem("id_sub_3", PurchaseItem.TYPE_SUBSCRIPTION),
            PurchaseItem("id_product_1", PurchaseItem.TYPE_PRODUCT),
        )

        val iap = Iap.getInstance()
        iap.init(this,purchaseItems).connect{ connected , disconnected ->
            if(connected){
                iap.checkSubscription {
                    if(it){
                        Hawk.put("pro",true)
                        println("user subscription is active")
                    } else{
                        Hawk.put("pro",false)
                        println("user subscription is not active")
                    }
                }
            }
        }
    }


}