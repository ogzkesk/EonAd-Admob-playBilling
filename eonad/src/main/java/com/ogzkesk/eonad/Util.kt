package com.ogzkesk.eonad

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd

class EonAdError(private val error: AdError) {

    val message: String
        get() = error.message
    val domain: String
        get() = error.domain
    val code: Int
        get() = error.code
    val cause: AdError?
        get() = error.cause
}


open class EonAds {

    internal open var interstitialAd: InterstitialAd? = null
    internal open var nativeAd: NativeAd? = null
    internal open var rewardedAd: RewardedAd? = null

}

class EonRewardedAdItem(private val item: RewardItem) {

    val amount: Int
        get() = item.amount
    val type: String
        get() = item.type
    val defaultRewardItem
        get() = RewardItem.DEFAULT_REWARD

}

internal fun Context.showToast(@StringRes message: Int){
    Toast.makeText(this,getString(message),Toast.LENGTH_SHORT).show()
}