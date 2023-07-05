package com.ogzkesk.ad.ui.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ogzkesk.ad.ui.navigation.setup.ProvideNavHost
import com.ogzkesk.ad.ui.screens.banner.banner
import com.ogzkesk.ad.ui.screens.home.native
import com.ogzkesk.ad.ui.screens.products.products


@Composable
fun Root() {

    val navController = rememberNavController()

    ProvideNavHost(navHostController = navController) {
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route
        ){
            native()
            banner()
            products()
        }
    }
}


