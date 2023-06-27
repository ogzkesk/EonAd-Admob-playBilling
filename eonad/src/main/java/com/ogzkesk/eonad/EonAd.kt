package com.ogzkesk.eonad

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

private const val TAG = "EonAd"

class EonAd private constructor() : Application.ActivityLifecycleCallbacks {

    // TODOS -- > image cornerslerini değiştir. -> medium templateti değiştir -> banner ad yap -> sonra resume...
    private lateinit var appOpenAdManager: AppOpenAdManager
    private lateinit var provider: String
    private var deviceTestIds: List<String> = emptyList()
    private val nativeAd = EonNativeAd() // TODO bunu tek instance yapmayıpta REAL native adlar ile denersin. çift activityde dene destroy olayınıda ele al
    private val interstitialAd = EonInterstitialAd()
    private val rewardedAd = EonRewardedAd()

    fun init(application: Application, config: EonAdConfig) {
        MobileAds.initialize(application) {}
        this.appOpenAdManager = AppOpenAdManager()
        this.provider = config.provider
        this.deviceTestIds = config.deviceTestIds
    }


    fun loadNativeAd(
        context: Context,
        adUnitId: String,
        eonAdCallback: EonAdCallback,
        multipleAdCount: Int = 1
    ) {
        nativeAd.loadNativeAd(
            adUnitId,
            context,
            multipleAdCount,
            eonAdCallback
        )
    }

    fun loadNativeAd(
        context: Context,
        adUnitId: String,
        multipleAdCount: Int = 1,
        onNativeAdLoaded: (EonNativeAd) -> Unit,
    ) {
        nativeAd.loadNativeAd(
            adUnitId,
            context,
            multipleAdCount,
            onNativeAdLoaded
        )
    }

    fun loadNativeAdTemplate(
        context: Context,
        adUnitId: String,
        type: NativeAdTemplateType,
        multipleAdCount: Int = 1,
        onNativeAdLoaded: (EonAdError?,View?) -> Unit,
    ) {
        nativeAd.loadNativeAdTemplate(
            context,
            adUnitId,
            type,
            multipleAdCount,
            onNativeAdLoaded,
        )
    }

    fun loadRewardedAd(context: Context, adUnitId: String, eonAdCallback: EonAdCallback) {
        rewardedAd.loadRewardedAd(
            context,
            adUnitId,
            eonAdCallback
        )
    }

    fun loadRewardedAd(context: Context, adUnitId: String) {
        rewardedAd.loadRewardedAd(context, adUnitId)
    }


    fun loadInterstitialAd(context: Context, adUnitId: String, eonAdCallback: EonAdCallback) {
        interstitialAd.loadInterstitialAd(
            context,
            adUnitId,
            eonAdCallback
        )
    }

    fun loadInterstitialAd(context: Context, adUnitId: String) {
        interstitialAd.loadInterstitialAd(context, adUnitId)
    }


    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onActivityStarted(p0: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivityResumed(p0: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivityPaused(p0: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivityStopped(p0: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
        TODO("Not yet implemented")
    }

    override fun onActivityDestroyed(p0: Activity) {
        TODO("Not yet implemented")
    }


    companion object {

        @Volatile
        private var instance: EonAd? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: EonAd().also { instance = it }
        }
    }
}

