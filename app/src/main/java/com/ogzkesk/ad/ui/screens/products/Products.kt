package com.ogzkesk.ad.ui.screens.products

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ogzkesk.ad.BuildConfig
import com.ogzkesk.ad.ui.navigation.Screen
import com.ogzkesk.ad.ui.navigation.setup.navigator
import com.ogzkesk.ad.ui.space
import com.ogzkesk.eonad.billing.Iap
import com.ogzkesk.eonad.billing.ProductDetail

fun NavGraphBuilder.products() {
    composable(Screen.Products.route) {
        Products()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Products() {

    val navigator = navigator
    val viewModel: ProductViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isUserPro by viewModel.isUserPro
    val product by viewModel.product
    val product2 by viewModel.product2

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Products") },
                navigationIcon = {
                    IconButton(onClick = { navigator.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            contentPadding = paddingValues,
            content = {
                space(24)
                item {
                    Text(
                        text = "Subs",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
                space(24)
                items(uiState.subs) { ProductContainer(it, viewModel::launchPurchase) }
                space(24)
                item {
                    Text(
                        text = "Products",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
                space(24)
                items(uiState.inapp) { ProductContainer(it, viewModel::launchPurchase) }
                space(24)
                item {
                    Button(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        onClick = { Iap.getInstance().release() },
                        content = {
                            Text(text = "Release BillingClient")
                        }
                    )
                }
                space(24)
                item {
                    Button(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        onClick = { Iap.getInstance().connect() },
                        content = {
                            Text(text = "Start BillingClient")
                        }
                    )
                }
                space(24)
                item {
                    if (isUserPro) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(Color.Black),
                            contentAlignment = Alignment.Center,
                            content = {
                                Text(text = "USER PRO", color = Color.White)
                            }
                        )
                    }
                }
                space(24)
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(Color.Black),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        content = {
                            Text(text = "PRODUCT COUNT :: $product", color = Color.White)
                            Text(text = "PRODUCT2 COUNT :: $product2", color = Color.White)
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "Version : ${BuildConfig.VERSION_NAME}",
                                color = Color.White
                            )
                        }
                    )
                }
            }
        )
    }
}

@Composable
fun ProductContainer(product: ProductDetail, onClick: (String, Activity) -> Unit) {
    val context = LocalContext.current
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onClick(product.productId, context as Activity) }
            .padding(24.dp)
    ) {
        Text(text = product.name)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = product.formattedPrice)
    }
}

