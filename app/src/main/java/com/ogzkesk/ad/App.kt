package com.ogzkesk.ad

import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import androidx.lifecycle.LifecycleObserver
import com.ogzkesk.eonad.*
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val eonAdConfig = EonAdConfig(EonAdConfig.PROVIDER_ADMOB)
        val eonAd = EonAd.getInstance()

        eonAd.init(this,eonAdConfig)
        eonAd.setResumeAd(BuildConfig.ad_resume_id)


    }

}