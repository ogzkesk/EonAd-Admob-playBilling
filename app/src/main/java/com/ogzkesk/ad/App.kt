package com.ogzkesk.ad

import android.app.Application
import com.ogzkesk.eonad.*
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val eonAdConfig = EonAdConfig(EonAdConfig.PROVIDER_ADMOB)
        EonAd.getInstance().init(this,eonAdConfig)

    }

}