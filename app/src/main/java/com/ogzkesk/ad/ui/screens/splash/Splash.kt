package com.ogzkesk.ad.ui.screens.splash

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ogzkesk.ad.ui.navigation.Screen

fun NavGraphBuilder.splash(){
    composable(route = Screen.Splash.route){
        Splash()
    }
}

@Composable
fun Splash() {

    Scaffold {

    }
}