package com.ogzkesk.eonad


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

}







