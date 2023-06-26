package com.ogzkesk.ad.ui.navigation

import android.provider.DocumentsContract.Root
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ogzkesk.ad.ui.navigation.setup.ProvideNavHost
import com.ogzkesk.ad.ui.screens.home.home
import com.ogzkesk.ad.ui.screens.splash.splash


@Composable
fun Root() {

    val navController = rememberNavController()

    ProvideNavHost(navHostController = navController) {
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route
        ){
            splash()
            home()
        }
    }
}