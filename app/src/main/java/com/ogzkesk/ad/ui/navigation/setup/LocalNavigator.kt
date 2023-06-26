package com.ogzkesk.ad.ui.navigation.setup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController

val LocalNavigator: ProvidableCompositionLocal<Navigator> = compositionLocalOf { StubNavigator }

val navigator: Navigator
    @Composable get() = LocalNavigator.current



@Composable
fun ProvideNavHost(navHostController: NavHostController, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalNavigator provides NavHostNavigator(navHostController), content = content)
}