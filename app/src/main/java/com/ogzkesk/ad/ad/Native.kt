package com.ogzkesk.ad.ad

import android.content.Context
import android.view.View
import com.ogzkesk.eonad.EonAdError

fun showNativeAd(
    context: Context,
    onLoading: () -> Unit,
    onLoadedSmall: (View) -> Unit,
    onLoadedMedium: (View) -> Unit,
    onLoadedLarge: (View) -> Unit,
    onFailed: (EonAdError) -> Unit
) {

//    val eonAd = EonAd.getInstance()
//
//    eonAd.loadNativeAd(
//        context,
//        BuildConfig.ad_native_id,
//        object : EonAdCallback {
//            override fun onLoading() {
//                super.onLoading()
//                onLoading()
//            }
//
//            override fun onNativeAdLoaded(eonNativeAd: EonNativeAd) {
//                super.onNativeAdLoaded(eonNativeAd)
//
//            }
//
//            override fun onAdFailedToLoad(error: EonAdError) {
//                super.onAdFailedToLoad(error)
//                onFailed(error)
//            }
//        }
//    )
}