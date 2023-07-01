package com.ogzkesk.eonad

import android.app.Application
import android.content.Context
import android.view.View
import com.google.android.gms.ads.MobileAds
import com.ogzkesk.eonad.ads.*

private const val TAG = "EonAd"

class EonAd private constructor() {

    private lateinit var application: Application
    private lateinit var provider: String
    private var deviceTestIds: List<String> = emptyList()

    private val nativeAd = EonNativeAd()
    private val interstitialAd = EonInterstitialAd()
    private val rewardedAd = EonRewardedAd()
    private val resumeAd = EonResumeAd()
    private val bannerAd = EonBannerAd()

    fun init(application: Application) {
        MobileAds.initialize(application) {}
        this.application = application
    }


    fun setResumeAd(adUnitId: String) {
        resumeAd.init(application, adUnitId)
    }

    fun enableResumeAds() {
        resumeAd.enableResumeAds()
    }

    fun disableResumeAds() {
        resumeAd.disableResumeAds()
    }

    fun disableResumeAdsOnClickEvent() {
        resumeAd.disableResumeAdsOnClickEvent()
    }

    fun loadBannerAd(context: Context, adUnitId: String, adSize: BannerAdSize): View {
        return bannerAd.loadBannerAd(context, adUnitId, adSize)
    }

    fun loadBannerAd(
        context: Context,
        adUnitId: String,
        adSize: BannerAdSize,
        eonAdCallback: EonAdCallback
    ) {
        bannerAd.loadBannerAd(context, adUnitId, adSize, eonAdCallback)
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
        type: NativeAdTemplate,
        multipleAdCount: Int = 1,
        onFailedToLoad: ((EonAdError) -> Unit)? = null,
        onNativeAdLoaded: (View) -> Unit,
    ) {
        nativeAd.loadNativeAdTemplate(
            context,
            adUnitId,
            type,
            multipleAdCount,
            onNativeAdLoaded,
            onFailedToLoad
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


    companion object {

        @Volatile
        private var instance: EonAd? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: EonAd().also { instance = it }
        }
    }
}

