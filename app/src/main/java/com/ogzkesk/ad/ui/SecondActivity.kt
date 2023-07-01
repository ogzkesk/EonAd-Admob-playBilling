package com.ogzkesk.ad.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import com.ogzkesk.ad.BuildConfig
import com.ogzkesk.ad.databinding.ActivitySecondBinding
import com.ogzkesk.eonad.*
import com.ogzkesk.eonad.billing.Iap

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Iap.getInstance().launchPurchaseFlow("product_id",this)
            .listener {
                if(it != null){
                    println("Iap ::::: error launch :: $it")
                } else {
                    println("Iap ::::: success.")
                }
            }

        binding.apply {

            btnShowInterstitialAd.setOnClickListener {
                EonAd.getInstance().loadInterstitialAd(this@SecondActivity,BuildConfig.ad_interstitial_id)
            }

            btnShowRewardedAd.setOnClickListener {
                EonAd.getInstance().loadRewardedAd(this@SecondActivity,BuildConfig.ad_rewarded_id)
            }

            btnEnableResumeAds.setOnClickListener { EonAd.getInstance().enableResumeAds() }

            btnDisableResumeAds.setOnClickListener { EonAd.getInstance().disableResumeAds() }

            btnDisableResumeOnClick.setOnClickListener {
                EonAd.getInstance().disableResumeAdsOnClickEvent()
                val intent = Intent(Intent.ACTION_VIEW,"https://play.google.com".toUri())
                startActivity(intent)
            }

            btnShowBannerAds.setOnClickListener { loadBannerAds() }

            btnShowNativeAds.setOnClickListener {
                loadSmallNativeAd {
                    nativeSmallContainer.removeAllViews()
                    nativeSmallContainer.addView(it)
                }
                loadMediumNativeAd {
                    nativeMediumContainer.removeAllViews()
                    nativeMediumContainer.addView(it)
                }
                loadLargeNativeAd {
                    nativeLargeContainer.removeAllViews()
                    nativeLargeContainer.addView(it)
                }
            }
        }
    }

    private fun loadBannerAds() = with(binding){
        banner2Container.viewTreeObserver.addOnGlobalLayoutListener {

        }
        val banner1 = EonAd.getInstance().loadBannerAd(
            this@SecondActivity,
            BuildConfig.ad_banner_id,
            BannerAdSize.BANNER)
        val banner2 = EonAd.getInstance().loadBannerAd(
            this@SecondActivity,
            BuildConfig.ad_banner_id,
            BannerAdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this@SecondActivity, 320)
        )
        val banner3 = EonAd.getInstance().loadBannerAd(
            this@SecondActivity,
            BuildConfig.ad_banner_id,
            BannerAdSize.SMART_BANNER)

        banner1Container.removeAllViews()
        banner1Container.addView(banner1)
        banner2Container.removeAllViews()
        banner2Container.addView(banner2)
        banner3Container.removeAllViews()
        banner3Container.addView(banner3)
    }
    private fun loadSmallNativeAd(onLoad: (View) -> Unit){
        val eonAd = EonAd.getInstance()
        eonAd.loadNativeAdTemplate(this, BuildConfig.ad_native_id, NativeAdTemplate.SMALL){
            onLoad.invoke(it)
        }
    }

    private fun loadMediumNativeAd(onLoad: (View) -> Unit){
        val eonAd = EonAd.getInstance()
        eonAd.loadNativeAdTemplate(this, BuildConfig.ad_native_id, NativeAdTemplate.MEDIUM){
            onLoad.invoke(it)
        }
    }

    private fun loadLargeNativeAd(onLoad: (View) -> Unit){
        val eonAd = EonAd.getInstance()
        eonAd.loadNativeAdTemplate(this, BuildConfig.ad_native_id, NativeAdTemplate.LARGE){
            onLoad.invoke(it)
        }
    }
}