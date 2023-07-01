package com.ogzkesk.ad.ui.screens.home

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.ogzkesk.ad.BuildConfig
import com.ogzkesk.ad.ui.SecondActivity
import com.ogzkesk.ad.ui.navigation.Screen
import com.ogzkesk.ad.ui.navigation.setup.navigator
import com.ogzkesk.eonad.EonAd
import com.ogzkesk.eonad.EonAdCallback
import com.ogzkesk.eonad.EonAdError
import com.ogzkesk.eonad.EonRewardedAdItem
import com.ogzkesk.eonad.ads.EonRewardedAd

@Composable
fun Tool(onLoadNativeTemplatesClick: () -> Unit){

    val context = LocalContext.current
    val navigator = navigator

    var resumeAdsEnabled by remember { mutableStateOf(true) }
    LaunchedEffect(resumeAdsEnabled){
        if(resumeAdsEnabled){
            println("resumeAds Enabled girdi")
            EonAd.getInstance().enableResumeAds()
        } else {
            println("resumeAds Disabled girdi")
            EonAd.getInstance().disableResumeAds()
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(onClick = {
            EonAd.getInstance().loadInterstitialAd(context, BuildConfig.ad_interstitial_id,object : EonAdCallback{
                override fun onLoading() {
                    super.onLoading()
                    println("onAdLoading")
                    resumeAdsEnabled = false
                }

                override fun onAdClosed() {
                    super.onAdClosed()
                    println("onAdClosed")
                    resumeAdsEnabled = true
                }
            })
        }) {
            Text(text = "Show Interstitial")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            EonAd.getInstance().loadRewardedAd(context, BuildConfig.ad_rewarded_id,object: EonAdCallback{
                override fun onLoading() {
                    super.onLoading()
                    println("onRewardedOnLoading.")
                    resumeAdsEnabled = false
                }

                override fun onAdClosed() {
                    super.onAdClosed()
                    println("onAdClosed")
                    resumeAdsEnabled = true
                }
            })
        }) {
            Text(text = "Show Rewarded")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            EonAd.getInstance().enableResumeAds()
        }) {
            Text(text = "Enable ResumeAds")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            EonAd.getInstance().disableResumeAds()
        }) {
            Text(text = "Disable ResumeAds")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            EonAd.getInstance().disableResumeAdsOnClickEvent()
            val intent = Intent(Intent.ACTION_VIEW,"https://play.google.com".toUri())
            context.startActivity(intent)
        }) {
            Text(text = "Disable ResumeAds On Click Event")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navigator.navigate(Screen.Banner.route)
        }) {
            Text(text = "Go Banner Screen")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            (context as Activity).apply {
                startActivity(Intent(context, SecondActivity::class.java))
                finish()
            }
        }) {
            Text(text = "Go Second Activity")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navigator.navigate(Screen.Products.route) }) {
            Text(text = "Go Product Screen")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onLoadNativeTemplatesClick) {
            Text(text = "Load NativeAd Templates")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}