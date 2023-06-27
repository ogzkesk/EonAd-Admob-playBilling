package com.ogzkesk.eonad

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.common.api.internal.LifecycleFragment


private const val TAG = "AppOpenManager"

internal class AppOpenManager(
    private val application: Application,
    private val adUnitId: String
) : Application.ActivityLifecycleCallbacks, LifecycleObserver {


    private var appOpenAd: AppOpenAd? = null
    private var loadCallback: AppOpenAd.AppOpenAdLoadCallback? = null
    private var currentActivity: Activity? = null
    private val request = AdRequest.Builder().build()


    private var isLoadingAd = false
    private var isShowingAd = false
    var isDisabled = false
    var isDisabledOnClickEvent = false

    private fun loadAd() {
        Log.d(TAG,"loadAd() isDisabledOnClickEvent :: $isDisabledOnClickEvent")
        Log.d(TAG,"loadAd() isDisabled :: $isDisabled")

        if (isAdAvailable()) {
            return
        }

        isLoadingAd = true
        loadCallback = object : AppOpenAd.AppOpenAdLoadCallback() {
            override fun onAdLoaded(ad: AppOpenAd) {
                Log.d(TAG, "ResumeAd onAdLoaded.")
                appOpenAd = ad
                isLoadingAd = false
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                Log.d(TAG, "ResumeAd onAdFailedToLoad :: ${loadAdError.message}")
                isLoadingAd = false
            }
        }

        AppOpenAd.load(
            application,
            adUnitId,
            request,
            loadCallback as AppOpenAd.AppOpenAdLoadCallback
        )
    }

    private fun show() {
        Log.d(TAG,"show() isDisabledOnClickEvent :: $isDisabledOnClickEvent")
        Log.d(TAG,"show() isDisabled :: $isDisabled")
        if (!isShowingAd  && !isDisabled && !isDisabledOnClickEvent && isAdAvailable()) {

            val fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    Log.d(TAG,"ResumeAd onAdDismissedFullScreenContent")
                    isShowingAd = false
                    appOpenAd = null
                    loadAd()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    super.onAdFailedToShowFullScreenContent(p0)
                    Log.d(TAG,"ResumeAd onAdFailedToShowFullScreenContent :: ${p0.message}")
                }

                override fun onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent()
                    isShowingAd = true
                }
            }

            currentActivity?.let { activity ->
                appOpenAd?.fullScreenContentCallback = fullScreenContentCallback
                appOpenAd?.show(activity)
            }

            return
        }

        Log.d(TAG, "ResumeAd not available for show. Re-Loading..")
        isDisabledOnClickEvent = false
        loadAd()
    }

    override fun onActivityStarted(activity: Activity) {
        Log.d(TAG, "onActivityStarted")
        this.currentActivity = activity
        show()
    }

    override fun onActivityResumed(activity: Activity) {
        this.currentActivity = activity
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.d(TAG, "onActivityDestroyed")
        currentActivity = null
    }

    private fun isAdAvailable(): Boolean {
        return appOpenAd != null
    }

    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {}
    override fun onActivityCreated(activity: Activity, p1: Bundle?) {}

    init {
        application.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }
}