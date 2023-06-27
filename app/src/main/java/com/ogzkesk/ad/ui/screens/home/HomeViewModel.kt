package com.ogzkesk.ad.ui.screens.home

import android.content.Context
import android.view.View
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import com.ogzkesk.ad.ad.showNativeAd
import com.ogzkesk.ad.ad.showInterstitialAd
import com.ogzkesk.ad.ad.showRewardedAd
import com.ogzkesk.eonad.EonAdError
import com.ogzkesk.eonad.ads.EonInterstitialAd
import com.ogzkesk.eonad.EonRewardedAdItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {


    private val _interState = MutableStateFlow(InterstitialAdState())
    val interState = _interState.asStateFlow()

    private val _rewardState = MutableStateFlow(RewardedAdState())
    val rewardState = _rewardState.asStateFlow()

    private val _nativeState = MutableStateFlow(NativeAdState())
    val nativeState = _nativeState.asStateFlow()

    fun loadInterstitialHome(context: Context) {
        showInterstitialAd(
            context = context,
            onAdLoading = {
                _interState.update { state ->
                    state.copy(isLoading = true)
                }
            },
            onAdFailed = { error ->
                _interState.update { state ->
                    state.copy(isLoading = false, adFailed = error)
                }
            },
            onAdLoaded = { ad ->
                _interState.update { state ->
                    state.copy(isLoading = false, adFailed = null, adLoaded = ad)
                }
            }
        )
    }

    fun loadRewardedAd(context: Context) {
        showRewardedAd(
            context = context,
            onLoading = {
                _rewardState.update {
                    it.copy(isLoading = true)
                }
            },
            onFailedToLoad = { error ->
                _rewardState.update {
                    it.copy(isLoading = false, adFailed = error)
                }
            },
            onRewardEarned = { reward ->
                _rewardState.update {
                    it.copy(isLoading = false, adFailed = null, receivedReward = reward)
                }
            }
        )
    }

    fun loadNativeAd(context: Context) {
        showNativeAd(
            context = context,
            onLoading = {
                _nativeState.update { state ->
                    state.copy(isLoading = true)
                }
            },
            onFailed = { error ->
                _nativeState.update { state ->
                    state.copy(isLoading = false, adFailed = error)
                }
            },
            onLoadedSmall = { view ->
                _nativeState.update { state ->
                    state.copy(isLoading = false, adFailed = null, smallNativeView = view)
                }
            },
            onLoadedMedium = { view ->
                _nativeState.update { state ->
                    state.copy(isLoading = false, adFailed = null, mediumNativeView = view)
                }
            },
            onLoadedLarge = { view ->
                _nativeState.update { state ->
                    state.copy(isLoading = false, adFailed = null, largeNativeView = view)
                }
            }
        )
    }


    @Stable
    data class InterstitialAdState(
        val isLoading: Boolean = false,
        val adLoaded: EonInterstitialAd? = null,
        val adFailed: EonAdError? = null
    )

    @Stable
    data class RewardedAdState(
        val isLoading: Boolean = false,
        val receivedReward: EonRewardedAdItem? = null,
        val adFailed: EonAdError? = null
    )

    @Stable
    data class NativeAdState(
        val isLoading: Boolean = false,
        val smallNativeView: View? = null,
        val mediumNativeView: View? = null,
        val largeNativeView: View? = null,
        val adFailed: EonAdError? = null
    )
}