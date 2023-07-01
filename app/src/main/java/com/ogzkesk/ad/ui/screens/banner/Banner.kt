package com.ogzkesk.ad.ui.screens.banner

import android.view.View
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ogzkesk.ad.ui.navigation.Screen
import com.ogzkesk.ad.ui.navigation.setup.navigator
import com.ogzkesk.eonad.ui.EonBannerView
import com.ogzkesk.eonad.ui.EonNativeAdView

fun NavGraphBuilder.banner() {
    composable(Screen.Banner.route) {
        Banner()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Banner() {

    val navigator = navigator
    val context = LocalContext.current
    val viewModel : BannerViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Banner") },
                navigationIcon = {
                    IconButton(onClick = { navigator.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { padd ->

        LazyColumn(
            contentPadding = padd,
            content = {
                item {
                    Button(onClick = { viewModel.loadBannerAds(context) }) {
                        Text(text = "Show Banner Ads")
                    }
                }
                item {
                    EonBannerView(view = uiState.banner)
                    Spacer(modifier = Modifier.height(16.dp))
                    EonBannerView(view = uiState.adaptive)
                    Spacer(modifier = Modifier.height(16.dp))
                    EonBannerView(view = uiState.smart)
                    Spacer(modifier = Modifier.height(16.dp))
                    EonBannerView(view = uiState.full)
                    Spacer(modifier = Modifier.height(16.dp))
                    EonBannerView(view = uiState.large)
                    Spacer(modifier = Modifier.height(16.dp))
                    EonBannerView(view = uiState.fluid)
                    Spacer(modifier = Modifier.height(16.dp))
                    EonBannerView(view = uiState.wide)
                    Spacer(modifier = Modifier.height(16.dp))
                    EonBannerView(view = uiState.rec)
                    Spacer(modifier = Modifier.height(16.dp))
                    EonBannerView(view = uiState.leaderboard)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        )
    }
}

