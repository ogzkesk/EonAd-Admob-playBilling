package com.ogzkesk.ad.ad

import android.content.Context
import com.ogzkesk.ad.BuildConfig
import com.ogzkesk.eonad.EonAd
import com.ogzkesk.eonad.EonAdCallback
import com.ogzkesk.eonad.EonAdError
import com.ogzkesk.eonad.EonRewardedAdItem

private const val TAG = "EonAd-Ad-Package-Rewarded"

fun showRewardedAd(
    context: Context,
    onLoading: () -> Unit,
    onRewardEarned: (EonRewardedAdItem) -> Unit,
    onFailedToLoad: (EonAdError) -> Unit
) {

    val eonAd = EonAd.getInstance()

    eonAd.loadRewardedAd(context,BuildConfig.ad_rewarded_id, object : EonAdCallback {
        override fun onLoading() {
            super.onLoading()
            onLoading.invoke()
        }

        override fun onRewardEarned(rewardItem: EonRewardedAdItem) {
            super.onRewardEarned(rewardItem)
            onRewardEarned.invoke(rewardItem)
        }

        override fun onAdFailedToLoad(error: EonAdError) {
            super.onAdFailedToLoad(error)
            onFailedToLoad.invoke(error)
        }
    })
}