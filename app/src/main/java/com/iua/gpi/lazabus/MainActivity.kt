package com.iua.gpi.lazabus

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.iua.gpi.lazabus.ui.AppNavigation
import com.iua.gpi.lazabus.ui.theme.LazaBusTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Actividad principal de la aplicación.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()
        setContent {
            LazaBusTheme {
                AppNavigation()
            }
        }
    }
}
