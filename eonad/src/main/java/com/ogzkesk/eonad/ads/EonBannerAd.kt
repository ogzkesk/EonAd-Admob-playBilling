package com.ogzkesk.eonad.ads

import android.content.Context
import android.view.View
import com.google.android.gms.ads.*
import com.ogzkesk.eonad.BannerAdSize
import com.ogzkesk.eonad.EonAdCallback
import com.ogzkesk.eonad.EonAdError
import com.ogzkesk.eonad.mapBannerAdSize

private const val TAG = "EonBannerAd"

class EonBannerAd {


    internal fun loadBannerAd(context: Context,adUnitId: String,adSize: BannerAdSize): View {
        return AdView(context).apply {
            setAdSize(mapBannerAdSize(adSize))
            setAdUnitId(adUnitId)
            loadAd(AdRequest.Builder().build())
        }
    }

    internal fun loadBannerAd(
        context: Context,
        adUnitId: String,
        adSize: BannerAdSize,
        eonAdCallback: EonAdCallback
    ){
        val adView = AdView(context).apply {
            setAdSize(mapBannerAdSize(adSize))
            setAdUnitId(adUnitId)
        }

        adView.loadAd(AdRequest.Builder().build())
        adView.adListener = object : AdListener(){
            override fun onAdClicked() {
                super.onAdClicked()
                eonAdCallback.onAdClicked()
            }

            override fun onAdClosed() {
                super.onAdClosed()
                eonAdCallback.onAdClosed()
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                eonAdCallback.onAdFailedToLoad(EonAdError(p0))
            }

            override fun onAdImpression() {
                super.onAdImpression()
                eonAdCallback.onAdImpression()
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                eonAdCallback.onBannerAdLoaded(adView)
            }

            override fun onAdOpened() {
                super.onAdOpened()
                eonAdCallback.onBannerAdOpened()
            }

            override fun onAdSwipeGestureClicked() {
                super.onAdSwipeGestureClicked()
                eonAdCallback.onAdSwipeGestureClicked()
            }
        }

        if(adView.isLoading){
            eonAdCallback.onLoading()
        }
    }
}