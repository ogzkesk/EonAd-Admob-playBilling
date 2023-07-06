package com.ogzkesk.eonad

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdSize
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

    open var interstitialAd: InterstitialAd? = null
    open var nativeAd: NativeAd? = null
    open var rewardedAd: RewardedAd? = null

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

class BannerAdSize private constructor(
    val width: Int,
    val height: Int,
    val adSize: AdSize
){



    companion object {

        fun getCurrentOrientationAnchoredAdaptiveBannerAdSize(context: Context, width: Int): BannerAdSize {
            val adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context,width)
            return BannerAdSize(adSize.width,adSize.height,adSize)
        }

        fun getCurrentOrientationInlineAdaptiveBannerAdSize(context: Context, width: Int): BannerAdSize {
            val adSize = AdSize.getCurrentOrientationInlineAdaptiveBannerAdSize(context,width)
            return BannerAdSize(adSize.width,adSize.height,adSize)
        }

        fun getCurrentOrientationInterscrollerAdSize(context: Context, width: Int): BannerAdSize {
            val adSize = AdSize.getCurrentOrientationInterscrollerAdSize(context,width)
            return BannerAdSize(adSize.width,adSize.height,adSize)
        }

        fun getInlineAdaptiveBannerAdSize(width: Int, maxHeight: Int): BannerAdSize {
            val adSize = AdSize.getInlineAdaptiveBannerAdSize(width,maxHeight)
            return BannerAdSize(adSize.width,adSize.height,adSize)
        }

        fun getLandscapeAnchoredAdaptiveBannerAdSize(context: Context, width: Int): BannerAdSize {
            val adSize = AdSize.getLandscapeAnchoredAdaptiveBannerAdSize(context,width)
            return BannerAdSize(adSize.width,adSize.height,adSize)
        }

        fun getLandscapeInlineAdaptiveBannerAdSize(context: Context, width: Int): BannerAdSize {
            val adSize = AdSize.getLandscapeInlineAdaptiveBannerAdSize(context,width)
            return BannerAdSize(adSize.width,adSize.height,adSize)
        }

        fun getLandscapeInterscrollerAdSize(context: Context, width: Int): BannerAdSize {
            val adSize = AdSize.getLandscapeInterscrollerAdSize(context,width)
            return BannerAdSize(adSize.width,adSize.height,adSize)
        }

        fun getPortraitAnchoredAdaptiveBannerAdSize(context: Context, width: Int): BannerAdSize {
            val adSize = AdSize.getPortraitAnchoredAdaptiveBannerAdSize(context,width)
            return BannerAdSize(adSize.width,adSize.height,adSize)
        }

        fun getPortraitInlineAdaptiveBannerAdSize(context: Context, width: Int): BannerAdSize {
            val adSize = AdSize.getPortraitInlineAdaptiveBannerAdSize(context,width)
            return BannerAdSize(adSize.width,adSize.height,adSize)
        }

        fun getPortraitInterscrollerAdSize(context: Context, width: Int): BannerAdSize {
            val adSize = AdSize.getPortraitInterscrollerAdSize(context,width)
            return BannerAdSize(adSize.width,adSize.height,adSize)
        }

        val BANNER: BannerAdSize = BannerAdSize(0,0,AdSize.BANNER)
        val FULL_BANNER = BannerAdSize(0,0,AdSize.FULL_BANNER)
        val LARGE_BANNER = BannerAdSize(0,0,AdSize.LARGE_BANNER)
        val LEADERBOARD = BannerAdSize(0,0,AdSize.LEADERBOARD)
        val MEDIUM_RECTANGLE = BannerAdSize(0,0,AdSize.MEDIUM_RECTANGLE)
        val WIDE_SKYSCRAPER = BannerAdSize(0,0,AdSize.WIDE_SKYSCRAPER)

        @Deprecated("")
        val SMART_BANNER = BannerAdSize(0,0,AdSize.SMART_BANNER)
        val FLUID = BannerAdSize(0,0,AdSize.FLUID)
        val INVALID = BannerAdSize(0,0,AdSize.INVALID)
    }
}

internal fun mapBannerAdSize(adSize : BannerAdSize) : AdSize {
    return when(adSize){
        BannerAdSize.BANNER -> AdSize.BANNER
        BannerAdSize.FULL_BANNER -> AdSize.FULL_BANNER
        BannerAdSize.LARGE_BANNER -> AdSize.LARGE_BANNER
        BannerAdSize.LEADERBOARD -> AdSize.LEADERBOARD
        BannerAdSize.MEDIUM_RECTANGLE -> AdSize.MEDIUM_RECTANGLE
        BannerAdSize.WIDE_SKYSCRAPER -> AdSize.WIDE_SKYSCRAPER
        BannerAdSize.SMART_BANNER -> AdSize.SMART_BANNER
        BannerAdSize.FLUID -> AdSize.FLUID
        BannerAdSize.INVALID -> AdSize.INVALID
        else -> { adSize.adSize }
    }
}


enum class NativeAdTemplate {
    SMALL,
    MEDIUM,
    MEDIUM_2,
    LARGE
}