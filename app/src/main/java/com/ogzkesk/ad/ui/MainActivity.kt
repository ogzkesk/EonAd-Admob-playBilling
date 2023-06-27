package com.ogzkesk.ad.ui

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ogzkesk.ad.ui.navigation.Root
import com.ogzkesk.ad.ui.theme.AdTheme
import com.ogzkesk.eonad.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bannerView = EonAd.getInstance().loadBannerAd(
            this,
            "",
            BannerAdSize.BANNER
        )

        EonAd.getInstance().loadBannerAd(
            context = this,
            adUnitId = com.ogzkesk.ad.BuildConfig.ad_native_id,
            adSize = BannerAdSize.BANNER,
            eonAdCallback = object : EonAdCallback {
                override fun onAdClicked() {
                    super.onAdClicked()
                }

                override fun onBannerAdLoaded(bannerAdView: View) {
                    super.onBannerAdLoaded(bannerAdView)
                    // updateUi
                }

                override fun onLoading() {
                    super.onLoading()

                }
            }
        )



        setContent {
            AdTheme {
                Root()
            }
        }
    }
}