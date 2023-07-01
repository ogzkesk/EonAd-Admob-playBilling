package com.ogzkesk.ad.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ogzkesk.ad.ui.navigation.Screen
import com.ogzkesk.ad.ui.showToast
import com.ogzkesk.eonad.ui.EonNativeAdView
fun NavGraphBuilder.native() {
    composable(route = Screen.Home.route) {
        Native()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Native() {

    val viewModel: NativeViewModel = hiltViewModel()
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.error){
        if(uiState.error != null){
            context.showToast(uiState.error?.message ?: "")
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "Native")}) }
    ) { padd ->

        LazyColumn(
            contentPadding = padd,
            content = {

                item {
                    Tool(
                        onLoadNativeTemplatesClick = {
                            viewModel.loadNativeSmallTemplate(context)
                            viewModel.loadNativeMediumTemplate(context)
                            viewModel.loadNativeLargeTemplate(context)
                        }
                    )
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item {
                    EonNativeAdView(
                        nativeAdView = uiState.smallNativeView,
                        darkModeSupport = true
                    )
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item {
                    EonNativeAdView(
                        nativeAdView = uiState.mediumNativeView,
                        darkModeSupport = true
                    )
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item {
                    EonNativeAdView(
                        nativeAdView = uiState.largeNativeView,
                        darkModeSupport = true
                    )
                }
            }
        )
    }
}