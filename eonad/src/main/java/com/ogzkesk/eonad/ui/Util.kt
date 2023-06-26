package com.ogzkesk.eonad.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.res.ResourcesCompat
import com.ogzkesk.eonad.R

@Composable
internal fun rememberBackground(darkModeSupport: Boolean): Int {
    val context = LocalContext.current
    val darkThemeOn = isSystemInDarkTheme()
    val white = ResourcesCompat.getColor(
        context.resources,
        R.color.white,
        context.resources.newTheme()
    )
    val black = ResourcesCompat.getColor(
        context.resources,
        R.color.black,
        context.resources.newTheme()
    )

    return remember(isSystemInDarkTheme()) {
        if (darkModeSupport) {
            if (darkThemeOn) black else white
        } else {
            white
        }
    }
}

@Composable
internal fun rememberForeground(darkModeSupport: Boolean): Int {
    val context = LocalContext.current
    val darkThemeOn = isSystemInDarkTheme()
    val white = ResourcesCompat.getColor(
        context.resources,
        R.color.white,
        context.resources.newTheme()
    )
    val black = ResourcesCompat.getColor(
        context.resources,
        R.color.black,
        context.resources.newTheme()
    )

    return remember(isSystemInDarkTheme()) {
        if (darkModeSupport) {
            if(darkThemeOn) white else black
        } else {
            black
        }
    }
}