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
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.ogzkesk.eonad.*

private const val TAG = "EonRewardedAd"

class EonRewardedAd() {

    var rewardedAd: RewardedAd? = null

    internal fun showRewardedAd(
        context: Context,
        adUnitId: String
    ){
        if(rewardedAd != null){
            rewardedAd!!.show(context as Activity){}
            rewardedFullScreenContentCallback(context,adUnitId,null)
            return
        }

        loadRewardedAd(context,adUnitId)
    }

    internal fun showRewardedAd(
        context: Context,
        adUnitId: String,
        eonAdCallback: EonAdCallback
    ){
        if(rewardedAd != null){
            rewardedAd!!.show(context as Activity){}
            rewardedFullScreenContentCallback(
                context,
                adUnitId,
                eonAdCallback
            )
            return
        }

        loadRewardedAd(context,adUnitId,eonAdCallback)
    }

    private fun loadRewardedAd(
        context: Context,
        adUnitId: String,
        eonAdCallback: EonAdCallback
    ){
        val root : ViewGroup? = null
        val loadingView = (context as Activity).layoutInflater.inflate(
            R.layout.loading_ad, root
        ) as FrameLayout
        val rootView = context.findViewById(android.R.id.content) as ViewGroup
        rootView.addView(loadingView)

        eonAdCallback.onLoading()
        val request = AdRequest.Builder().build()

        RewardedAd.load(
            context,
            adUnitId,
            request,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    eonAdCallback.onAdFailedToLoad(EonAdError(p0))
                    rootView.removeView(loadingView)
                    Log.d(TAG, "RewardedAd Failed to Load :: ${p0.message}")
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    super.onAdLoaded(rewardedAd)
                    Log.d(TAG, "RewardedAd Loaded:: $rewardedAd")

                    this@EonRewardedAd.rewardedAd = rewardedAd
                    eonAdCallback.onRewardedAdLoaded(this@EonRewardedAd)
                    rewardedFullScreenContentCallback(
                        context,
                        adUnitId,
                        eonAdCallback
                    )

                    rewardedAd.show(context) { reward ->
                        Log.d(TAG, "RewardedAd Loaded:: $rewardedAd")
                        eonAdCallback.onRewardEarned(EonRewardedAdItem(reward))
                    }

                    Handler(Looper.getMainLooper()).apply {
                        postDelayed({
                            rootView.removeView(loadingView)
                        }, 1_000)
                    }
                }
            }
        )
    }

    private fun loadRewardedAd(context: Context, adUnitId: String){
        val root : ViewGroup? = null
        val loadingView = (context as Activity).layoutInflater.inflate(
            R.layout.loading_ad, root
        ) as FrameLayout
        val rootView = context.findViewById(android.R.id.content) as ViewGroup
        rootView.addView(loadingView)

        val request = AdRequest.Builder().build()
        RewardedAd.load(
            context,
            adUnitId,
            request,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    rootView.removeView(loadingView)
                    context.showToast(R.string.ad_error)
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    super.onAdLoaded(ad)
                    this@EonRewardedAd.rewardedAd = ad
                    rewardedFullScreenContentCallback(context,adUnitId,null)
                    ad.show(context) {}
                    Handler(Looper.getMainLooper()).apply {
                        postDelayed({
                            rootView.removeView(loadingView)
                        }, 1_000)
                    }
                }
            }
        )
    }

    private fun rewardedFullScreenContentCallback(
        context: Context,
        adUnitId: String,
        callback: EonAdCallback?
    ) {
        rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                super.onAdClicked()
                callback?.onAdClicked()
            }

            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                callback?.onAdDismissedFullScreenContent()
                callback?.onAdClosed()
                rewardedAd = null
                onlyLoad(context,adUnitId)
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                super.onAdFailedToShowFullScreenContent(p0)
                callback?.onAdFailedToShowFullScreenContent(EonAdError(p0))
            }

            override fun onAdImpression() {
                super.onAdImpression()
                callback?.onAdImpression()
            }

            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                callback?.onAdShowedFullScreenContent()
            }
        }
    }

    private fun onlyLoad(
        context: Context,
        adUnitId: String
    ){
        val request = AdRequest.Builder().build()
        RewardedAd.load(
            context,
            adUnitId,
            request,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                }

                override fun onAdLoaded(p0: RewardedAd) {
                    super.onAdLoaded(p0)
                    rewardedAd = p0
                }
            }
        )
    }
}

