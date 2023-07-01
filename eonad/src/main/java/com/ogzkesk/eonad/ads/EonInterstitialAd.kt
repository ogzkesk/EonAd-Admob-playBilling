package com.ogzkesk.eonad.ads

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.ogzkesk.eonad.*

private const val TAG = "EonInterstitialAd"

class EonInterstitialAd() {

    var interstitialAd: InterstitialAd? = null

    internal fun loadInterstitialAd(
        context: Context,
        adUnitId: String,
        eonAdCallback: EonAdCallback
    ) {
        val root : ViewGroup? = null
        eonAdCallback.onLoading()
        val request = AdRequest.Builder().build()

        val loadingView = (context as Activity).layoutInflater.inflate(
            R.layout.loading_ad, root
        ) as FrameLayout
        val rootView = context.findViewById(android.R.id.content) as ViewGroup
        rootView.addView(loadingView)

        InterstitialAd.load(
            context,
            adUnitId,
            request,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    super.onAdFailedToLoad(error)
                    rootView.removeView(loadingView)
                    this@EonInterstitialAd.interstitialAd = null
                    eonAdCallback.onAdFailedToLoad(EonAdError(error))
                    context.showToast(R.string.ad_error)
                    Log.d(TAG, "InterstitialAd Failed to Load :: ${error.message}")
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    super.onAdLoaded(interstitialAd)
                    this@EonInterstitialAd.interstitialAd = interstitialAd
                    eonAdCallback.onInterstitialAdLoaded(this@EonInterstitialAd)
                    interFullScreenContentCallback(eonAdCallback)
                    interstitialAd.show(context)
                    Handler(Looper.getMainLooper()).apply {
                        postDelayed({
                            rootView.removeView(loadingView)
                        }, 1_000)
                    }
                    Log.d(TAG, "InterstitialAd Loaded")
                }
            }
        )
    }

    internal fun loadInterstitialAd(context: Context,adUnitId: String){
        val root : ViewGroup? = null
        val loadingView = (context as Activity).layoutInflater.inflate(
            R.layout.loading_ad, root
        ) as FrameLayout
        val rootView = context.findViewById(android.R.id.content) as ViewGroup
        rootView.addView(loadingView)

        val request = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            adUnitId,
            request,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    rootView.removeView(loadingView)
                    context.showToast(R.string.ad_error)
                }

                override fun onAdLoaded(p0: InterstitialAd) {
                    super.onAdLoaded(p0)
                    p0.show(context)
                    Handler(Looper.getMainLooper()).apply {
                        postDelayed({
                            rootView.removeView(loadingView)
                        }, 1_000)
                    }
                }
            })
    }

    private fun interFullScreenContentCallback(callback: EonAdCallback) {
        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                super.onAdClicked()
                callback.onAdClicked()
            }

            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                callback.onAdDismissedFullScreenContent()
                callback.onAdClosed()
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                super.onAdFailedToShowFullScreenContent(p0)
                callback.onAdFailedToShowFullScreenContent(EonAdError(p0))
            }

            override fun onAdImpression() {
                super.onAdImpression()
                callback.onAdImpression()
            }

            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                callback.onAdShowedFullScreenContent()
            }
        }
    }
}