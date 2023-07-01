package com.ogzkesk.ad.ui.screens.home

import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import com.ogzkesk.ad.BuildConfig
import com.ogzkesk.eonad.*
import com.ogzkesk.eonad.ads.EonNativeAd
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class NativeViewModel @Inject constructor() : ViewModel() {


    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun loadNativeSmallTemplate(context: Context) {
        EonAd.getInstance().loadNativeAdTemplate(
            context = context,
            adUnitId = BuildConfig.ad_native_id,
            type = NativeAdTemplate.SMALL,
            onFailedToLoad = {},
            onNativeAdLoaded = { view ->
                println("EonNativeAd -- ViewModel Small Loaded :: $view")
                _uiState.value = _uiState.value.copy(smallNativeView = view)
            },
        )
    }

    fun loadNativeMediumTemplate(context: Context) {
        EonAd.getInstance().loadNativeAdTemplate(
            context = context,
            adUnitId = BuildConfig.ad_native_id,
            type = NativeAdTemplate.MEDIUM,
            onFailedToLoad = {},
            onNativeAdLoaded = { view ->
                println("EonNativeAd -- ViewModel Medium Loaded :: $view")
                _uiState.value = _uiState.value.copy(mediumNativeView = view)
            },
        )
    }

    fun loadNativeLargeTemplate(context: Context) {
        EonAd.getInstance().loadNativeAdTemplate(
            context = context,
            adUnitId = BuildConfig.ad_native_id,
            type = NativeAdTemplate.LARGE,
            onFailedToLoad = {},
            onNativeAdLoaded = { view ->
                println("EonNativeAd -- ViewModel Large Loaded :: $view")
                _uiState.value = _uiState.value.copy(largeNativeView = view)
            },
        )
    }

    fun loadNativeAd(context: Context) {
        EonAd.getInstance().loadNativeAd(
            context,
            BuildConfig.ad_native_id,
            object : EonAdCallback {
                override fun onNativeAdLoaded(eonNativeAd: EonNativeAd) {
                    super.onNativeAdLoaded(eonNativeAd)
                    // Handle native Ad.
                }

                override fun onAdFailedToLoad(error: EonAdError) {
                    super.onAdFailedToLoad(error)

                }

                override fun onLoading() {
                    super.onLoading()

                }
            }
        )
    }

    data class UiState(
        val smallNativeView: View? = null,
        val mediumNativeView: View? = null,
        val largeNativeView: View? = null,
        val error: EonAdError? = null
    )
}