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

        EonAd.getInstance().loadNativeAdTemplate(
            context = this,
            adUnitId = BuildConfig.ad_native_id,
            type = NativeAdTemplateType.LARGE, // set one of them SMALL,MEDIUM,LARGE
            onNativeAdLoaded = { error, view ->

                if(error != null){
                    // handle error
                    return@loadNativeAdTemplate
                }

                binding.flNativeAdContainer.removeAllViews()
                binding.flNativeAdContainer.addView(view)
            }
        )

        EonAd.getInstance().loadNativeAd(this, BuildConfig.ad_native_id) { ad ->
            ad.nativeAd?.let {
                // update your own ui
            }
        }

        EonAd.getInstance().loadNativeAd(this,BuildConfig.ad_native_id,object : EonAdCallback {
            override fun onLoading() {
                super.onLoading()
            }

            override fun onAdClicked() {
                super.onAdClicked()
            }

            override fun onAdFailedToLoad(error: EonAdError) {
                super.onAdFailedToLoad(error)
            }

            override fun onNativeAdLoaded(eonNativeAd: EonNativeAd) {
                super.onNativeAdLoaded(eonNativeAd)
                eonNativeAd.nativeAd?.let {
                    // update your ui
                }
            }
        })
    }
}