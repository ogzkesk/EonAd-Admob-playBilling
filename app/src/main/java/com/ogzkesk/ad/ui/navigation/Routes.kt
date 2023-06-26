package com.ogzkesk.ad.ui.navigation

import android.graphics.Bitmap
import android.text.BoringLayout
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument


sealed class Screen(val route: String) {

    object Home : Screen("home")

    object Next : Screen("next")

    object Splash : Screen("splash")

}
