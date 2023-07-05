package com.ogzkesk.ad.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.ogzkesk.ad.ui.navigation.Root
import com.ogzkesk.ad.ui.theme.AdTheme
import com.ogzkesk.eonad.*
import com.ogzkesk.eonad.billing.Iap
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AdTheme {
                Root()
            }
        }
    }
}