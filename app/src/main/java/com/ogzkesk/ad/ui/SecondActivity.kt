package com.ogzkesk.ad.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.ogzkesk.ad.BuildConfig
import com.ogzkesk.ad.R
import com.ogzkesk.ad.databinding.ActivitySecondBinding
import com.ogzkesk.eonad.*

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bannerView = EonAd.getInstance().loadBannerAd(
            context = this,
            adUnitId = BuildConfig.ad_banner_id,
            adSize = BannerAdSize.BANNER
        )
        binding.flNativeAdContainer.removeAllViews()
        binding.flNativeAdContainer.addView(bannerView)

        binding.btnFirstActivity.text = "ENABLE RESUME ADS"
        binding.btnSecond.text = "DISABLE RESUME ADS"

        binding.btnFirstActivity.setOnClickListener {
            EonAd.getInstance().enableResumeAds()
        }
        binding.btnSecond.setOnClickListener {
            EonAd.getInstance().disableResumeAd()
        }
    }
}