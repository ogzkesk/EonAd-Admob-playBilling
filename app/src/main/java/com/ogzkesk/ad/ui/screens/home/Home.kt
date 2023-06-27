package com.ogzkesk.ad.ui.screens.home

import android.app.Activity
import android.content.Intent
import android.widget.Space
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ogzkesk.ad.ui.SecondActivity
import com.ogzkesk.ad.ui.navigation.Screen
import com.ogzkesk.ad.ui.navigation.setup.navigator
import com.ogzkesk.ad.ui.screens.splash.Splash
import com.ogzkesk.eonad.EonAd
import com.ogzkesk.eonad.ui.EonNativeAdView

fun NavGraphBuilder.home() {
    composable(route = Screen.Home.route) {
        Home()
    }
}

@Composable
fun Home() {

    val viewModel: HomeViewModel = hiltViewModel()
    val nativeState by viewModel.nativeState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadNativeAd(context = context)
    }

    Scaffold {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                EonAd.getInstance().disableResumeAdsOnClickEvent()
                val intent = Intent(context, SecondActivity::class.java)
                context.startActivity(intent)
                (context as Activity).finish()
            }) {
                Text(text = "NextScreen")
            }

            Button(onClick = { viewModel.loadInterstitialHome(context) }) {
                Text(text = "Show Interstitial")
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { viewModel.loadRewardedAd(context) }) {
                Text(text = "Show Rewarded")
            }

            Spacer(modifier = Modifier.height(24.dp))
            EonNativeAdView(
                nativeAdView = nativeState.smallNativeView,
                darkModeSupport = true
            )
            Spacer(modifier = Modifier.height(24.dp))
            EonNativeAdView(
                nativeAdView = nativeState.mediumNativeView,
                darkModeSupport = true
            )
            Spacer(modifier = Modifier.height(24.dp))
            EonNativeAdView(
                nativeAdView = nativeState.largeNativeView,
                darkModeSupport = true
            )
        }
    }
}