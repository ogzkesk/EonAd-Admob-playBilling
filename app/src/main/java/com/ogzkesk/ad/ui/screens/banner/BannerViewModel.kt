package com.ogzkesk.ad.ui.screens.banner

import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import com.ogzkesk.ad.BuildConfig
import com.ogzkesk.eonad.BannerAdSize
import com.ogzkesk.eonad.EonAd
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
@HiltViewModel
class BannerViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun loadBannerAds(context: Context){
        val banner = EonAd.getInstance().loadBannerAd(
            context = context,
            adUnitId = BuildConfig.ad_banner_id,
            adSize = BannerAdSize.BANNER
        )
        val smart = EonAd.getInstance().loadBannerAd(
            context = context,
            adUnitId = BuildConfig.ad_banner_id,
            adSize = BannerAdSize.SMART_BANNER
        )
        val full = EonAd.getInstance().loadBannerAd(
            context = context,
            adUnitId = BuildConfig.ad_banner_id,
            adSize = BannerAdSize.SMART_BANNER
        )
        val large = EonAd.getInstance().loadBannerAd(
            context = context,
            adUnitId = BuildConfig.ad_banner_id,
            adSize = BannerAdSize.LARGE_BANNER
        )
        val rec = EonAd.getInstance().loadBannerAd(
            context = context,
            adUnitId = BuildConfig.ad_banner_id,
            adSize = BannerAdSize.MEDIUM_RECTANGLE
        )
        val wide = EonAd.getInstance().loadBannerAd(
            context = context,
            adUnitId = BuildConfig.ad_banner_id,
            adSize = BannerAdSize.WIDE_SKYSCRAPER
        )
        val fluid = EonAd.getInstance().loadBannerAd(
            context = context,
            adUnitId = BuildConfig.ad_banner_id,
            adSize = BannerAdSize.FLUID
        )
        val leaderboard = EonAd.getInstance().loadBannerAd(
            context = context,
            adUnitId = BuildConfig.ad_banner_id,
            adSize = BannerAdSize.LEADERBOARD
        )
        val adaptive = EonAd.getInstance().loadBannerAd(
            context = context,
            adUnitId = BuildConfig.ad_banner_id,
            adSize = BannerAdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context,320)
        )

        _uiState.update {
            it.copy(
                banner = banner,
                full = full,
                smart = smart,
                large = large,
                rec = rec,
                wide = wide,
                fluid = fluid,
                leaderboard = leaderboard,
                adaptive = adaptive
            )
        }
    }


    data class UiState(
        val banner: View? = null,
        val full: View? = null,
        val smart: View? = null,
        val large: View? = null,
        val rec: View? = null,
        val wide: View? = null,
        val fluid: View? = null,
        val leaderboard: View? = null,
        val adaptive: View? = null
    )
}