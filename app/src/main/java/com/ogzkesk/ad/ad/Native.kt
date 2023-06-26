package com.ogzkesk.ad.ad

import android.content.Context
import android.view.View
import com.ogzkesk.ad.BuildConfig
import com.ogzkesk.eonad.EonAd
import com.ogzkesk.eonad.EonAdCallback
import com.ogzkesk.eonad.EonAdError
import com.ogzkesk.eonad.EonNativeAd

fun showNativeAd(
    context: Context,
    onLoading: () -> Unit,
    onLoadedSmall: (View) -> Unit,
    onLoadedMedium: (View) -> Unit,
    onLoadedLarge: (View) -> Unit,
    onFailed: (EonAdError) -> Unit
) {

    val eonAd = EonAd.getInstance()

    eonAd.loadNativeAd(
        context,
        BuildConfig.ad_native_id,
        object : EonAdCallback {
            override fun onLoading() {
                super.onLoading()
                onLoading()
            }

            override fun onNativeAdLoaded(eonNativeAd: EonNativeAd) {
                super.onNativeAdLoaded(eonNativeAd)
                onLoadedSmall(eonNativeAd.populateSmallNativeView(context))
                onLoadedMedium(eonNativeAd.populateMediumNativeView(context))
                onLoadedLarge(eonNativeAd.populateLargeNativeView(context))
            }

            override fun onAdFailedToLoad(error: EonAdError) {
                super.onAdFailedToLoad(error)
                onFailed(error)
            }
        }
    )
}