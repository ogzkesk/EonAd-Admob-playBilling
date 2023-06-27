package com.ogzkesk.eonad

import android.view.View
import com.ogzkesk.eonad.ads.EonInterstitialAd
import com.ogzkesk.eonad.ads.EonNativeAd
import com.ogzkesk.eonad.ads.EonRewardedAd


interface EonAdCallback {

    fun onLoading() {

    }

    fun onAdClosed() {

    }

    fun onAdClicked() {

    }

    fun onAdImpression() {

    }

    fun onAdShowedFullScreenContent() {

    }

    fun onAdDismissedFullScreenContent() {

    }

    fun onAdFailedToShowFullScreenContent(error: EonAdError) {

    }

    fun onAdFailedToLoad(error: EonAdError) {

    }

    // ------- interstitial --

    fun onInterstitialAdLoaded(ad: EonInterstitialAd) {

    }


   //--------- rewarded -----

    fun onRewardEarned(rewardItem: EonRewardedAdItem){

    }

    fun onRewardedAdLoaded(ad: EonRewardedAd) {

    }

    // ------- native -----

    fun onNativeAdLoaded(eonNativeAd: EonNativeAd){

    }

    fun onNativeAdOpened(){

    }

    fun onAdSwipeGestureClicked(){

    }

    // -------- banner --

    fun onBannerAdLoaded(bannerAdView: View){

    }

    fun onBannerAdOpened(){

    }

}







