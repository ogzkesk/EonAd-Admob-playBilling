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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("Mactivity :: onCreate")
        setContent {
            AdTheme {
                Root()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        println("Mactivity :: onStart")

    }

    override fun onResume() {
        super.onResume()
        println("Mactivity :: onResume")

    }

    override fun onPostResume() {
        super.onPostResume()
        println("Mactivity :: onPostResume")

    }

    override fun onStop() {
        super.onStop()
        println("Mactivity :: onStop")
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        println("Mactivity :: onSavedInstance")
    }

    override fun onRestart() {
        super.onRestart()
        println("Mactivity :: onRestart")

    }

    override fun onDestroy() {
        super.onDestroy()
        println("Mactivity :: onDestroy")
    }

}