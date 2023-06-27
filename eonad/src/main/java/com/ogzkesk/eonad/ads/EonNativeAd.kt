package com.ogzkesk.eonad.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.ogzkesk.eonad.EonAdCallback
import com.ogzkesk.eonad.EonAdError
import com.ogzkesk.eonad.EonAds
import com.ogzkesk.eonad.NativeAdTemplateType
import com.ogzkesk.eonad.ui.NativeAdUi

private const val TAG = "EonNativeAd"

class EonNativeAd() : EonAds() {

    override var nativeAd: NativeAd? = null

    internal fun loadNativeAdTemplate(
        context: Context,
        adUnitId: String,
        type: NativeAdTemplateType,
        multipleAdCount: Int = 1,
        onNativeAdViewLoaded: (EonAdError?, View?) -> Unit,
    ) {
        val loadingUi = NativeAdUi(nativeAd).getNativeUi(context,type)
        onNativeAdViewLoaded(null,loadingUi)

        loadNativeAd(adUnitId, context, multipleAdCount, object : EonAdCallback {

            override fun onAdFailedToLoad(error: EonAdError) {
                super.onAdFailedToLoad(error)
                onNativeAdViewLoaded(error, null)
            }

            override fun onNativeAdLoaded(eonNativeAd: EonNativeAd) {
                super.onNativeAdLoaded(eonNativeAd)
                onNativeAdViewLoaded(
                    null,
                    NativeAdUi(eonNativeAd.nativeAd).getNativeUi(context,type)
                )
            }
        })
    }

    internal fun loadNativeAd(
        adUnitId: String,
        context: Context,
        multipleAdCount: Int = 1,
        eonAdCallback: EonAdCallback
    ) {
        val adRequest = AdRequest.Builder().build()
        val adLoader = AdLoader.Builder(context, adUnitId)
            .forNativeAd { ad: NativeAd ->

                this.nativeAd = ad
                eonAdCallback.onNativeAdLoaded(this@EonNativeAd)

                if ((context as Activity).isDestroyed) {
                    Log.d(TAG,"nativeAd Destroyed")
                    this.nativeAd = null
                    ad.destroy()
                    return@forNativeAd
                }

                Log.d(TAG, "nativeAd Loaded ::$nativeAd")
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    eonAdCallback.onAdFailedToLoad(EonAdError(adError))
                    this@EonNativeAd.nativeAd = null
                    Log.d(TAG, "nativeAd failed to load ::${adError.message}")
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    eonAdCallback.onAdClicked()
                }

                override fun onAdClosed() {
                    super.onAdClosed()
                    eonAdCallback.onAdClosed()
                    this@EonNativeAd.nativeAd = null
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    eonAdCallback.onAdImpression()
                }

                override fun onAdOpened() {
                    super.onAdOpened()
                    eonAdCallback.onNativeAdOpened()
                }

                override fun onAdSwipeGestureClicked() {
                    super.onAdSwipeGestureClicked()
                    eonAdCallback.onAdSwipeGestureClicked()
                }
            })
            .build()

        if (multipleAdCount > 1) {
            adLoader.loadAds(adRequest, multipleAdCount)
            return
        }

        adLoader.loadAd(adRequest)

        if (adLoader.isLoading) {
            Log.d(TAG, "nativeAd onLoading...")
            eonAdCallback.onLoading()
        }
    }

    internal fun loadNativeAd(
        adUnitId: String,
        context: Context,
        multipleAdCount: Int = 1,
        onNativeAdLoaded: (EonNativeAd) -> Unit,
    ) {
        val adRequest = AdRequest.Builder().build()
        val adLoader = AdLoader.Builder(context, adUnitId)
            .forNativeAd { ad ->

                this@EonNativeAd.nativeAd = ad
                onNativeAdLoaded.invoke(this@EonNativeAd)

                if ((context as Activity).isDestroyed) {
                    Log.d(TAG,"nativeAd Destroyed")
                    this@EonNativeAd.nativeAd = null
                    ad.destroy()
                }
            }
            .build()

        if (multipleAdCount > 1) {
            adLoader.loadAds(adRequest, multipleAdCount)
            return
        }

        adLoader.loadAd(adRequest)
    }


    companion object {

//        const val NATIVE_MEDIA_ASPECT_RATIO_UNKNOWN = 0
//        const val NATIVE_MEDIA_ASPECT_RATIO_ANY = 1
//        const val NATIVE_MEDIA_ASPECT_RATIO_LANDSCAPE = 2
//        const val NATIVE_MEDIA_ASPECT_RATIO_PORTRAIT = 3
//        const val NATIVE_MEDIA_ASPECT_RATIO_SQUARE = 4
//        const val ADCHOICES_TOP_LEFT = 0
//        const val ADCHOICES_TOP_RIGHT = 1
//        const val ADCHOICES_BOTTOM_RIGHT = 2
//        const val ADCHOICES_BOTTOM_LEFT = 3
//        const val SWIPE_GESTURE_DIRECTION_RIGHT = 1
//        const val SWIPE_GESTURE_DIRECTION_LEFT = 2
//        const val SWIPE_GESTURE_DIRECTION_UP = 4
//        const val SWIPE_GESTURE_DIRECTION_DOWN = 8
    }
}