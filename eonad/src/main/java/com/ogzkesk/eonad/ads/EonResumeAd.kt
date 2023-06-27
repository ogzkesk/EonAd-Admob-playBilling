package com.ogzkesk.eonad.ads

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LifecycleObserver
import com.ogzkesk.eonad.AppOpenManager

private const val TAG = "EonResumeAd"

class EonResumeAd() {

    private lateinit var appOpenManager : AppOpenManager

    fun init(application: Application,adUnitId: String){
        this.appOpenManager = AppOpenManager(application,adUnitId)
    }

    fun disableResumeAds(){
        this.appOpenManager.isDisabled = true
    }

    fun enableResumeAds(){
        this.appOpenManager.isDisabled = false
    }

    fun disableResumeAdsOnClickEvent(){
        this.appOpenManager.isDisabledOnClickEvent = true
    }
}