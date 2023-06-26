package com.ogzkesk.ad.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ogzkesk.ad.BuildConfig
import com.ogzkesk.ad.R
import com.ogzkesk.ad.databinding.ActivitySecondBinding
import com.ogzkesk.eonad.*

class SecondActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        EonAd.getInstance().loadNativeAd(this, BuildConfig.ad_native_id){ nativeAd ->
            val adView = nativeAd.populateLargeNativeView(this@SecondActivity)
            binding.flNativeAdContainer.removeAllViews()
            binding.flNativeAdContainer.addView(adView)
        }

        binding.btnFirstActivity.setOnClickListener {
//            val intent = Intent(this,MainActivity::class.java)
//            startActivity(intent)
//            finish()
            EonAd.getInstance().loadInterstitialAd(this, BuildConfig.ad_interstitial_id)
        }

        binding.btnSecond.setOnClickListener {
//            val intent = Intent(this,MainActivity::class.java)
//            startActivity(intent)
//            finish()
            EonAd.getInstance().loadRewardedAd(this, BuildConfig.ad_rewarded_id)
        }
    }
}