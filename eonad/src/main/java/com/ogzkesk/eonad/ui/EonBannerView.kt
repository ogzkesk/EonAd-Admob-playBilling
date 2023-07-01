package com.ogzkesk.eonad.ui

import android.view.View
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun EonBannerView(view: View?) {
    view?.let {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { view }
        )
    }
}