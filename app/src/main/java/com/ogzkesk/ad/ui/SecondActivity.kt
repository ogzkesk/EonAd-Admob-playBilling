package com.ogzkesk.ad.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.ogzkesk.ad.BuildConfig
import com.ogzkesk.ad.databinding.ActivitySecondBinding
import com.ogzkesk.eonad.BannerAdSize
import com.ogzkesk.eonad.EonAd
import com.ogzkesk.eonad.EonAdCallback
import com.ogzkesk.eonad.EonRewardedAdItem
import com.ogzkesk.eonad.NativeAdTemplate
import com.ogzkesk.eonad.billing.Iap
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.launch

private const val TAG = "SecondActivity"
class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding
    private val iap = Iap.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val subs = iap.getSubscriptions()
        val inapp = iap.getInAppProducts()

        binding.apply {

            btnSubscribe.setOnClickListener {
                if (iap.isConnected()) {
                    if (subs.isNotEmpty()) {

                    }
                }
            }

            btnPurchase.setOnClickListener {
                if (iap.isConnected()) {
                    if (inapp.isNotEmpty()) {
                        lifecycleScope.launch {
                            iap.purchase(this@SecondActivity,inapp.first().productId)
                                .listen{
                                    if (it.purchases.isNotEmpty()) {
                                        val productCount = Hawk.get<Int>("product")
                                        Hawk.put("product", productCount + 50)
                                        textInapp.text = "${productCount + 50}"
                                    } else {
                                        println("Iap Purchase Empty ${it.result.debugMessage}")
                                    }
                                }

                        }
                    }
                }
            }

            btnShowInterstitialAd.setOnClickListener {
                EonAd.getInstance()
                    .loadInterstitialAdWithInterval(
                        this@SecondActivity,
                        BuildConfig.ad_interstitial_id,
                        40_000
                    )
            }

            btnShowRewardedAd.setOnClickListener {
                EonAd.getInstance().loadRewardedAd(
                    this@SecondActivity,
                    BuildConfig.ad_rewarded_id,
                    object : EonAdCallback {
                        override fun onRewardEarned(rewardItem: EonRewardedAdItem) {
                            Log.d(TAG,"reward : ${rewardItem.amount}")
                            super.onRewardEarned(rewardItem)
                        }
                    }
                )
            }

            btnEnableResumeAds.setOnClickListener { EonAd.getInstance().enableResumeAds() }

            btnDisableResumeAds.setOnClickListener { EonAd.getInstance().disableResumeAds() }

            btnDisableResumeOnClick.setOnClickListener {
                EonAd.getInstance().disableResumeAdsOnClickEvent()
                val intent = Intent(Intent.ACTION_VIEW, "https://play.google.com".toUri())
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

    private fun loadBannerAds() = with(binding) {
        banner2Container.viewTreeObserver.addOnGlobalLayoutListener {

        }
        val banner1 = EonAd.getInstance().loadBannerAd(
            this@SecondActivity,
            BuildConfig.ad_banner_id,
            BannerAdSize.BANNER
        )
        val banner2 = EonAd.getInstance().loadBannerAd(
            this@SecondActivity,
            BuildConfig.ad_banner_id,
            BannerAdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this@SecondActivity, 320)
        )
        val banner3 = EonAd.getInstance().loadBannerAd(
            this@SecondActivity,
            BuildConfig.ad_banner_id,
            BannerAdSize.SMART_BANNER
        )

        banner1Container.removeAllViews()
        banner1Container.addView(banner1)
        banner2Container.removeAllViews()
        banner2Container.addView(banner2)
        banner3Container.removeAllViews()
        banner3Container.addView(banner3)
    }

    private fun loadSmallNativeAd(onLoad: (View) -> Unit) {
        val eonAd = EonAd.getInstance()
        eonAd.loadNativeAdTemplate(this, BuildConfig.ad_native_id, NativeAdTemplate.SMALL) {
            onLoad.invoke(it)
        }
    }

    private fun loadMediumNativeAd(onLoad: (View) -> Unit) {
        val eonAd = EonAd.getInstance()
        eonAd.loadNativeAdTemplate(this, BuildConfig.ad_native_id, NativeAdTemplate.MEDIUM) {
            onLoad.invoke(it)
        }
    }

    private fun loadLargeNativeAd(onLoad: (View) -> Unit) {
        val eonAd = EonAd.getInstance()
        eonAd.loadNativeAdTemplate(this, BuildConfig.ad_native_id, NativeAdTemplate.LARGE) {
            onLoad.invoke(it)
        }
    }

}