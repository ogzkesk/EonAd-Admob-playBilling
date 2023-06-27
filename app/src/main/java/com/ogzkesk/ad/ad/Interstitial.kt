package com.ogzkesk.ad.ad

import android.content.Context
import android.util.Log
import com.ogzkesk.ad.BuildConfig
import com.ogzkesk.eonad.*
import com.ogzkesk.eonad.ads.EonInterstitialAd

private const val TAG = "EonAd-Ad-Package-Interstitial"

fun showInterstitialAd(
    context: Context,
    onAdLoaded: (EonInterstitialAd) -> Unit,
    onAdFailed: (EonAdError) -> Unit,
    onAdLoading: () -> Unit,
) {

    EonAd.getInstance().apply {

        loadInterstitialAd(context,BuildConfig.ad_interstitial_id, object : EonAdCallback {
            override fun onLoading() {
                super.onLoading()
                onAdLoading()
                Log.d(TAG, "interstitialAd Loading..")
            }

            override fun onAdFailedToLoad(error: EonAdError) {
                super.onAdFailedToLoad(error)
                onAdFailed(error)
            }

            override fun onInterstitialAdLoaded(ad: EonInterstitialAd) {
                super.onInterstitialAdLoaded(ad)
                onAdLoaded(ad)
            }
        })
    }
}