package com.ogzkesk.eonad

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

private const val TAG = "EonNativeAd"

class EonNativeAd() : EonAds() {

    override var nativeAd: NativeAd? = null

    internal fun loadNativeAd(
        adUnitId: String,
        context: Context,
        multipleAdCount: Int = 1,
        eonAdCallback: EonAdCallback
    ){
        val adRequest = AdRequest.Builder().build()
        val adLoader = AdLoader.Builder(context, adUnitId)
            .forNativeAd { ad: NativeAd ->

                this.nativeAd = ad
                eonAdCallback.onNativeAdLoaded(this@EonNativeAd)

                if ((context as Activity).isDestroyed) {
                    println("$TAG activity destroy girdi")
                    this.nativeAd = null
                    ad.destroy()
                    return@forNativeAd
                }

                Log.d(TAG,"nativeAd Loaded ::$nativeAd")
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    eonAdCallback.onAdFailedToLoad(EonAdError(adError))
                    this@EonNativeAd.nativeAd = null
                    Log.d(TAG,"nativeAd failed to load ::${adError.message}")
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
            Log.d(TAG,"nativeAd onLoading...")
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
                    println("$TAG activity destroy girdi")
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


    fun populateSmallNativeView(context: Context): View {
        return NativeAdView(context).apply {
            this@EonNativeAd.nativeAd?.let { ad ->

                val adView = (context as Activity).layoutInflater.inflate(
                    R.layout.admob_native_small,
                    null
                ) as FrameLayout

                val headLine = adView.findViewById<TextView>(R.id.ad_headline)
                val body = adView.findViewById<TextView>(R.id.ad_body)
                val icon = adView.findViewById<ImageView>(R.id.ad_icon)
                val callToAction = adView.findViewById<Button>(R.id.ad_call_to_action)

                headLine.text = ad.headline
                body.text = ad.body
                callToAction.text = ad.callToAction
                ad.icon?.drawable?.let { draw -> icon.setImageDrawable(draw) }

                headlineView = headLine
                bodyView = body
                iconView = icon
                callToActionView = callToAction

                removeAllViews()
                setNativeAd(ad)
                addView(adView)
            }
        }
    }

    fun populateMediumNativeView(context: Context): View {
        return NativeAdView(context).apply {
            this@EonNativeAd.nativeAd?.let { ad ->

                val adView = (context as Activity).layoutInflater.inflate(
                    R.layout.admob_native_medium,
                    null
                ) as FrameLayout

                val headLine = adView.findViewById<TextView>(R.id.ad_headline)
                val advertiser = adView.findViewById<TextView>(R.id.ad_advertiser)
                val icon = adView.findViewById<ImageView>(R.id.ad_icon)
                val callToAction = adView.findViewById<Button>(R.id.ad_call_to_action)
                val adImage = adView.findViewById<ImageView>(R.id.ad_image)

                headLine.text = ad.headline
                advertiser.text = ad.advertiser
                callToAction.text = ad.callToAction
                ad.icon?.drawable?.let { draw -> icon.setImageDrawable(draw) }
                if(ad.images.isNotEmpty()){
                    ad.images[0].drawable?.let { adImage.setImageDrawable(it) }
                }

                headlineView = headLine
                advertiserView = advertiser
                iconView = icon
                callToActionView = callToAction
                imageView = adImage

                removeAllViews()
                setNativeAd(ad)
                addView(adView)
            }
        }
    }

    fun populateLargeNativeView(context: Context): View {
        return NativeAdView(context).apply {
            this@EonNativeAd.nativeAd?.let { ad ->

                val adView = (context as Activity).layoutInflater.inflate(
                    R.layout.admob_native_large,
                    null
                ) as FrameLayout

                val headLine = adView.findViewById<TextView>(R.id.ad_headline)
                val advertiser = adView.findViewById<TextView>(R.id.ad_advertiser)
                val body = adView.findViewById<TextView>(R.id.ad_body)
                val icon = adView.findViewById<ImageView>(R.id.ad_icon)
                val callToAction = adView.findViewById<Button>(R.id.ad_call_to_action)
                val adImage = adView.findViewById<ImageView>(R.id.ad_image)

                headLine.text = ad.headline
                advertiser.text = ad.advertiser
                body.text = ad.body
                callToAction.text = ad.callToAction
                ad.icon?.drawable?.let { draw -> icon.setImageDrawable(draw) }
                if(ad.images.isNotEmpty()){
                    ad.images[0].drawable?.let { adImage.setImageDrawable(it) }
                }

                headlineView = headLine
                advertiserView = advertiser
                bodyView = body
                iconView = icon
                imageView = adImage
                callToActionView = callToAction

                removeAllViews()
                addView(adView)
                setNativeAd(ad)
            }
        }
    }

    companion object{

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